package br.com.finduhouse.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public final class ProprietarioDtos {

    private ProprietarioDtos() {
    }

    public record CadastroRequest(
            @NotBlank String nome,
            @NotBlank @Email String email,
            @NotBlank String telefone,
            @NotBlank String senha) {
    }

    public record CadastroResponse(
            long proprietarioId,
            String nome,
            String email,
            String telefone,
            LocalDateTime criadoEm) {
    }
}
