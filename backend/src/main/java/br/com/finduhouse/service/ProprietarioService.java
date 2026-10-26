package br.com.finduhouse.service;

import br.com.finduhouse.dto.ProprietarioDtos;

public interface ProprietarioService {

    ProprietarioDtos.CadastroResponse cadastrar(ProprietarioDtos.CadastroRequest request);
}
