package br.com.finduhouse.service;

import br.com.finduhouse.dto.QuestionarioDtos;

public interface CompatibilidadeService {

    QuestionarioDtos.CompatibilidadeResponse obter(
            long vagaId,
            long estudanteId,
            long usuarioAutenticadoId,
            String perfilAutenticado);
}
