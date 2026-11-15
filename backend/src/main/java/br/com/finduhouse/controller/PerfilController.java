package br.com.finduhouse.controller;

import br.com.finduhouse.config.TokenAuthenticationFilter;
import br.com.finduhouse.dto.PerfilDtos;
import br.com.finduhouse.service.AuthenticatedUser;
import br.com.finduhouse.service.PerfilService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Validated
@RestController
@RequestMapping("/api/usuario/perfil")
public class PerfilController {

    private final PerfilService perfilService;

    public PerfilController(PerfilService perfilService) {
        this.perfilService = perfilService;
    }

    @GetMapping
    public ResponseEntity<PerfilDtos.PerfilResponse> obter(HttpServletRequest request) {
        return ResponseEntity.ok(perfilService.obter(usuarioAutenticado(request)));
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PerfilDtos.PerfilResponse> atualizar(
            @RequestParam(required = false) String tipoUsuario,
            @RequestParam @NotBlank String nome,
            @RequestParam @NotBlank String email,
            @RequestParam @NotBlank String telefone,
            @RequestParam(required = false) String cidade,
            @RequestParam(required = false) String curso,
            @RequestParam(required = false) String novaSenha,
            @RequestParam(defaultValue = "false") boolean removerFoto,
            @RequestPart(name = "fotoPerfil", required = false) MultipartFile fotoPerfil,
            HttpServletRequest request) {
        return ResponseEntity.ok(perfilService.atualizar(
                usuarioAutenticado(request),
                tipoUsuario,
                nome,
                email,
                telefone,
                cidade,
                curso,
                novaSenha,
                removerFoto,
                fotoPerfil));
    }

    private AuthenticatedUser usuarioAutenticado(HttpServletRequest request) {
        return (AuthenticatedUser) request.getAttribute(
                TokenAuthenticationFilter.AUTHENTICATED_USER_ATTRIBUTE);
    }
}
