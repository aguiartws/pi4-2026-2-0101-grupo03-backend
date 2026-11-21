package br.com.finduhouse.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void deveMapearIntegridadeParaConflito() {
        var response = handler.handleDataIntegrity(
                new DataIntegrityViolationException("restricao"));

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("CONFLITO_DADOS", response.getBody().codigo());
    }

    @Test
    void devePreservarStatusDeResponseStatusException() {
        var response = handler.handleResponseStatus(
                new ResponseStatusException(
                        HttpStatus.SERVICE_UNAVAILABLE,
                        "Validador indisponível"));

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertEquals("SERVICO_INDISPONIVEL", response.getBody().codigo());
        assertEquals("Validador indisponível", response.getBody().mensagem());
    }
}
