package br.com.finduhouse.service;

import java.util.Optional;

public interface TokenService {

    String gerar(long usuarioId, String perfil);

    String gerar(long usuarioId, String perfil, boolean lembrar);

    Optional<TokenData> validar(String token);

    record TokenData(long usuarioId, String perfil) {
    }
}
