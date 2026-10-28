package br.com.finduhouse.service;

import br.com.finduhouse.dto.QuestionarioDtos;

public interface QuestionarioService {

    QuestionarioDtos.SalvarResponse salvar(QuestionarioDtos.SalvarRequest request);
}
