package br.com.finduhouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/auth")
public class RecuperacaoSenhaController {

    @Autowired
    private JavaMailSender mailSender;

    // armazenamento temporário em memória (Depois troque pelo seu banco Postgres!)
    private final Map<String, String> codigosSalvos = new ConcurrentHashMap<>();
    private final Map<String, String> tokensSalvos = new ConcurrentHashMap<>();

    @PostMapping("/solicitar-recuperacao")
    public ResponseEntity<?> solicitarCodigo(@RequestBody Map<String, String> body) {
        String emailDestino = body.get("email");

        if (emailDestino == null || emailDestino.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("mensagem", "E-mail é obrigatório."));
        }

        try {
            // 1. gera o código de 4 dígitos
            String codigo = String.format("%04d", new Random().nextInt(10000));
            
            // 2. salva temporariamente na memória
            codigosSalvos.put(emailDestino, codigo);

            // 3. prepara o e-mail
            SimpleMailMessage mensagem = new SimpleMailMessage();
            mensagem.setFrom("anacarolinafalvesb@gmail.com"); 
            mensagem.setTo(emailDestino);
            mensagem.setSubject("Código de Verificação - FindUHouse");
            mensagem.setText("Olá! Seu código para recuperar a senha é: " + codigo + "\n\nEle expira em 10 minutos.");

            // 4. envia o e-mail real
            mailSender.send(mensagem);

            return ResponseEntity.ok(Map.of("mensagem", "Se o e-mail estiver cadastrado, você receberá um código."));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("mensagem", "Erro ao enviar e-mail. Tente novamente."));
        }
    }

    @PostMapping("/verificar-codigo")
    public ResponseEntity<?> verificarCodigo(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String codigoDigitado = body.get("codigo");

        String codigoSalvo = codigosSalvos.get(email);

        if (codigoSalvo != null && codigoSalvo.equals(codigoDigitado)) {
            // código correto! gera um token
            String tokenRecuperacao = UUID.randomUUID().toString();
            tokensSalvos.put(tokenRecuperacao, email);
            
            // remove o código usado para não ser reutilizado
            codigosSalvos.remove(email);

            // devolve o token para o front-end
            return ResponseEntity.ok(Map.of("tokenRecuperacao", tokenRecuperacao));
        }

        return ResponseEntity.badRequest().body(Map.of("mensagem", "Código inválido ou expirado."));
    }
}