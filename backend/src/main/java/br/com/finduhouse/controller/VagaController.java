package br.com.finduhouse.controller;

import br.com.finduhouse.dto.VagaDtos;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
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

@Validated
@RestController
@RequestMapping("/api/vagas")
public class VagaController {

    @PostMapping
    public ResponseEntity<Void> cadastrar(
            @Valid @RequestBody VagaDtos.CadastroRequest request) {
        return notImplemented();
    }

    @GetMapping
    public ResponseEntity<Void> buscar(
            @RequestParam @NotNull @DecimalMin("0.0") Double valorAluguelMaximo,
            @RequestParam @NotNull @DecimalMin("0.0") Double distanciaMaxima) {
        return notImplemented();
    }

    @GetMapping("/mapa")
    public ResponseEntity<Void> listarMapa() {
        return notImplemented();
    }

    @PatchMapping("/{vagaId}")
    public ResponseEntity<Void> editar(
            @PathVariable long vagaId,
            @Valid @RequestBody VagaDtos.EdicaoRequest request) {
        return notImplemented();
    }

    @GetMapping("/{vagaId}/compatibilidade")
    public ResponseEntity<Void> obterCompatibilidade(
            @PathVariable long vagaId,
            @RequestParam long estudanteId) {
        return notImplemented();
    }

    private ResponseEntity<Void> notImplemented() {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}
