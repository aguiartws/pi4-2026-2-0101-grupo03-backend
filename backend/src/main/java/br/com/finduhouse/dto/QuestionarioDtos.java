package br.com.finduhouse.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public final class QuestionarioDtos {

    private QuestionarioDtos() {
    }

    public record SalvarRequest(
            @NotNull @Positive Long estudanteId,
            @NotNull @Valid Respostas respostas) {
    }

    public record SalvarResponse(
            boolean sucesso,
            LocalDateTime atualizadoEm) {
    }

    public record Respostas(
            @NotNull Boolean aceitaBarulhoDepoisDas22,
            @NotNull Boolean fuma,
            @NotNull Boolean aceitaAnimais,
            @NotNull Boolean gostaReceberVisitas,
            @NotNull Boolean gostaCozinhar,
            @NotNull Boolean estudaEmCasa,
            @NotNull Boolean aceitaDividirTarefas,
            @NotNull Boolean aceitaDividirQuarto,
            @NotNull Boolean aceitaFestas,
            @NotNull Boolean comunicativo) {
    }

    public record CompatibilidadeResponse(
            long vagaId,
            int nota,
            java.util.List<Dimensao> dimensoes) {
    }

    public record Dimensao(
            String nome,
            int peso,
            double similaridade,
            Impacto impacto) {
    }

    public enum Impacto {
        @JsonProperty("positivo")
        POSITIVO,
        @JsonProperty("negativo")
        NEGATIVO
    }
}
