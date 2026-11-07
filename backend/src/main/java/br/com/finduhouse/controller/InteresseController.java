package br.com.finduhouse.controller;

import br.com.finduhouse.service.EmailService;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/vagas")
public class InteresseController {

    private final EmailService emailService;

    public InteresseController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/{vagaId}/interesse")
    public ResponseEntity<Void> solicitar(
            @PathVariable @Positive long vagaId,
            @RequestParam @Positive long estudanteId) {
        
        // E-mails fixos para teste
        String emailEstudante = "estudante.teste@puc-campinas.edu.br";
        String emailProprietario = "proprietario.teste@puc-campinas.edu.br";
        
        emailService.enviarEmailContato(
            emailEstudante, 
            emailProprietario, 
            "Novo interesse na sua vaga", 
            "Olá! O estudante " + estudanteId + " tem interesse na sua vaga " + vagaId + "."
        );

        return ResponseEntity.ok().build();
    }
}