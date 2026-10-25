package br.com.finduhouse.dto;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public final class FavoritoDtos {

    private FavoritoDtos() {
    }

    public record CriacaoRequest(
            @NotNull @Positive Long estudanteId,
            @NotNull @Positive Long vagaId) {
    }

    public record CriacaoResponse(
            String mensagem,
            long estudanteId,
            long vagaId) {
    }

    public record ListaRequest(
            @NotNull @Positive Long estudanteId) {
    }

    public record Item(
            long id,
            String titulo,
            String endereco,
            BigDecimal valorAluguel,
            String descricao) {
    }

    public record ListaResponse(
            List<Item> favoritos) {
    }
}
