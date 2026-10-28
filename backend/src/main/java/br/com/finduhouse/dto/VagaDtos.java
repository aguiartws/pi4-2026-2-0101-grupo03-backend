package br.com.finduhouse.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public final class VagaDtos {

    private VagaDtos() {
    }

    public record CadastroRequest(
            @NotNull @Positive Long responsavelId,
            @NotBlank String titulo,
            @NotBlank String descricao,
            @NotBlank String endereco,
            @NotNull @DecimalMin(value = "0.0", inclusive = false) BigDecimal valorAluguel,
            @NotNull @Positive Integer numeroMoradores,
            @NotBlank String regrasCasa,
            @NotNull BigDecimal latitude,
            @NotNull BigDecimal longitude,
            @NotNull @Valid Regras regras) {
    }

    public record CadastroResponse(
            long vagaId,
            Status status,
            LocalDateTime criadoEm) {
    }

    public record EdicaoRequest(
            @NotNull @Positive Long responsavelId,
            @NotNull @DecimalMin(value = "0.0", inclusive = false) BigDecimal valorAluguel) {
    }

    public record EdicaoResponse(
            long vagaId,
            BigDecimal valorAluguel,
            LocalDateTime atualizadoEm) {
    }

    public record ListaResponsavelItem(
            long id,
            String titulo,
            String endereco,
            BigDecimal valorAluguel,
            Status status,
            LocalDateTime criadoEm) {
    }

    public record ListaResponsavelResponse(
            List<ListaResponsavelItem> vagas) {
    }

    public record BuscaItem(
            long id,
            String titulo,
            BigDecimal valorAluguel,
            BigDecimal distanciaCampus,
            String descricao) {
    }

    public record BuscaResponse(
            List<BuscaItem> vagas) {
    }

    public record MapaItem(
            long id,
            String titulo,
            String endereco,
            BigDecimal valorAluguel,
            BigDecimal latitude,
            BigDecimal longitude) {
    }

    public record MapaResponse(
            List<MapaItem> vagas) {
    }

    public record Regras(
            @NotNull HorarioDormir horarioDormirSemana,
            @NotNull HorarioDormir horarioDormirFimSemana,
            @NotNull HorarioAcordar horarioAcordarSemana,
            @NotNull HorarioAcordar horarioAcordarFimSemana,
            @NotNull Nivel nivelLimpeza,
            @NotNull Nivel nivelOrganizacao,
            @NotNull ToleranciaRuido toleranciaRuido,
            @NotNull Frequencia frequenciaVisitas,
            @NotNull Frequencia frequenciaFestas,
            @NotNull Boolean temPet) {
    }

    public enum Status {
        @JsonProperty("ativa")
        ATIVA,
        @JsonProperty("em_analise")
        EM_ANALISE,
        @JsonProperty("inativa")
        INATIVA
    }

    public enum HorarioDormir {
        @JsonProperty("antes_das_22")
        ANTES_DAS_22,
        @JsonProperty("entre_22_e_0")
        ENTRE_22_E_0,
        @JsonProperty("depois_da_0")
        DEPOIS_DA_0
    }

    public enum HorarioAcordar {
        @JsonProperty("antes_das_6")
        ANTES_DAS_6,
        @JsonProperty("entre_6_e_8")
        ENTRE_6_E_8,
        @JsonProperty("depois_das_8")
        DEPOIS_DAS_8
    }

    public enum Nivel {
        @JsonProperty("baixo")
        BAIXO,
        @JsonProperty("medio")
        MEDIO,
        @JsonProperty("alto")
        ALTO
    }

    public enum ToleranciaRuido {
        @JsonProperty("baixa")
        BAIXA,
        @JsonProperty("media")
        MEDIA,
        @JsonProperty("alta")
        ALTA
    }

    public enum Frequencia {
        @JsonProperty("nunca")
        NUNCA,
        @JsonProperty("as_vezes")
        AS_VEZES,
        @JsonProperty("frequentemente")
        FREQUENTEMENTE
    }
}
