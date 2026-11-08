package br.com.finduhouse.service;

import java.util.List;

import br.com.finduhouse.dto.QuestionarioDtos;

public interface CalculadoraCompatibilidade {

    Resultado calcular(Respostas respostas, Regras regras);

    record Respostas(
            Boolean aceitaBarulhoDepoisDas22,
            Boolean fuma,
            Boolean aceitaAnimais,
            Boolean gostaReceberVisitas,
            Boolean gostaCozinhar,
            Boolean estudaEmCasa,
            Boolean aceitaDividirTarefas,
            Boolean aceitaDividirQuarto,
            Boolean aceitaFestas,
            Boolean comunicativo) {
    }

    record Regras(
            Boolean aceitaBarulhoDepoisDas22,
            Boolean fuma,
            Boolean aceitaAnimais,
            Boolean gostaReceberVisitas,
            Boolean gostaCozinhar,
            Boolean estudaEmCasa,
            Boolean aceitaDividirTarefas,
            Boolean aceitaDividirQuarto,
            Boolean aceitaFestas,
            Boolean comunicativo) {
    }

    record Resultado(int nota, List<QuestionarioDtos.Dimensao> dimensoes) {
    }
}
