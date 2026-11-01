package br.com.finduhouse.service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {

    private final Map<String, TokenData> tokens = new ConcurrentHashMap<>();

    @Override
    public String gerar(long usuarioId, String perfil) {
        String token = UUID.randomUUID().toString();
        tokens.put(token, new TokenData(usuarioId, perfil));
        return token;
    }

    private record TokenData(long usuarioId, String perfil) {
    }
}
