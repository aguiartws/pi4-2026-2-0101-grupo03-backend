package br.com.finduhouse.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.finduhouse.dto.CepDtos;
import br.com.finduhouse.service.CepService;
import jakarta.validation.constraints.Pattern;

@Validated
@RestController
@RequestMapping("/api/cep")
public class CepController {

    private final CepService cepService;

    public CepController(CepService cepService) {
        this.cepService = cepService;
    }

    // O regex também impede que o usuário injete quebras de linha no socket
    @GetMapping("/{cep}")
    public ResponseEntity<CepDtos.Response> consultar(
            @PathVariable @Pattern(regexp = "^\\d{5}-?\\d{3}$", message = "Formato de CEP inválido") String cep) {
        return ResponseEntity.ok(cepService.consultar(cep));
    }
}