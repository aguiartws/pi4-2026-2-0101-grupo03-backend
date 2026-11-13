package br.com.finduhouse.controller;

import br.com.finduhouse.config.TokenAuthenticationFilter;
import br.com.finduhouse.dto.InteresseDtos;
import br.com.finduhouse.service.AuthenticatedUser;
import br.com.finduhouse.service.InteresseService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/vagas")
public class InteresseController {

    private final InteresseService interesseService;

    public InteresseController(InteresseService interesseService) {
        this.interesseService = interesseService;
    }

    @PostMapping("/{vagaId}/interesse")
    public ResponseEntity<InteresseDtos.Response> solicitar(
            @PathVariable @Positive long vagaId,
            @RequestParam @Positive long estudanteId,
            HttpServletRequest request) {
        AuthenticatedUser usuario = (AuthenticatedUser) request.getAttribute(
                TokenAuthenticationFilter.AUTHENTICATED_USER_ATTRIBUTE);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                interesseService.solicitar(vagaId, estudanteId, usuario.usuarioId()));
    }
}
