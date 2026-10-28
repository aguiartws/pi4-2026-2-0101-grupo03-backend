package br.com.finduhouse.controller;

import br.com.finduhouse.dto.QuestionarioDtos;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/questionario")
public class QuestionarioController {

    @PostMapping("/questionario")
    public ResponseEntity<Void> salvar(
            @Valid @RequestBody QuestionarioDtos.SalvarRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}
