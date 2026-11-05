package br.com.finduhouse.service;

import br.com.finduhouse.dto.VagaDtos;
import br.com.finduhouse.entity.QuestionarioEstudante;
import br.com.finduhouse.entity.Vaga;

public final class RegrasConverter {

    private RegrasConverter() {
    }

    public static VagaDtos.Regras regras(Vaga vaga) {
        return new VagaDtos.Regras(
                enumValue(vaga.getHorarioDormirSemana(), VagaDtos.HorarioDormir.class),
                enumValue(vaga.getHorarioDormirFimSemana(), VagaDtos.HorarioDormir.class),
                enumValue(vaga.getHorarioAcordarSemana(), VagaDtos.HorarioAcordar.class),
                enumValue(vaga.getHorarioAcordarFimSemana(), VagaDtos.HorarioAcordar.class),
                enumValue(vaga.getNivelLimpeza(), VagaDtos.Nivel.class),
                enumValue(vaga.getNivelOrganizacao(), VagaDtos.Nivel.class),
                enumValue(vaga.getToleranciaRuido(), VagaDtos.ToleranciaRuido.class),
                enumValue(vaga.getFrequenciaVisitas(), VagaDtos.Frequencia.class),
                enumValue(vaga.getFrequenciaFestas(), VagaDtos.Frequencia.class),
                vaga.getTemPet());
    }

    public static VagaDtos.Regras respostas(QuestionarioEstudante questionario) {
        return new VagaDtos.Regras(
                enumValue(questionario.getHorarioDormirSemana(), VagaDtos.HorarioDormir.class),
                enumValue(questionario.getHorarioDormirFimSemana(), VagaDtos.HorarioDormir.class),
                enumValue(questionario.getHorarioAcordarSemana(), VagaDtos.HorarioAcordar.class),
                enumValue(questionario.getHorarioAcordarFimSemana(), VagaDtos.HorarioAcordar.class),
                enumValue(questionario.getNivelLimpeza(), VagaDtos.Nivel.class),
                enumValue(questionario.getNivelOrganizacao(), VagaDtos.Nivel.class),
                enumValue(questionario.getToleranciaRuido(), VagaDtos.ToleranciaRuido.class),
                enumValue(questionario.getFrequenciaVisitas(), VagaDtos.Frequencia.class),
                enumValue(questionario.getFrequenciaFestas(), VagaDtos.Frequencia.class),
                questionario.getTemPet());
    }

    private static <T extends Enum<T>> T enumValue(String value, Class<T> enumType) {
        try {
            return Enum.valueOf(enumType, value.toUpperCase(java.util.Locale.ROOT));
        } catch (IllegalArgumentException | NullPointerException exception) {
            throw new IllegalStateException("Valor de regra desconhecido: " + value, exception);
        }
    }
}
