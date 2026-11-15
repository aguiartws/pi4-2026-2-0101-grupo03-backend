package br.com.finduhouse.dto;

import java.math.BigDecimal;
import java.util.List;

public final class InteresseDtos {

    private InteresseDtos() {
    }

    public record Response(
            boolean sucesso,
            boolean emailEnviado,
            String mensagem) {
    }

    public record MatchItem(
            long vagaId,
            String titulo,
            String endereco,
            BigDecimal valorAluguel) {
    }

    public record ListaMatchesResponse(
            List<MatchItem> matches) {
    }
}