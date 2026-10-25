package br.com.finduhouse.controller;

import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/responsaveis")
public class ResponsavelController {

    @GetMapping("/{responsavelId}/vagas")
    public ResponseEntity<Void> listarVagas(
            @PathVariable @Positive long responsavelId) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}
