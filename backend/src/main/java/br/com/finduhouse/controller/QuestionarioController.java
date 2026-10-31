package br.com.finduhouse.controller;

import br.com.finduhouse.dto.QuestionarioDtos;
import br.com.finduhouse.config.TokenAuthenticationFilter;
import br.com.finduhouse.service.AuthenticatedUser;
import br.com.finduhouse.service.QuestionarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/questionario")
public class QuestionarioController {

    private final QuestionarioService questionarioService;

    public QuestionarioController(QuestionarioService questionarioService) {
        this.questionarioService = questionarioService;
    }

    @PostMapping("/questionario")
    public ResponseEntity<QuestionarioDtos.SalvarResponse> salvar(
            @Valid @RequestBody QuestionarioDtos.SalvarRequest request,
            HttpServletRequest httpRequest) {
        AuthenticatedUser usuario = (AuthenticatedUser) httpRequest.getAttribute(
                TokenAuthenticationFilter.AUTHENTICATED_USER_ATTRIBUTE);
        return ResponseEntity.ok(questionarioService.salvar(
                request,
                usuario.usuarioId(),
                usuario.perfil()));
    }
}
