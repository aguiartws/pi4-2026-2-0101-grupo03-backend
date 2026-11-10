package br.com.finduhouse.service;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {

    private static final Duration SESSAO_NORMAL = Duration.ofHours(8);
    private static final Duration SESSAO_LEMBRADA = Duration.ofDays(30);

    private record Entrada(TokenData data, Instant expiraEm) {
    }

    private final Clock clock;
    private final Map<String, Entrada> tokens = new ConcurrentHashMap<>();

    public TokenServiceImpl() {
        this(Clock.systemUTC());
    }

    TokenServiceImpl(Clock clock) {
        this.clock = clock;
    }

    @Override
    public String gerar(long usuarioId, String perfil) {
        return gerar(usuarioId, perfil, false);
    }

    @Override
    public String gerar(long usuarioId, String perfil, boolean lembrar) {
        String token = UUID.randomUUID().toString();
        Duration duracao = lembrar ? SESSAO_LEMBRADA : SESSAO_NORMAL;
        tokens.put(token, new Entrada(
                new TokenData(usuarioId, perfil),
                Instant.now(clock).plus(duracao)));
        return token;
    }

    @Override
    public Optional<TokenData> validar(String token) {
        if (token == null || token.isBlank()) {
            return Optional.empty();
        }
        Entrada entrada = tokens.get(token);
        if (entrada == null) {
            return Optional.empty();
        }
        if (Instant.now(clock).isAfter(entrada.expiraEm())) {
            tokens.remove(token);
            return Optional.empty();
        }
        return Optional.of(entrada.data());
    }
}
