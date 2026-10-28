package br.com.finduhouse.dto;

import java.time.LocalDateTime;

public record ErrorResponse(
        String codigo,
        String mensagem,
        LocalDateTime timestamp) {
}
