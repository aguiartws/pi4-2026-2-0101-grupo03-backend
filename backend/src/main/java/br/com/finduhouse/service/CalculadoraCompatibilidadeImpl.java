package br.com.finduhouse.service;

import java.util.ArrayList;
import java.util.List;

import br.com.finduhouse.dto.QuestionarioDtos;
import org.springframework.stereotype.Component;

@Component
public class CalculadoraCompatibilidadeImpl implements CalculadoraCompatibilidade {

    private static final String[] NOMES = {
        "Aceita barulho depois das 22h",
        "Fuma",
        "Aceita animais",
        "Gosta de receber visitas",
        "Gosta de cozinhar",
        "Estuda em casa",
        "Aceita dividir tarefas",
        "Aceita dividir quarto",
        "Aceita festas",
        "É comunicativo"
    };
    private static final int[] PESOS = {15, 15, 10, 10, 10, 10, 10, 5, 10, 5};

    @Override
    public Resultado calcular(Respostas respostas, Regras regras) {
        boolean[] estudante = valores(respostas);
        boolean[] vaga = valores(regras);
        List<QuestionarioDtos.Dimensao> dimensoes = new ArrayList<>();
        double total = 0;
        for (int i = 0; i < estudante.length; i++) {
            double similaridade = estudante[i] == vaga[i] ? 1.0 : 0.0;
            total += similaridade * PESOS[i];
            dimensoes.add(new QuestionarioDtos.Dimensao(
                    NOMES[i],
                    PESOS[i],
                    similaridade,
                    similaridade >= 0.7
                            ? QuestionarioDtos.Impacto.POSITIVO
                            : QuestionarioDtos.Impacto.NEGATIVO));
        }
        return new Resultado((int) Math.floor(total + 0.5), dimensoes);
    }

    private boolean[] valores(Respostas respostas) {
        return converter(
                respostas.aceitaBarulhoDepoisDas22(),
                respostas.fuma(),
                respostas.aceitaAnimais(),
                respostas.gostaReceberVisitas(),
                respostas.gostaCozinhar(),
                respostas.estudaEmCasa(),
                respostas.aceitaDividirTarefas(),
                respostas.aceitaDividirQuarto(),
                respostas.aceitaFestas(),
                respostas.comunicativo());
    }

    private boolean[] valores(Regras regras) {
        return converter(
                regras.aceitaBarulhoDepoisDas22(),
                regras.fuma(),
                regras.aceitaAnimais(),
                regras.gostaReceberVisitas(),
                regras.gostaCozinhar(),
                regras.estudaEmCasa(),
                regras.aceitaDividirTarefas(),
                regras.aceitaDividirQuarto(),
                regras.aceitaFestas(),
                regras.comunicativo());
    }

    private boolean[] converter(Boolean... respostas) {
        for (Boolean resposta : respostas) {
            if (resposta == null) {
                throw new IllegalStateException("Resposta de questionário desconhecida: null");
            }
        }
        boolean[] valores = new boolean[respostas.length];
        for (int i = 0; i < respostas.length; i++) {
            valores[i] = respostas[i];
        }
        return valores;
    }
}
