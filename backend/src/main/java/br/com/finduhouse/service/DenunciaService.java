package br.com.finduhouse.service;

import br.com.finduhouse.dto.DenunciaDtos;

public interface DenunciaService {

    DenunciaDtos.CriacaoResponse denunciar(
            long vagaId,
            DenunciaDtos.CriacaoRequest request);
}
