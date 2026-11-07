package br.com.finduhouse.service;

import br.com.finduhouse.dto.DenunciaDtos;
import br.com.finduhouse.entity.Denuncia;
import br.com.finduhouse.entity.Estudante;
import br.com.finduhouse.entity.Vaga;
import br.com.finduhouse.exception.BusinessException;
import br.com.finduhouse.exception.ErrorCode;
import br.com.finduhouse.repository.DenunciaRepository;
import br.com.finduhouse.repository.EstudanteRepository;
import br.com.finduhouse.repository.VagaRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DenunciaServiceImpl implements DenunciaService {

    private final EstudanteRepository estudanteRepository;
    private final VagaRepository vagaRepository;
    private final DenunciaRepository denunciaRepository;

    public DenunciaServiceImpl(
            EstudanteRepository estudanteRepository,
            VagaRepository vagaRepository,
            DenunciaRepository denunciaRepository) {
        this.estudanteRepository = estudanteRepository;
        this.vagaRepository = vagaRepository;
        this.denunciaRepository = denunciaRepository;
    }

    @Override
    @Transactional
    public DenunciaDtos.CriacaoResponse denunciar(
            long vagaId,
            DenunciaDtos.CriacaoRequest request,
            long estudanteAutenticadoId) {
        validarEstudanteAutenticado(request.estudanteId(), estudanteAutenticadoId);

        Estudante estudante = estudanteRepository.findById(request.estudanteId())
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.ESTUDANTE_NAO_ENCONTRADO,
                        "Estudante não encontrado"));
        Vaga vaga = vagaRepository.findById(vagaId)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.VAGA_NAO_ENCONTRADA,
                        "Vaga não encontrada"));

        if (denunciaRepository.existsByVagaIdAndEstudanteId(vagaId, request.estudanteId())) {
            throw denunciaDuplicada();
        }

        Denuncia denuncia = new Denuncia(
                vaga,
                estudante,
                request.motivo(),
                request.descricao());
        try {
            denuncia = denunciaRepository.save(denuncia);
        } catch (DataIntegrityViolationException exception) {
            throw denunciaDuplicada();
        }

        vaga.marcarEmAnalise();
        vagaRepository.save(vaga);

        return new DenunciaDtos.CriacaoResponse(
                true,
                denuncia.getId(),
                "Denúncia registrada. A vaga está em análise.");
    }

    private void validarEstudanteAutenticado(
            long estudanteId,
            long estudanteAutenticadoId) {
        if (estudanteId != estudanteAutenticadoId) {
            throw new BusinessException(
                    ErrorCode.ACESSO_NEGADO,
                    "A operação só pode ser realizada pelo estudante autenticado");
        }
    }

    private BusinessException denunciaDuplicada() {
        return new BusinessException(
                ErrorCode.DENUNCIA_DUPLICADA,
                "A denúncia já foi registrada para esta vaga pelo estudante");
    }
}
