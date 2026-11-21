package br.com.finduhouse.dto;

public final class PerfilDtos {

    private PerfilDtos() {
    }

    public record PerfilResponse(
            long id,
            String tipoUsuario,
            String nome,
            String email,
            String telefone,
            String cidade,
            String curso,
            String fotoPerfilUrl,
            String sobreMim) {
    }
}
