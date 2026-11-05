package br.com.finduhouse.controller;

import br.com.finduhouse.dto.FavoritoDtos;
import br.com.finduhouse.config.TokenAuthenticationFilter;
import br.com.finduhouse.service.AuthenticatedUser;
import br.com.finduhouse.service.FavoritoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/favoritos")
public class FavoritoController {

    private final FavoritoService favoritoService;

    public FavoritoController(FavoritoService favoritoService) {
        this.favoritoService = favoritoService;
    }

    @PostMapping
    public ResponseEntity<FavoritoDtos.CriacaoResponse> favoritar(
            @Valid @RequestBody FavoritoDtos.CriacaoRequest request,
            HttpServletRequest httpRequest) {
        AuthenticatedUser usuario = usuarioAutenticado(httpRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                favoritoService.favoritar(request, usuario.usuarioId()));
    }

    @GetMapping
    public ResponseEntity<FavoritoDtos.ListaResponse> listar(
            @RequestParam @Positive long estudanteId,
            HttpServletRequest httpRequest) {
        AuthenticatedUser usuario = usuarioAutenticado(httpRequest);
        return ResponseEntity.ok(favoritoService.listar(
                estudanteId,
                usuario.usuarioId()));
    }

    private AuthenticatedUser usuarioAutenticado(HttpServletRequest request) {
        return (AuthenticatedUser) request.getAttribute(
                TokenAuthenticationFilter.AUTHENTICATED_USER_ATTRIBUTE);
    }
}
