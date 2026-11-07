package br.com.finduhouse.service;

import java.math.BigDecimal;

public interface CalculadoraDistancia {

    BigDecimal calcular(BigDecimal latitude, BigDecimal longitude);
}
