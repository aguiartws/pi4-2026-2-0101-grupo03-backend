package br.com.finduhouse.service;

import java.util.ArrayList;
import java.util.List;

import br.com.finduhouse.dto.QuestionarioDtos;
import org.springframework.stereotype.Component;

@Component
public class CalculadoraCompatibilidadeImpl implements CalculadoraCompatibilidade {

    private static final String[] NOMES = {
        "Horário de dormir (semana)",
        "Horário de dormir (fim de semana)",
        "Horário de acordar (semana)",
        "Horário de acordar (fim de semana)",
        "Limpeza",
        "Organização",
        "Tolerância a ruído",
        "Frequência de visitas",
        "Frequência de festas",
        "Tem pet"
    };
    private static final int[] PESOS = {15, 15, 10, 10, 10, 10, 10, 5, 10, 5};

    @Override
    public Resultado calcular(Respostas respostas, Regras regras) {
        int[] estudante = valores(respostas);
        int[] vaga = valores(regras);
        List<QuestionarioDtos.Dimensao> dimensoes = new ArrayList<>();
        double total = 0;
        for (int i = 0; i < estudante.length; i++) {
            double similaridade = 1.0 - Math.abs(estudante[i] - vaga[i]) / 2.0;
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

    private int[] valores(Respostas respostas) {
        return converter(
                respostas.horarioDormirSemana(),
                respostas.horarioDormirFimSemana(),
                respostas.horarioAcordarSemana(),
                respostas.horarioAcordarFimSemana(),
                respostas.nivelLimpeza(),
                respostas.nivelOrganizacao(),
                respostas.toleranciaRuido(),
                respostas.frequenciaVisitas(),
                respostas.frequenciaFestas(),
                respostas.temPet());
    }

    private int[] valores(Regras regras) {
        return converter(
                regras.horarioDormirSemana(),
                regras.horarioDormirFimSemana(),
                regras.horarioAcordarSemana(),
                regras.horarioAcordarFimSemana(),
                regras.nivelLimpeza(),
                regras.nivelOrganizacao(),
                regras.toleranciaRuido(),
                regras.frequenciaVisitas(),
                regras.frequenciaFestas(),
                regras.temPet());
    }

    private int[] converter(
            String dormirSemana,
            String dormirFimSemana,
            String acordarSemana,
            String acordarFimSemana,
            String limpeza,
            String organizacao,
            String ruido,
            String visitas,
            String festas,
            Boolean pet) {
        return new int[] {
            horarioDormir(dormirSemana),
            horarioDormir(dormirFimSemana),
            horarioAcordar(acordarSemana),
            horarioAcordar(acordarFimSemana),
            nivel(limpeza),
            nivel(organizacao),
            nivelRuido(ruido),
            frequencia(visitas),
            frequencia(festas),
            pet(pet)
        };
    }

    private int horarioDormir(String valor) {
        return switch (valor) {
            case "antes_das_22" -> 1;
            case "entre_22_e_0" -> 2;
            case "depois_da_0" -> 3;
            default -> throw new IllegalStateException("Valor de horário de dormir desconhecido: " + valor);
        };
    }

    private int horarioAcordar(String valor) {
        return switch (valor) {
            case "antes_das_6" -> 1;
            case "entre_6_e_8" -> 2;
            case "depois_das_8" -> 3;
            default -> throw new IllegalStateException("Valor de horário de acordar desconhecido: " + valor);
        };
    }

    private int nivel(String valor) {
        return switch (valor) {
            case "baixo" -> 1;
            case "medio" -> 2;
            case "alto" -> 3;
            default -> throw new IllegalStateException("Valor de nível desconhecido: " + valor);
        };
    }

    private int nivelRuido(String valor) {
        return switch (valor) {
            case "baixa" -> 1;
            case "media" -> 2;
            case "alta" -> 3;
            default -> throw new IllegalStateException("Valor de tolerância a ruído desconhecido: " + valor);
        };
    }

    private int frequencia(String valor) {
        return switch (valor) {
            case "nunca" -> 1;
            case "as_vezes" -> 2;
            case "frequentemente" -> 3;
            default -> throw new IllegalStateException("Valor de frequência desconhecido: " + valor);
        };
    }

    private int pet(Boolean valor) {
        if (valor == null) {
            throw new IllegalStateException("Valor de pet desconhecido: null");
        }
        return valor ? 3 : 1;
    }
}
