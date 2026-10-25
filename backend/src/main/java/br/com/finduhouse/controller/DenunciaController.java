package br.com.finduhouse.controller;

import br.com.finduhouse.dto.DenunciaDtos;
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

    @PostMapping("/{vagaId}/denuncia")
    public ResponseEntity<Void> denunciar(
            @PathVariable @Positive long vagaId,
            @Valid @RequestBody CriacaoRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    public record CriacaoRequest(
            @NotNull @Positive Long estudanteId,
            @NotNull DenunciaDtos.Motivo motivo,
            @NotBlank String descricao) {
    }
}
