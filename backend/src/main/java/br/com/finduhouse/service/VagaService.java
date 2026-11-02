package br.com.finduhouse.service;

import java.math.BigDecimal;

import br.com.finduhouse.dto.VagaDtos;

public interface VagaService {

    VagaDtos.CadastroResponse cadastrar(VagaDtos.CadastroRequest request, long usuarioAutenticadoId);

    VagaDtos.EdicaoResponse editar(
            long vagaId,
            VagaDtos.EdicaoRequest request,
            long usuarioAutenticadoId);

    VagaDtos.BuscaResponse buscar(BigDecimal valorAluguelMaximo, BigDecimal distanciaMaxima);

    VagaDtos.MapaResponse listarMapa();

    VagaDtos.ListaResponsavelResponse listarPorResponsavel(long responsavelId, long usuarioAutenticadoId);
}
