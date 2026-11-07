package br.com.finduhouse.controller;

import br.com.finduhouse.config.TokenAuthenticationFilter;
import br.com.finduhouse.dto.DenunciaDtos;
import br.com.finduhouse.service.AuthenticatedUser;
import br.com.finduhouse.service.DenunciaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/vagas")
public class DenunciaController {

    private final DenunciaService denunciaService;

    public DenunciaController(DenunciaService denunciaService) {
        this.denunciaService = denunciaService;
    }

    @PostMapping("/{vagaId}/denuncia")
    public ResponseEntity<DenunciaDtos.CriacaoResponse> denunciar(
            @PathVariable @Positive long vagaId,
            @Valid @RequestBody CriacaoRequest request,
            HttpServletRequest httpRequest) {
        AuthenticatedUser usuario = usuarioAutenticado(httpRequest);
        DenunciaDtos.CriacaoRequest serviceRequest = new DenunciaDtos.CriacaoRequest(
                vagaId,
                request.estudanteId(),
                request.motivo(),
                request.descricao());
        return ResponseEntity.status(HttpStatus.CREATED).body(
                denunciaService.denunciar(vagaId, serviceRequest, usuario.usuarioId()));
    }

    private AuthenticatedUser usuarioAutenticado(HttpServletRequest request) {
        return (AuthenticatedUser) request.getAttribute(
                TokenAuthenticationFilter.AUTHENTICATED_USER_ATTRIBUTE);
    }

    public record CriacaoRequest(
            @NotNull @Positive Long estudanteId,
            @NotNull DenunciaDtos.Motivo motivo,
            @NotBlank String descricao) {
    }
}
