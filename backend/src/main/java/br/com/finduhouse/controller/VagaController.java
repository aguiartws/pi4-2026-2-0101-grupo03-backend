package br.com.finduhouse.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.finduhouse.config.TokenAuthenticationFilter;
import br.com.finduhouse.dto.QuestionarioDtos;
import br.com.finduhouse.dto.VagaDtos;
import br.com.finduhouse.service.AuthenticatedUser;
import br.com.finduhouse.service.CompatibilidadeService;
import br.com.finduhouse.service.VagaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Validated
@RestController
@RequestMapping("/api/vagas")
public class VagaController {

    private final VagaService vagaService;
    private final CompatibilidadeService compatibilidadeService;

    public VagaController(
            VagaService vagaService,
            CompatibilidadeService compatibilidadeService) {
        this.vagaService = vagaService;
        this.compatibilidadeService = compatibilidadeService;
    }

    @PostMapping
    public ResponseEntity<VagaDtos.CadastroResponse> cadastrar(
            @Valid @RequestBody VagaDtos.CadastroRequest request,
            HttpServletRequest httpRequest) {
        AuthenticatedUser usuario = usuarioAutenticado(httpRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(vagaService.cadastrar(request, usuario.usuarioId()));
    }

    @GetMapping
    public ResponseEntity<VagaDtos.BuscaResponse> buscar(
            @RequestParam java.math.BigDecimal valorAluguelMaximo,
            @RequestParam java.math.BigDecimal distanciaMaxima) {
        return ResponseEntity.ok(vagaService.buscar(valorAluguelMaximo, distanciaMaxima));
    }

    @GetMapping("/mapa")
    public ResponseEntity<VagaDtos.MapaResponse> listarMapa() {
        return ResponseEntity.ok(vagaService.listarMapa());
    }

    // O mapeamento literal /mapa continua tendo prioridade sobre esta rota variável.
    @GetMapping("/{vagaId}")
    public ResponseEntity<VagaDtos.DetalheResponse> obterDetalhes(@PathVariable long vagaId) {
        return ResponseEntity.ok(vagaService.obterDetalhes(vagaId));
    }

    @PatchMapping("/{vagaId}")
    public ResponseEntity<VagaDtos.EdicaoResponse> editar(
            @PathVariable long vagaId,
            @Valid @RequestBody VagaDtos.EdicaoRequest request,
            HttpServletRequest httpRequest) {
        AuthenticatedUser usuario = usuarioAutenticado(httpRequest);
        return ResponseEntity.ok()
                .body(vagaService.editar(vagaId, request, usuario.usuarioId()));
    }

    @GetMapping("/responsavel/{responsavelId}")
    public ResponseEntity<VagaDtos.ListaResponsavelResponse> listarPorResponsavel(
            @PathVariable long responsavelId,
            HttpServletRequest httpRequest) {
        AuthenticatedUser usuario = usuarioAutenticado(httpRequest);
        return ResponseEntity.ok(
                vagaService.listarPorResponsavel(responsavelId, usuario.usuarioId()));
    }

    @GetMapping("/{vagaId}/compatibilidade")
    public ResponseEntity<QuestionarioDtos.CompatibilidadeResponse> obterCompatibilidade(
            @PathVariable long vagaId,
            @RequestParam long estudanteId,
            HttpServletRequest httpRequest) {
        AuthenticatedUser usuario = usuarioAutenticado(httpRequest);
        return ResponseEntity.ok(compatibilidadeService.obter(
                vagaId, estudanteId, usuario.usuarioId(), usuario.perfil()));
    }

    private AuthenticatedUser usuarioAutenticado(HttpServletRequest request) {
        return (AuthenticatedUser) request.getAttribute(
                TokenAuthenticationFilter.AUTHENTICATED_USER_ATTRIBUTE);
    }
}
