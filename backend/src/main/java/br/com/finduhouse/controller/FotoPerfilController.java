package br.com.finduhouse.controller;

import br.com.finduhouse.config.TokenAuthenticationFilter;
import br.com.finduhouse.dto.FotoPerfilDtos;
import br.com.finduhouse.service.AuthenticatedUser;
import br.com.finduhouse.service.FotoPerfilService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FotoPerfilController {

    private final FotoPerfilService fotoPerfilService;

    public FotoPerfilController(FotoPerfilService fotoPerfilService) {
        this.fotoPerfilService = fotoPerfilService;
    }

    @PostMapping(
            value = "/api/alunos/{id}/foto",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FotoPerfilDtos.Response> atualizarFotoEstudante(
            @PathVariable long id,
            @RequestPart("foto") MultipartFile foto,
            HttpServletRequest request) {
        return ResponseEntity.ok(fotoPerfilService.atualizarFoto(
                FotoPerfilService.Tipo.ESTUDANTE,
                id,
                usuarioAutenticado(request),
                foto));
    }

    @PostMapping(
            value = "/api/proprietarios/{id}/foto",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FotoPerfilDtos.Response> atualizarFotoProprietario(
            @PathVariable long id,
            @RequestPart("foto") MultipartFile foto,
            HttpServletRequest request) {
        return ResponseEntity.ok(fotoPerfilService.atualizarFoto(
                FotoPerfilService.Tipo.PROPRIETARIO,
                id,
                usuarioAutenticado(request),
                foto));
    }

    private AuthenticatedUser usuarioAutenticado(HttpServletRequest request) {
        return (AuthenticatedUser) request.getAttribute(
                TokenAuthenticationFilter.AUTHENTICATED_USER_ATTRIBUTE);
    }
}
