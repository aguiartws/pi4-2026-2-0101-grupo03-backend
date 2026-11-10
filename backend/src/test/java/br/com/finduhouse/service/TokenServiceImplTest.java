package br.com.finduhouse.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;

import org.junit.jupiter.api.Test;

class TokenServiceImplTest {

    private static final Instant AGORA = Instant.parse("2026-01-01T00:00:00Z");

    @Test
    void deveValidarTokenValido() {
        TokenServiceImpl service = new TokenServiceImpl(clockAt(AGORA));
        String token = service.gerar(42L, "estudante");

        assertEquals(
                new TokenService.TokenData(42L, "estudante"),
                service.validar(token).orElseThrow());
    }

    @Test
    void deveRejeitarTokenInexistente() {
        TokenServiceImpl service = new TokenServiceImpl(clockAt(AGORA));

        assertTrue(service.validar("token-inexistente").isEmpty());
    }

    @Test
    void deveRejeitarTokenExpirado() {
        MutableClock clock = new MutableClock(AGORA);
        TokenServiceImpl service = new TokenServiceImpl(clock);
        String token = service.gerar(42L, "estudante");

        clock.advanceSeconds(8 * 60 * 60L + 1);

        assertTrue(service.validar(token).isEmpty());
    }

    private Clock clockAt(Instant instant) {
        return Clock.fixed(instant, ZoneOffset.UTC);
    }

    private static final class MutableClock extends Clock {

        private Instant instant;

        private MutableClock(Instant instant) {
            this.instant = instant;
        }

        private void advanceSeconds(long seconds) {
            instant = instant.plusSeconds(seconds);
        }

        @Override
        public ZoneId getZone() {
            return ZoneOffset.UTC;
        }

        @Override
        public Clock withZone(ZoneId zone) {
            return this;
        }

        @Override
        public Instant instant() {
            return instant;
        }
    }
}
