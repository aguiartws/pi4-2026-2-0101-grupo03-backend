package br.com.finduhouse.service;

import br.com.finduhouse.dto.EstudanteDtos;

public interface EstudanteService {

    EstudanteDtos.CadastroResponse cadastrar(EstudanteDtos.CadastroRequest request);
}
