package br.com.finduhouse.service;

import br.com.finduhouse.dto.VagaDtos;
import br.com.finduhouse.entity.QuestionarioEstudante;
import br.com.finduhouse.entity.Vaga;

public final class RegrasConverter {

    private RegrasConverter() {
    }

    public static VagaDtos.Regras regras(Vaga vaga) {
        return new VagaDtos.Regras(
                vaga.getAceitaBarulhoDepoisDas22(),
                vaga.getFuma(),
                vaga.getAceitaAnimais(),
                vaga.getGostaReceberVisitas(),
                vaga.getGostaCozinhar(),
                vaga.getEstudaEmCasa(),
                vaga.getAceitaDividirTarefas(),
                vaga.getAceitaDividirQuarto(),
                vaga.getAceitaFestas(),
                vaga.getComunicativo());
    }

    public static VagaDtos.Regras respostas(QuestionarioEstudante questionario) {
        return new VagaDtos.Regras(
                questionario.getAceitaBarulhoDepoisDas22(),
                questionario.getFuma(),
                questionario.getAceitaAnimais(),
                questionario.getGostaReceberVisitas(),
                questionario.getGostaCozinhar(),
                questionario.getEstudaEmCasa(),
                questionario.getAceitaDividirTarefas(),
                questionario.getAceitaDividirQuarto(),
                questionario.getAceitaFestas(),
                questionario.getComunicativo());
    }

}
