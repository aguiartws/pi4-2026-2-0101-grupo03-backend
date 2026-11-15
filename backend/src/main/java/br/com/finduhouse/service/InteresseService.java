package br.com.finduhouse.service;

import br.com.finduhouse.dto.InteresseDtos;

public interface InteresseService {

    InteresseDtos.Response solicitar(
            long vagaId,
            long estudanteId,
            long estudanteAutenticadoId);

    // ADICIONE ESTA LINHA:
    InteresseDtos.ListaMatchesResponse listarMatches(long estudanteId, long estudanteAutenticadoId);

    InteresseDtos.ListaProprietarioMatchesResponse listarMatchesDoProprietario(
            long responsavelId,
            long responsavelAutenticadoId);
}