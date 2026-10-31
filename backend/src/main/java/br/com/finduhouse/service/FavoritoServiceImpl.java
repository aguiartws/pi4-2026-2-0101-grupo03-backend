package br.com.finduhouse.service;

import java.util.List;

import br.com.finduhouse.dto.FavoritoDtos;
import br.com.finduhouse.entity.Estudante;
import br.com.finduhouse.entity.Favorito;
import br.com.finduhouse.entity.Vaga;
import br.com.finduhouse.exception.BusinessException;
import br.com.finduhouse.exception.ErrorCode;
import br.com.finduhouse.repository.EstudanteRepository;
import br.com.finduhouse.repository.FavoritoRepository;
import br.com.finduhouse.repository.VagaRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FavoritoServiceImpl implements FavoritoService {

    private final EstudanteRepository estudanteRepository;
    private final VagaRepository vagaRepository;
    private final FavoritoRepository favoritoRepository;

    public FavoritoServiceImpl(
            EstudanteRepository estudanteRepository,
            VagaRepository vagaRepository,
            FavoritoRepository favoritoRepository) {
        this.estudanteRepository = estudanteRepository;
        this.vagaRepository = vagaRepository;
        this.favoritoRepository = favoritoRepository;
    }

    @Override
    @Transactional
    public FavoritoDtos.CriacaoResponse favoritar(
            FavoritoDtos.CriacaoRequest request,
            long estudanteAutenticadoId) {
        validarEstudanteAutenticado(request.estudanteId(), estudanteAutenticadoId);

        Estudante estudante = estudanteRepository.findById(request.estudanteId())
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.ESTUDANTE_NAO_ENCONTRADO,
                        "Estudante não encontrado"));
        Vaga vaga = vagaRepository.findById(request.vagaId())
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.VAGA_NAO_ENCONTRADA,
                        "Vaga não encontrada"));

        if (favoritoRepository.existsByIdEstudanteIdAndIdVagaId(
                request.estudanteId(), request.vagaId())) {
            throw favoritoDuplicado();
        }

        try {
            favoritoRepository.save(new Favorito(estudante, vaga));
        } catch (DataIntegrityViolationException exception) {
            throw favoritoDuplicado();
        }

        return new FavoritoDtos.CriacaoResponse(
                "Vaga favoritada com sucesso",
                request.estudanteId(),
                request.vagaId());
    }

    @Override
    @Transactional(readOnly = true)
    public FavoritoDtos.ListaResponse listar(
            long estudanteId,
            long estudanteAutenticadoId) {
        validarEstudanteAutenticado(estudanteId, estudanteAutenticadoId);
        estudanteRepository.findById(estudanteId)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.ESTUDANTE_NAO_ENCONTRADO,
                        "Estudante não encontrado"));

        List<FavoritoDtos.Item> favoritos = favoritoRepository.findByIdEstudanteId(estudanteId)
                .stream()
                .map(favorito -> {
                    Vaga vaga = favorito.getVaga();
                    return new FavoritoDtos.Item(
                            vaga.getId(),
                            vaga.getTitulo(),
                            vaga.getEndereco(),
                            vaga.getValorAluguel(),
                            vaga.getDescricao());
                })
                .toList();
        return new FavoritoDtos.ListaResponse(favoritos);
    }

    private void validarEstudanteAutenticado(long estudanteId, long estudanteAutenticadoId) {
        if (estudanteId != estudanteAutenticadoId) {
            throw new BusinessException(
                    ErrorCode.ACESSO_NEGADO,
                    "A operação só pode ser realizada pelo estudante autenticado");
        }
    }

    private BusinessException favoritoDuplicado() {
        return new BusinessException(
                ErrorCode.VAGA_JA_FAVORITADA,
                "A vaga já está nos favoritos do estudante");
    }
}
