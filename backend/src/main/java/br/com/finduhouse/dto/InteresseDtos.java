package br.com.finduhouse.dto;

public final class InteresseDtos {

    private InteresseDtos() {
    }

    public record Response(
            boolean sucesso,
            boolean emailEnviado,
            String mensagem) {
    }
}
