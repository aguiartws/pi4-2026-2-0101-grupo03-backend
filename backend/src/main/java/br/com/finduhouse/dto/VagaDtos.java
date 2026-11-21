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
import jakarta.validation.constraints.Size;

public final class VagaDtos {

    private VagaDtos() {
    }

    public record CadastroRequest(
            @NotNull
            @Positive Long responsavelId,
            @NotBlank
            @Size(max = 255) String titulo,
            @NotBlank String descricao,
            @NotBlank String endereco,
            @NotNull
            @DecimalMin(value = "0.0", inclusive = false) BigDecimal valorAluguel,
            @NotNull
            @Positive Integer numeroMoradores,
            @NotBlank String regrasCasa,
            @NotNull BigDecimal latitude,
            @NotNull BigDecimal longitude,
            @NotNull
            @Valid Regras regras) {

    }

    public record CadastroResponse(
            long vagaId,
            Status status,
            LocalDateTime criadoEm) {

    }

    public record EdicaoRequest(
            @NotNull
            @Positive Long responsavelId,
            @NotBlank
            @Size(max = 255) String titulo,
            @NotBlank String descricao,
            @NotBlank String endereco,
            @NotNull
            @DecimalMin(value = "0.0", inclusive = false) BigDecimal valorAluguel,
            @NotNull
            @Positive Integer numeroMoradores,
            @NotBlank String regrasCasa,
            @NotNull BigDecimal latitude,
            @NotNull BigDecimal longitude,
            @NotNull
            @Valid Regras regrasConvivencia,
            @NotNull Status status) {

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
            LocalDateTime criadoEm,
            String fotoCapa) {
        public ListaResponsavelItem(
                long id,
                String titulo,
                String endereco,
                BigDecimal valorAluguel,
                Status status,
                LocalDateTime criadoEm) {
            this(id, titulo, endereco, valorAluguel, status, criadoEm, null);
        }

    }

    public record ListaResponsavelResponse(
            List<ListaResponsavelItem> vagas) {

    }

    public record BuscaItem(
            long id,
            String titulo,
            BigDecimal valorAluguel,
            BigDecimal distanciaCampus,
            String descricao,
            String endereco,
            BigDecimal latitude,
            BigDecimal longitude,
            String fotoCapa) {
        public BuscaItem(
                long id,
                String titulo,
                BigDecimal valorAluguel,
                BigDecimal distanciaCampus,
                String descricao,
                String endereco,
                BigDecimal latitude,
                BigDecimal longitude) {
            this(id, titulo, valorAluguel, distanciaCampus, descricao, endereco, latitude, longitude, null);
        }

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

    public record DetalheResponse(
            long id,
            String titulo,
            String descricao,
            String endereco,
            BigDecimal valorAluguel,
            Integer numeroMoradores,
            String regrasCasa,
            BigDecimal latitude,
            BigDecimal longitude,
            Status status,
            Regras regrasConvivencia,
            List<String> fotos) {
        public DetalheResponse(
                long id,
                String titulo,
                String descricao,
                String endereco,
                BigDecimal valorAluguel,
                Integer numeroMoradores,
                String regrasCasa,
                BigDecimal latitude,
                BigDecimal longitude,
                Status status,
                Regras regrasConvivencia) {
            this(id, titulo, descricao, endereco, valorAluguel, numeroMoradores, regrasCasa,
                    latitude, longitude, status, regrasConvivencia, List.of());
        }

    }

    public record FotosResponse(List<String> fotos) {
    }

    public record Regras(
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

    public enum Status {
        @JsonProperty("ativa")
        ATIVA,
        @JsonProperty("em_analise")
        EM_ANALISE,
        @JsonProperty("inativa")
        INATIVA
    }
}
