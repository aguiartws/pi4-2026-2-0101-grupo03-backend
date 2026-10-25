package br.com.finduhouse.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public final class DenunciaDtos {

    private DenunciaDtos() {
    }

    public record CriacaoRequest(
            @NotNull @Positive Long vagaId,
            @NotNull @Positive Long estudanteId,
            @NotNull Motivo motivo,
            @NotBlank String descricao) {
    }

    public record CriacaoResponse(
            boolean sucesso,
            long denunciaId,
            String mensagem) {
    }

    public enum Motivo {
        @JsonProperty("informacao_falsa")
        INFORMACAO_FALSA,
        @JsonProperty("golpe_financeiro")
        GOLPE_FINANCEIRO,
        @JsonProperty("conteudo_inadequado")
        CONTEUDO_INADEQUADO,
        @JsonProperty("imovel_inexistente")
        IMOVEL_INEXISTENTE,
        @JsonProperty("outro")
        OUTRO
    }
}
