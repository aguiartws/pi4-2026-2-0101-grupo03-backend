package br.com.finduhouse.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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

    public record ProprietarioMatchItem(
            long interesseId,
            long vagaId,
            String tituloVaga,
            String nomeEstudante,
            String curso,
            String email,
            String telefone,
            String fotoPerfilUrl,
            String sobreMim,
            LocalDateTime criadoEm,
            Long estudanteId,
            Map<String, Boolean> questionario) {

    }

    public record ListaProprietarioMatchesResponse(
            List<ProprietarioMatchItem> matches) {

    }
}