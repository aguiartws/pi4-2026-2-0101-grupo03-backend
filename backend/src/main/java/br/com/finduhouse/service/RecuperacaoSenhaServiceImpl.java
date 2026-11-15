package br.com.finduhouse.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import br.com.finduhouse.entity.Usuario;
import br.com.finduhouse.exception.BusinessException;
import br.com.finduhouse.exception.ErrorCode;
import br.com.finduhouse.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Recuperação de senha por código enviado ao e-mail.
 *
 * <p>Códigos e tokens ficam apenas em memória: expiram sozinhos e são perdidos se o
 * backend reiniciar (o usuário só precisa pedir outro código).
 */
@Service
public class RecuperacaoSenhaServiceImpl implements RecuperacaoSenhaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecuperacaoSenhaServiceImpl.class);

    static final Duration VALIDADE_CODIGO = Duration.ofMinutes(10);
    static final Duration VALIDADE_TOKEN = Duration.ofMinutes(15);
    static final int MAX_TENTATIVAS = 5;

    private record CodigoPendente(String codigo, Instant expiraEm, int tentativas) {
    }

    private record TokenPendente(String email, Instant expiraEm) {
    }

    private final UsuarioRepository usuarioRepository;
    private final PasswordService passwordService;
    private final JavaMailSender mailSender;
    private final String remetente;
    private final Clock clock;
    private final SecureRandom random = new SecureRandom();

    private final Map<String, CodigoPendente> codigos = new ConcurrentHashMap<>();
    private final Map<String, TokenPendente> tokens = new ConcurrentHashMap<>();

    @Autowired
    public RecuperacaoSenhaServiceImpl(
            UsuarioRepository usuarioRepository,
            PasswordService passwordService,
            JavaMailSender mailSender,
            @Value("${finduhouse.mail.from:nao-responda@finduhouse.com}") String remetente) {
        this(usuarioRepository, passwordService, mailSender, remetente, Clock.systemUTC());
    }

    RecuperacaoSenhaServiceImpl(
            UsuarioRepository usuarioRepository,
            PasswordService passwordService,
            JavaMailSender mailSender,
            String remetente,
            Clock clock) {
        this.usuarioRepository = usuarioRepository;
        this.passwordService = passwordService;
        this.mailSender = mailSender;
        this.remetente = remetente;
        this.clock = clock;
    }

    @Override
    public void solicitarCodigo(String email) {
        String chave = normalizar(email);
        removerExpirados();

        if (usuarioRepository.findByEmail(chave).isEmpty()) {
            // Mesma resposta para cadastrado e não cadastrado: não revela quem tem conta.
            LOGGER.info("Recuperação de senha solicitada para e-mail não cadastrado");
            return;
        }

        String codigo = String.format("%04d", random.nextInt(10_000));
        codigos.put(chave, new CodigoPendente(codigo, clock.instant().plus(VALIDADE_CODIGO), 0));

        try {
            enviarCodigo(chave, codigo);
        } catch (MailException e) {
            codigos.remove(chave);
            LOGGER.error("Falha ao enviar o e-mail de recuperação de senha", e);
            throw new BusinessException(
                    ErrorCode.FALHA_ENVIO_EMAIL,
                    "Não foi possível enviar o e-mail de verificação. Tente novamente.");
        }
    }

    @Override
    public synchronized String verificarCodigo(String email, String codigo) {
        String chave = normalizar(email);
        Instant agora = clock.instant();
        CodigoPendente pendente = codigos.get(chave);

        if (pendente == null || agora.isAfter(pendente.expiraEm())) {
            codigos.remove(chave);
            throw codigoInvalido();
        }

        if (!iguais(pendente.codigo(), codigo)) {
            int tentativas = pendente.tentativas() + 1;
            if (tentativas >= MAX_TENTATIVAS) {
                codigos.remove(chave);
            } else {
                codigos.put(chave, new CodigoPendente(
                        pendente.codigo(), pendente.expiraEm(), tentativas));
            }
            throw codigoInvalido();
        }

        codigos.remove(chave);
        String token = UUID.randomUUID().toString();
        tokens.put(token, new TokenPendente(chave, agora.plus(VALIDADE_TOKEN)));
        return token;
    }

    @Override
    public synchronized void redefinirSenha(String tokenRecuperacao, String novaSenha) {
        TokenPendente pendente = tokens.get(tokenRecuperacao);

        if (pendente == null || clock.instant().isAfter(pendente.expiraEm())) {
            tokens.remove(tokenRecuperacao);
            throw tokenInvalido();
        }

        Usuario usuario = usuarioRepository.findByEmail(pendente.email())
                .orElseThrow(this::tokenInvalido);

        usuario.atualizarSenhaHash(passwordService.hash(novaSenha));
        usuarioRepository.save(usuario);
        tokens.remove(tokenRecuperacao);
    }

    private void enviarCodigo(String emailDestino, String codigo) {
        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setFrom(remetente);
        mensagem.setTo(emailDestino);
        mensagem.setSubject("Código de Verificação - FindUHouse");
        mensagem.setText(
                "Olá! Seu código para recuperar a senha é: " + codigo + "\n\n"
                        + "Ele expira em " + VALIDADE_CODIGO.toMinutes() + " minutos.\n"
                        + "Se você não pediu a recuperação, ignore este e-mail.");
        mailSender.send(mensagem);
    }

    private void removerExpirados() {
        Instant agora = clock.instant();
        codigos.values().removeIf(c -> agora.isAfter(c.expiraEm()));
        tokens.values().removeIf(t -> agora.isAfter(t.expiraEm()));
    }

    private static String normalizar(String email) {
        return email == null ? "" : email.trim().toLowerCase();
    }

    private static boolean iguais(String esperado, String informado) {
        String digitado = informado == null ? "" : informado.trim();
        return MessageDigest.isEqual(
                esperado.getBytes(StandardCharsets.UTF_8),
                digitado.getBytes(StandardCharsets.UTF_8));
    }

    private BusinessException codigoInvalido() {
        return new BusinessException(
                ErrorCode.CODIGO_RECUPERACAO_INVALIDO,
                "Código inválido ou expirado. Solicite um novo código se necessário.");
    }

    private BusinessException tokenInvalido() {
        return new BusinessException(
                ErrorCode.TOKEN_RECUPERACAO_INVALIDO,
                "Sessão de recuperação inválida ou expirada. Solicite um novo código.");
    }
}
