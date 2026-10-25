package br.com.finduhouse.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public final class EstudanteDtos {

    private EstudanteDtos() {
    }

    public record CadastroRequest(
            @NotBlank String nome,
            @NotBlank @Email String email,
            @NotBlank String telefone,
            @NotBlank String senha,
            @NotBlank String curso) {
    }

    public record CadastroResponse(
            long estudanteId,
            String nome,
            String email,
            String telefone,
            String curso,
            LocalDateTime criadoEm) {
    }
}
