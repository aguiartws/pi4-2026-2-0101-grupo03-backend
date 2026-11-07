package br.com.finduhouse.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.finduhouse.entity.Estudante;
import br.com.finduhouse.entity.Proprietario;
import br.com.finduhouse.entity.Usuario;
import br.com.finduhouse.entity.Vaga;
import br.com.finduhouse.repository.EstudanteRepository;
import br.com.finduhouse.repository.ProprietarioRepository;
import br.com.finduhouse.repository.VagaRepository;
import br.com.finduhouse.service.EmailService;
import jakarta.validation.constraints.Positive;

@Validated
@RestController
@RequestMapping("/api/vagas")
public class InteresseController {

    private final EmailService emailService;
    private final EstudanteRepository estudanteRepository;
    private final VagaRepository vagaRepository;
    private final ProprietarioRepository proprietarioRepository;

    public InteresseController(EmailService emailService,
                               EstudanteRepository estudanteRepository,
                               VagaRepository vagaRepository,
                               ProprietarioRepository proprietarioRepository) {
        this.emailService = emailService;
        this.estudanteRepository = estudanteRepository;
        this.vagaRepository = vagaRepository;
        this.proprietarioRepository = proprietarioRepository;
    }

    @PostMapping("/{vagaId}/interesse")
    public ResponseEntity<Void> solicitar(
            @PathVariable @Positive long vagaId,
            @RequestParam @Positive long estudanteId) {

        // 1) Buscar estudante
        Estudante estudante = estudanteRepository.findById(estudanteId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Estudante não encontrado: " + estudanteId));

        // 2) Buscar vaga
        Vaga vaga = vagaRepository.findById(vagaId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Vaga não encontrada: " + vagaId));

        // 3) O responsável da vaga é um Usuario; o Proprietario é 1:1 com ele
        Usuario responsavel = vaga.getResponsavel();
        Proprietario proprietario = proprietarioRepository.findById(responsavel.getId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Proprietário não encontrado para o usuário: " + responsavel.getId()));

        // 4) Enviar os dois e-mails (estudante + proprietário)
        emailService.enviarDoisEmails(
                estudante.getEmail(), estudante.getNome(), estudante.getTelefone(),
                proprietario.getEmail(), proprietario.getNome(), proprietario.getTelefone(),
                vaga.getTitulo()
        );

        return ResponseEntity.ok().build();
    }
}