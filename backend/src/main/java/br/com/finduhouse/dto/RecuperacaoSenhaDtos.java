package br.com.finduhouse.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public final class RecuperacaoSenhaDtos {

    private RecuperacaoSenhaDtos() {
    }

    public record SolicitarRequest(
            @NotBlank @Email String email) {
    }

    public record VerificarRequest(
            @NotBlank @Email String email,
            @NotBlank String codigo) {
    }

    public record RedefinirRequest(
            @NotBlank String tokenRecuperacao,
            @NotBlank @Size(min = 8, max = 72) String novaSenha) {
    }

    public record MensagemResponse(String mensagem) {
    }

    public record TokenRecuperacaoResponse(String tokenRecuperacao) {
    }
}
