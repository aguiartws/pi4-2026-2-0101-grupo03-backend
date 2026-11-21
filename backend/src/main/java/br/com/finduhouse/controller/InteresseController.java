package br.com.finduhouse.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.finduhouse.config.TokenAuthenticationFilter;
import br.com.finduhouse.dto.InteresseDtos;
import br.com.finduhouse.service.AuthenticatedUser;
import br.com.finduhouse.service.InteresseService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Positive;

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

    @DeleteMapping("/{vagaId}/interesse")
    public ResponseEntity<Void> desfazer(
            @PathVariable @Positive long vagaId,
            @RequestParam @Positive long estudanteId,
            HttpServletRequest request) {
        AuthenticatedUser usuario = (AuthenticatedUser) request.getAttribute(
                TokenAuthenticationFilter.AUTHENTICATED_USER_ATTRIBUTE);
        interesseService.desfazer(vagaId, estudanteId, usuario.usuarioId());
        return ResponseEntity.noContent().build();
    }

    // ADICIONE ESTE MÉTODO:
    @GetMapping("/meus-matches")
    public ResponseEntity<InteresseDtos.ListaMatchesResponse> listarMeusMatches(
            @RequestParam @Positive long estudanteId,
            HttpServletRequest request) {
        AuthenticatedUser usuario = (AuthenticatedUser) request.getAttribute(
                TokenAuthenticationFilter.AUTHENTICATED_USER_ATTRIBUTE);
        return ResponseEntity.ok(
                interesseService.listarMatches(estudanteId, usuario.usuarioId()));
    }

    @GetMapping("/responsavel/{responsavelId}/matches")
    public ResponseEntity<InteresseDtos.ListaProprietarioMatchesResponse> listarMatchesDoProprietario(
            @PathVariable @Positive long responsavelId,
            HttpServletRequest request) {
        AuthenticatedUser usuario = (AuthenticatedUser) request.getAttribute(
                TokenAuthenticationFilter.AUTHENTICATED_USER_ATTRIBUTE);
        return ResponseEntity.ok(
                interesseService.listarMatchesDoProprietario(
                        responsavelId,
                        usuario.usuarioId()));
    }
}