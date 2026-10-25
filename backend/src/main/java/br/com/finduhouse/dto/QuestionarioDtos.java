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
            @NotNull VagaDtos.HorarioDormir horarioDormirSemana,
            @NotNull VagaDtos.HorarioDormir horarioDormirFimSemana,
            @NotNull VagaDtos.HorarioAcordar horarioAcordarSemana,
            @NotNull VagaDtos.HorarioAcordar horarioAcordarFimSemana,
            @NotNull VagaDtos.Nivel nivelLimpeza,
            @NotNull VagaDtos.Nivel nivelOrganizacao,
            @NotNull VagaDtos.ToleranciaRuido toleranciaRuido,
            @NotNull VagaDtos.Frequencia frequenciaVisitas,
            @NotNull VagaDtos.Frequencia frequenciaFestas,
            @NotNull Boolean temPet) {
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
