package br.com.finduhouse.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public final class AuthDtos {

    private AuthDtos() {
    }

    public record LoginRequest(
            @NotBlank @Email String email,
            @NotBlank String senha) {
    }

    public record LoginResponse(
            String token,
            long usuarioId,
            Perfil perfil) {
    }

    public enum Perfil {
        @JsonProperty("estudante")
        ESTUDANTE,
        @JsonProperty("proprietario")
        PROPRIETARIO,
        @JsonProperty("admin")
        ADMIN
    }
}
