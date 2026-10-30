package br.com.finduhouse.controller;

import br.com.finduhouse.dto.ProprietarioDtos;
import br.com.finduhouse.service.ProprietarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/proprietarios")
public class ProprietarioController {

    private final ProprietarioService proprietarioService;

    public ProprietarioController(ProprietarioService proprietarioService) {
        this.proprietarioService = proprietarioService;
    }

    @PostMapping
    public ResponseEntity<ProprietarioDtos.CadastroResponse> cadastrar(
            @Valid @RequestBody ProprietarioDtos.CadastroRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(proprietarioService.cadastrar(request));
    }
}
