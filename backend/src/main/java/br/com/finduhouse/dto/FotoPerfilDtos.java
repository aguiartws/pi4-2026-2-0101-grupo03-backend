package br.com.finduhouse.dto;

public final class FotoPerfilDtos {

    private FotoPerfilDtos() {
    }

    public record Response(String fotoUrl) {
    }
}
