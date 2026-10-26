package br.com.finduhouse.service;

import br.com.finduhouse.dto.InteresseDtos;

public interface InteresseService {

    InteresseDtos.Response solicitar(long vagaId, long estudanteId);
}
