package br.com.finduhouse.controller;

import jakarta.validation.constraints.Positive;
import br.com.finduhouse.config.TokenAuthenticationFilter;
import br.com.finduhouse.dto.VagaDtos;
import br.com.finduhouse.service.AuthenticatedUser;
import br.com.finduhouse.service.VagaService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/responsaveis")
public class ResponsavelController {

    private final VagaService vagaService;

    public ResponsavelController(VagaService vagaService) {
        this.vagaService = vagaService;
    }

    @GetMapping("/{responsavelId}/vagas")
    public ResponseEntity<VagaDtos.ListaResponsavelResponse> listarVagas(
            @PathVariable @Positive long responsavelId,
            HttpServletRequest request) {
        AuthenticatedUser usuario = (AuthenticatedUser) request.getAttribute(
                TokenAuthenticationFilter.AUTHENTICATED_USER_ATTRIBUTE);
        return ResponseEntity.ok(vagaService.listarPorResponsavel(
                responsavelId,
                usuario.usuarioId()));
    }
}
