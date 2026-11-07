package br.com.finduhouse.service;

import br.com.finduhouse.dto.QuestionarioDtos;
import br.com.finduhouse.entity.QuestionarioEstudante;
import br.com.finduhouse.entity.Vaga;
import br.com.finduhouse.exception.BusinessException;
import br.com.finduhouse.exception.ErrorCode;
import br.com.finduhouse.repository.EstudanteRepository;
import br.com.finduhouse.repository.QuestionarioEstudanteRepository;
import br.com.finduhouse.repository.VagaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CompatibilidadeServiceImpl implements CompatibilidadeService {

    private final VagaRepository vagaRepository;
    private final EstudanteRepository estudanteRepository;
    private final QuestionarioEstudanteRepository questionarioRepository;
    private final CalculadoraCompatibilidade calculadora;

    public CompatibilidadeServiceImpl(
            VagaRepository vagaRepository,
            EstudanteRepository estudanteRepository,
            QuestionarioEstudanteRepository questionarioRepository,
            CalculadoraCompatibilidade calculadora) {
        this.vagaRepository = vagaRepository;
        this.estudanteRepository = estudanteRepository;
        this.questionarioRepository = questionarioRepository;
        this.calculadora = calculadora;
    }

    @Override
    @Transactional(readOnly = true)
    public QuestionarioDtos.CompatibilidadeResponse obter(
            long vagaId,
            long estudanteId,
            long usuarioAutenticadoId,
            String perfilAutenticado) {
        validarAcesso(estudanteId, usuarioAutenticadoId, perfilAutenticado);

        Vaga vaga = vagaRepository.findById(vagaId)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.VAGA_NAO_ENCONTRADA,
                        "Vaga não encontrada"));
        estudanteRepository.findById(estudanteId)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.ESTUDANTE_NAO_ENCONTRADO,
                        "Estudante não encontrado"));
        QuestionarioEstudante questionario = questionarioRepository
                .findByEstudanteId(estudanteId)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.QUESTIONARIO_NAO_PREENCHIDO,
                        "O estudante ainda não respondeu o questionário"));

        CalculadoraCompatibilidade.Resultado resultado = calculadora.calcular(
                new CalculadoraCompatibilidade.Respostas(
                        questionario.getHorarioDormirSemana(),
                        questionario.getHorarioDormirFimSemana(),
                        questionario.getHorarioAcordarSemana(),
                        questionario.getHorarioAcordarFimSemana(),
                        questionario.getNivelLimpeza(),
                        questionario.getNivelOrganizacao(),
                        questionario.getToleranciaRuido(),
                        questionario.getFrequenciaVisitas(),
                        questionario.getFrequenciaFestas(),
                        questionario.getTemPet()),
                new CalculadoraCompatibilidade.Regras(
                        vaga.getHorarioDormirSemana(),
                        vaga.getHorarioDormirFimSemana(),
                        vaga.getHorarioAcordarSemana(),
                        vaga.getHorarioAcordarFimSemana(),
                        vaga.getNivelLimpeza(),
                        vaga.getNivelOrganizacao(),
                        vaga.getToleranciaRuido(),
                        vaga.getFrequenciaVisitas(),
                        vaga.getFrequenciaFestas(),
                        vaga.getTemPet()));
        return new QuestionarioDtos.CompatibilidadeResponse(
                vagaId, resultado.nota(), resultado.dimensoes());
    }

    private void validarAcesso(long estudanteId, long autenticadoId, String perfil) {
        if (!"estudante".equalsIgnoreCase(perfil) || estudanteId != autenticadoId) {
            throw new BusinessException(
                    ErrorCode.ACESSO_NEGADO,
                    "A operação só pode ser realizada pelo estudante autenticado");
        }
    }
}
