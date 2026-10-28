package br.com.finduhouse.controller;

import br.com.finduhouse.dto.FavoritoDtos;
import jakarta.validation.Valid;
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

    @PostMapping
    public ResponseEntity<Void> favoritar(
            @Valid @RequestBody FavoritoDtos.CriacaoRequest request) {
        return notImplemented();
    }

    @GetMapping
    public ResponseEntity<Void> listar(@RequestParam long estudanteId) {
        return notImplemented();
    }

    private ResponseEntity<Void> notImplemented() {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}
