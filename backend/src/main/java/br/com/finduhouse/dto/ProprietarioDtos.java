package br.com.finduhouse.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public final class ProprietarioDtos {

    private ProprietarioDtos() {
    }

    public record CadastroRequest(
            @NotBlank String nome,
            @NotBlank @Email String email,
            @NotBlank String telefone,
            @NotBlank String senha,
            @Size(max = 100) String cidade) {
        public CadastroRequest(
                String nome,
                String email,
                String telefone,
                String senha) {
            this(nome, email, telefone, senha, null);
        }
    }

    public record CadastroResponse(
            long proprietarioId,
            String nome,
            String email,
            String telefone,
            LocalDateTime criadoEm) {
    }
}
