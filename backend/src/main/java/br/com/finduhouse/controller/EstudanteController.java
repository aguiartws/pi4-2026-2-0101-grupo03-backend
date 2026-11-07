package br.com.finduhouse.controller;

import br.com.finduhouse.dto.EstudanteDtos;
import br.com.finduhouse.config.TokenAuthenticationFilter;
import br.com.finduhouse.dto.QuestionarioDtos;
import br.com.finduhouse.service.AuthenticatedUser;
import br.com.finduhouse.service.EstudanteService;
import br.com.finduhouse.service.QuestionarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/estudantes")
public class EstudanteController {

    private final EstudanteService estudanteService;
    private final QuestionarioService questionarioService;

    public EstudanteController(
            EstudanteService estudanteService,
            QuestionarioService questionarioService) {
        this.estudanteService = estudanteService;
        this.questionarioService = questionarioService;
    }

    @PostMapping
    public ResponseEntity<EstudanteDtos.CadastroResponse> cadastrar(
            @Valid @RequestBody EstudanteDtos.CadastroRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(estudanteService.cadastrar(request));
    }

    @GetMapping("/{estudanteId}/questionario")
    public ResponseEntity<QuestionarioDtos.Respostas> obterQuestionario(
            @PathVariable long estudanteId,
            HttpServletRequest httpRequest) {
        AuthenticatedUser usuario = (AuthenticatedUser) httpRequest.getAttribute(
                TokenAuthenticationFilter.AUTHENTICATED_USER_ATTRIBUTE);
        return ResponseEntity.ok(questionarioService.obter(
                estudanteId, usuario.usuarioId(), usuario.perfil()));
    }
}
