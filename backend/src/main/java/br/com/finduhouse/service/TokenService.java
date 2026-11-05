package br.com.finduhouse.service;

import java.util.Optional;

public interface TokenService {

    String gerar(long usuarioId, String perfil);

    Optional<TokenData> validar(String token);

    record TokenData(long usuarioId, String perfil) {
    }
}
