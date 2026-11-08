package br.com.finduhouse.service;

import br.com.finduhouse.dto.QuestionarioDtos;
import br.com.finduhouse.dto.VagaDtos;
import br.com.finduhouse.entity.Estudante;
import br.com.finduhouse.entity.QuestionarioEstudante;
import br.com.finduhouse.exception.BusinessException;
import br.com.finduhouse.exception.ErrorCode;
import br.com.finduhouse.repository.EstudanteRepository;
import br.com.finduhouse.repository.QuestionarioEstudanteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class QuestionarioServiceImpl implements QuestionarioService {

    private final EstudanteRepository estudanteRepository;
    private final QuestionarioEstudanteRepository questionarioRepository;

    public QuestionarioServiceImpl(
            EstudanteRepository estudanteRepository,
            QuestionarioEstudanteRepository questionarioRepository) {
        this.estudanteRepository = estudanteRepository;
        this.questionarioRepository = questionarioRepository;
    }

    @Override
    @Transactional
    public QuestionarioDtos.SalvarResponse salvar(
            QuestionarioDtos.SalvarRequest request,
            long estudanteAutenticadoId,
            String perfilAutenticado) {
        validarAcesso(request.estudanteId(), estudanteAutenticadoId, perfilAutenticado);

        Estudante estudante = estudanteRepository.findById(request.estudanteId())
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.ESTUDANTE_NAO_ENCONTRADO,
                        "Estudante não encontrado"));

        QuestionarioEstudante.Dados dados = dados(request.respostas());
        QuestionarioEstudante questionario = questionarioRepository
                .findByEstudanteId(request.estudanteId())
                .map(existente -> {
                    existente.atualizar(dados);
                    return existente;
                })
                .orElseGet(() -> new QuestionarioEstudante(estudante, dados));

        QuestionarioEstudante salvo = questionarioRepository.save(questionario);
        return new QuestionarioDtos.SalvarResponse(true, salvo.getAtualizadoEm());
    }

    @Override
    @Transactional(readOnly = true)
    public QuestionarioDtos.Respostas obter(
            long estudanteId,
            long usuarioAutenticadoId,
            String perfilAutenticado) {
        validarAcesso(estudanteId, usuarioAutenticadoId, perfilAutenticado);

        estudanteRepository.findById(estudanteId)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.ESTUDANTE_NAO_ENCONTRADO,
                        "Estudante não encontrado"));
        QuestionarioEstudante questionario = questionarioRepository
                .findByEstudanteId(estudanteId)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.QUESTIONARIO_NAO_PREENCHIDO,
                        "O estudante ainda não respondeu o questionário"));

        VagaDtos.Regras regras = RegrasConverter.respostas(questionario);
        return new QuestionarioDtos.Respostas(
                regras.aceitaBarulhoDepoisDas22(),
                regras.fuma(),
                regras.aceitaAnimais(),
                regras.gostaReceberVisitas(),
                regras.gostaCozinhar(),
                regras.estudaEmCasa(),
                regras.aceitaDividirTarefas(),
                regras.aceitaDividirQuarto(),
                regras.aceitaFestas(),
                regras.comunicativo());
    }

    private void validarAcesso(long estudanteId, long autenticadoId, String perfil) {
        if (!"estudante".equalsIgnoreCase(perfil) || estudanteId != autenticadoId) {
            throw new BusinessException(
                    ErrorCode.ACESSO_NEGADO,
                    "A operação só pode ser realizada pelo estudante autenticado");
        }
    }

    private QuestionarioEstudante.Dados dados(QuestionarioDtos.Respostas respostas) {
        if (respostas == null
                || respostas.aceitaBarulhoDepoisDas22() == null
                || respostas.fuma() == null
                || respostas.aceitaAnimais() == null
                || respostas.gostaReceberVisitas() == null
                || respostas.gostaCozinhar() == null
                || respostas.estudaEmCasa() == null
                || respostas.aceitaDividirTarefas() == null
                || respostas.aceitaDividirQuarto() == null
                || respostas.aceitaFestas() == null
                || respostas.comunicativo() == null) {
            throw new BusinessException(
                    ErrorCode.QUESTIONARIO_INCOMPLETO,
                    "Todas as respostas do questionário são obrigatórias");
        }

        return new QuestionarioEstudante.Dados(
                respostas.aceitaBarulhoDepoisDas22(),
                respostas.fuma(),
                respostas.aceitaAnimais(),
                respostas.gostaReceberVisitas(),
                respostas.gostaCozinhar(),
                respostas.estudaEmCasa(),
                respostas.aceitaDividirTarefas(),
                respostas.aceitaDividirQuarto(),
                respostas.aceitaFestas(),
                respostas.comunicativo());
    }
}
