package br.com.finduhouse.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.finduhouse.entity.Usuario;
import br.com.finduhouse.exception.BusinessException;
import br.com.finduhouse.exception.ErrorCode;
import br.com.finduhouse.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@ExtendWith(MockitoExtension.class)
class RecuperacaoSenhaServiceImplTest {

    private static final String EMAIL = "ana@puccampinas.edu.br";

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordService passwordService;

    @Mock
    private JavaMailSender mailSender;

    private MutableClock clock;
    private RecuperacaoSenhaServiceImpl service;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        clock = new MutableClock(Instant.parse("2026-11-15T12:00:00Z"));
        service = new RecuperacaoSenhaServiceImpl(
                usuarioRepository, passwordService, mailSender, "nao-responda@finduhouse.com", clock);
        usuario = new Usuario("Ana", EMAIL, "19999999999", "hash-antigo", Usuario.Perfil.ESTUDANTE);
    }

    @Test
    void solicitarCodigoEnviaEmailComCodigoDeQuatroDigitos() {
        when(usuarioRepository.findByEmail(EMAIL)).thenReturn(Optional.of(usuario));

        service.solicitarCodigo("  ANA@puccampinas.edu.br ");

        SimpleMailMessage email = capturarEmail();
        assertEquals(EMAIL, email.getTo()[0]);
        assertNotNull(extrairCodigo(email));
    }

    @Test
    void solicitarCodigoParaEmailNaoCadastradoNaoEnviaNada() {
        when(usuarioRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        service.solicitarCodigo(EMAIL);

        verify(mailSender, never()).send(any(SimpleMailMessage.class));
    }

    @Test
    void fluxoCompletoTrocaASenha() {
        when(usuarioRepository.findByEmail(EMAIL)).thenReturn(Optional.of(usuario));
        when(passwordService.hash("novaSenha123")).thenReturn("hash-novo");

        service.solicitarCodigo(EMAIL);
        String token = service.verificarCodigo(EMAIL, extrairCodigo(capturarEmail()));
        service.redefinirSenha(token, "novaSenha123");

        assertEquals("hash-novo", usuario.getSenhaHash());
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void tokenSoPodeSerUsadoUmaVez() {
        when(usuarioRepository.findByEmail(EMAIL)).thenReturn(Optional.of(usuario));
        when(passwordService.hash("novaSenha123")).thenReturn("hash-novo");

        service.solicitarCodigo(EMAIL);
        String token = service.verificarCodigo(EMAIL, extrairCodigo(capturarEmail()));
        service.redefinirSenha(token, "novaSenha123");

        BusinessException erro = assertThrows(
                BusinessException.class, () -> service.redefinirSenha(token, "outraSenha123"));
        assertEquals(ErrorCode.TOKEN_RECUPERACAO_INVALIDO, erro.getCodigo());
    }

    @Test
    void codigoExpiradoEhRejeitado() {
        when(usuarioRepository.findByEmail(EMAIL)).thenReturn(Optional.of(usuario));

        service.solicitarCodigo(EMAIL);
        String codigo = extrairCodigo(capturarEmail());
        clock.avancar(Duration.ofMinutes(11));

        BusinessException erro = assertThrows(
                BusinessException.class, () -> service.verificarCodigo(EMAIL, codigo));
        assertEquals(ErrorCode.CODIGO_RECUPERACAO_INVALIDO, erro.getCodigo());
    }

    @Test
    void depoisDeCincoErradosOCodigoCertoTambemEhRejeitado() {
        when(usuarioRepository.findByEmail(EMAIL)).thenReturn(Optional.of(usuario));

        service.solicitarCodigo(EMAIL);
        String codigoCerto = extrairCodigo(capturarEmail());
        String codigoErrado = codigoCerto.equals("0000") ? "1111" : "0000";

        for (int i = 0; i < RecuperacaoSenhaServiceImpl.MAX_TENTATIVAS; i++) {
            assertThrows(BusinessException.class, () -> service.verificarCodigo(EMAIL, codigoErrado));
        }

        BusinessException erro = assertThrows(
                BusinessException.class, () -> service.verificarCodigo(EMAIL, codigoCerto));
        assertEquals(ErrorCode.CODIGO_RECUPERACAO_INVALIDO, erro.getCodigo());
    }

    @Test
    void tokenInexistenteEhRejeitado() {
        BusinessException erro = assertThrows(
                BusinessException.class, () -> service.redefinirSenha("token-falso", "novaSenha123"));
        assertEquals(ErrorCode.TOKEN_RECUPERACAO_INVALIDO, erro.getCodigo());
        verify(usuarioRepository, never()).save(any());
    }

    private SimpleMailMessage capturarEmail() {
        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());
        return captor.getValue();
    }

    private static String extrairCodigo(SimpleMailMessage email) {
        Matcher matcher = Pattern.compile("\\b\\d{4}\\b").matcher(email.getText());
        if (!matcher.find()) {
            throw new AssertionError("O e-mail não contém um código de 4 dígitos");
        }
        return matcher.group();
    }

    private static final class MutableClock extends Clock {
        private Instant agora;

        MutableClock(Instant inicio) {
            this.agora = inicio;
        }

        void avancar(Duration duracao) {
            agora = agora.plus(duracao);
        }

        @Override
        public ZoneId getZone() {
            return ZoneOffset.UTC;
        }

        @Override
        public Clock withZone(ZoneId zone) {
            return this;
        }

        @Override
        public Instant instant() {
            return agora;
        }
    }
}
