package br.com.finduhouse.service;

import java.util.List;

import br.com.finduhouse.dto.QuestionarioDtos;

public interface CalculadoraCompatibilidade {

    Resultado calcular(Respostas respostas, Regras regras);

    record Respostas(
            String horarioDormirSemana,
            String horarioDormirFimSemana,
            String horarioAcordarSemana,
            String horarioAcordarFimSemana,
            String nivelLimpeza,
            String nivelOrganizacao,
            String toleranciaRuido,
            String frequenciaVisitas,
            String frequenciaFestas,
            Boolean temPet) {
    }

    record Regras(
            String horarioDormirSemana,
            String horarioDormirFimSemana,
            String horarioAcordarSemana,
            String horarioAcordarFimSemana,
            String nivelLimpeza,
            String nivelOrganizacao,
            String toleranciaRuido,
            String frequenciaVisitas,
            String frequenciaFestas,
            Boolean temPet) {
    }

    record Resultado(int nota, List<QuestionarioDtos.Dimensao> dimensoes) {
    }
}
