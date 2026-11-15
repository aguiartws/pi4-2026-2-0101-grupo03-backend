package br.com.finduhouse.service;

import java.math.BigDecimal;
import java.util.List;

import br.com.finduhouse.dto.VagaDtos;

public interface VagaService {

    VagaDtos.CadastroResponse cadastrar(VagaDtos.CadastroRequest request, long usuarioAutenticadoId);

    VagaDtos.EdicaoResponse editar(long vagaId, VagaDtos.EdicaoRequest request, long usuarioAutenticadoId);

    VagaDtos.BuscaResponse buscar(BigDecimal valorAluguelMaximo, BigDecimal distanciaMaxima);

    VagaDtos.MapaResponse listarMapa();

    VagaDtos.DetalheResponse obterDetalhes(long vagaId);

    VagaDtos.ListaResponsavelResponse listarPorResponsavel(long responsavelId, long usuarioAutenticadoId);

    void excluir(long vagaId, long usuarioAutenticadoId);

    VagaDtos.FotosResponse adicionarFotos(
            long vagaId,
            List<org.springframework.web.multipart.MultipartFile> fotos,
            long usuarioAutenticadoId);

    void removerFoto(long vagaId, long fotoId, long usuarioAutenticadoId);
}