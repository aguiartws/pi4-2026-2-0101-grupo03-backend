package br.com.finduhouse.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public final class EstudanteDtos {

    private EstudanteDtos() {
    }

    public record CadastroRequest(
            @NotBlank String nome,
            @NotBlank @Email String email,
            @NotBlank String telefone,
            @NotBlank String senha,
            @NotBlank String curso,
            @Size(max = 100) String cidade,
            @Size(max = 2000) String sobreMim) {

        public CadastroRequest(
                String nome,
                String email,
                String telefone,
                String senha,
                String curso) {
            this(nome, email, telefone, senha, curso, null, null);
        }
    }

    public record CadastroResponse(
            long estudanteId,
            String nome,
            String email,
            String telefone,
            String curso,
            String sobreMim,
            LocalDateTime criadoEm) {
    }
}
