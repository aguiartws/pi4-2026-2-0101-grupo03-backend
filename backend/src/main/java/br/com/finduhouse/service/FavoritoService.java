package br.com.finduhouse.service;

import br.com.finduhouse.dto.FavoritoDtos;

public interface FavoritoService {

    FavoritoDtos.CriacaoResponse favoritar(FavoritoDtos.CriacaoRequest request);

    FavoritoDtos.ListaResponse listar(long estudanteId);
}
