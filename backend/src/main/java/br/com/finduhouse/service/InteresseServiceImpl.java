package br.com.finduhouse.service;

import br.com.finduhouse.dto.InteresseDtos;
import br.com.finduhouse.entity.Estudante;
import br.com.finduhouse.entity.Interesse;
import br.com.finduhouse.entity.Vaga;
import br.com.finduhouse.exception.BusinessException;
import br.com.finduhouse.exception.ErrorCode;
import br.com.finduhouse.repository.EstudanteRepository;
import br.com.finduhouse.repository.InteresseRepository;
import br.com.finduhouse.repository.VagaRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InteresseServiceImpl implements InteresseService {

    private final EstudanteRepository estudanteRepository;
    private final VagaRepository vagaRepository;
    private final InteresseRepository interesseRepository;

    public InteresseServiceImpl(
            EstudanteRepository estudanteRepository,
            VagaRepository vagaRepository,
            InteresseRepository interesseRepository) {
        this.estudanteRepository = estudanteRepository;
        this.vagaRepository = vagaRepository;
        this.interesseRepository = interesseRepository;
    }

    @Override
    @Transactional
    public InteresseDtos.Response solicitar(
            long vagaId,
            long estudanteId,
            long estudanteAutenticadoId) {
        if (estudanteId != estudanteAutenticadoId) {
            throw new BusinessException(
                    ErrorCode.ACESSO_NEGADO,
                    "A operação só pode ser realizada pelo estudante autenticado");
        }

        Estudante estudante = estudanteRepository.findById(estudanteId)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.ESTUDANTE_NAO_ENCONTRADO,
                        "Estudante não encontrado"));
        Vaga vaga = vagaRepository.findById(vagaId)
                .filter(vagaEncontrada -> vagaEncontrada.getStatus() == Vaga.Status.ATIVA)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.VAGA_NAO_ENCONTRADA,
                        "Vaga não encontrada"));

        if (interesseRepository.existsByEstudanteIdAndVagaId(estudanteId, vagaId)) {
            throw interesseDuplicado();
        }

        try {
            interesseRepository.save(new Interesse(estudante, vaga));
        } catch (DataIntegrityViolationException exception) {
            throw interesseDuplicado();
        }

        // TODO (servidor de e-mail): acionar a notificação aqui
        return new InteresseDtos.Response(true, false, "Interesse registrado.");
    }

    private BusinessException interesseDuplicado() {
        return new BusinessException(
                ErrorCode.INTERESSE_JA_REGISTRADO,
                "O interesse já foi registrado para esta vaga pelo estudante");
    }
}
