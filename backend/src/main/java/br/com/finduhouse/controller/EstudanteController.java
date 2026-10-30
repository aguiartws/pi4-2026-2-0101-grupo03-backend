package br.com.finduhouse.controller;

import br.com.finduhouse.dto.EstudanteDtos;
import br.com.finduhouse.service.EstudanteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/estudantes")
public class EstudanteController {

    private final EstudanteService estudanteService;

    public EstudanteController(EstudanteService estudanteService) {
        this.estudanteService = estudanteService;
    }

    @PostMapping
    public ResponseEntity<EstudanteDtos.CadastroResponse> cadastrar(
            @Valid @RequestBody EstudanteDtos.CadastroRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(estudanteService.cadastrar(request));
    }
}
