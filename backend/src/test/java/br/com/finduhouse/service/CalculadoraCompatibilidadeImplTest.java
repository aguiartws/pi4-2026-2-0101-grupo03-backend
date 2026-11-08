package br.com.finduhouse.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import br.com.finduhouse.dto.QuestionarioDtos;
import org.junit.jupiter.api.Test;

class CalculadoraCompatibilidadeImplTest {

    private final CalculadoraCompatibilidade calculadora = new CalculadoraCompatibilidadeImpl();

    @Test
    void deveAtribuirNotaMaximaQuandoTodasAsRespostasForemIguais() {
        var resultado = calculadora.calcular(respostas(true), regras(true));

        assertEquals(100, resultado.nota());
        assertEquals(10, resultado.dimensoes().size());
        assertEquals(10, resultado.dimensoes().stream()
                .filter(dimensao -> dimensao.impacto() == QuestionarioDtos.Impacto.POSITIVO)
                .count());
    }

    @Test
    void deveAplicarPesoDaPerguntaQuandoUmaRespostaForDiferente() {
        var resultado = calculadora.calcular(respostas(true), regras(false, true));

        assertEquals(85, resultado.nota());
        assertEquals(1, resultado.dimensoes().stream()
                .filter(dimensao -> dimensao.impacto() == QuestionarioDtos.Impacto.NEGATIVO)
                .count());
    }

    private CalculadoraCompatibilidade.Respostas respostas(boolean valor) {
        return new CalculadoraCompatibilidade.Respostas(
                valor, valor, valor, valor, valor,
                valor, valor, valor, valor, valor);
    }

    private CalculadoraCompatibilidade.Regras regras(boolean valor) {
        return new CalculadoraCompatibilidade.Regras(
                valor, valor, valor, valor, valor,
                valor, valor, valor, valor, valor);
    }

    private CalculadoraCompatibilidade.Regras regras(boolean primeira, boolean demais) {
        return new CalculadoraCompatibilidade.Regras(
                primeira, demais, demais, demais, demais,
                demais, demais, demais, demais, demais);
    }
}
