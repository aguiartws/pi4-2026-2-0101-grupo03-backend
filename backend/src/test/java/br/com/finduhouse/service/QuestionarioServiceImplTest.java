package br.com.finduhouse.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import br.com.finduhouse.dto.QuestionarioDtos;
import br.com.finduhouse.entity.Estudante;
import br.com.finduhouse.entity.QuestionarioEstudante;
import br.com.finduhouse.entity.Usuario;
import br.com.finduhouse.exception.BusinessException;
import br.com.finduhouse.exception.ErrorCode;
import br.com.finduhouse.repository.EstudanteRepository;
import br.com.finduhouse.repository.QuestionarioEstudanteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class QuestionarioServiceImplTest {

    @Mock
    private EstudanteRepository estudanteRepository;

    @Mock
    private QuestionarioEstudanteRepository questionarioRepository;

    private QuestionarioServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new QuestionarioServiceImpl(
                estudanteRepository,
                questionarioRepository);
    }

    @Test
    void deveSalvarQuestionarioParaEstudanteAutenticado() {
        Estudante estudante = estudante(42L);
        when(estudanteRepository.findById(42L)).thenReturn(Optional.of(estudante));
        when(questionarioRepository.findByEstudanteId(42L)).thenReturn(Optional.empty());
        when(questionarioRepository.save(any(QuestionarioEstudante.class)))
                .thenAnswer(invocation -> {
                    QuestionarioEstudante questionario = invocation.getArgument(0);
                    ReflectionTestUtils.invokeMethod(questionario, "atualizarTimestamp");
                    return questionario;
                });

        var response = service.salvar(request(), 42L, "estudante");

        assertTrue(response.sucesso());
        assertTrue(response.atualizadoEm() != null);
        verify(questionarioRepository).save(any(QuestionarioEstudante.class));
    }

    @Test
    void deveAtualizarQuestionarioExistente() {
        Estudante estudante = estudante(42L);
        QuestionarioEstudante existente = new QuestionarioEstudante(
                estudante,
                dados());
        when(estudanteRepository.findById(42L)).thenReturn(Optional.of(estudante));
        when(questionarioRepository.findByEstudanteId(42L))
                .thenReturn(Optional.of(existente));
        when(questionarioRepository.save(existente)).thenAnswer(invocation -> {
            ReflectionTestUtils.invokeMethod(existente, "atualizarTimestamp");
            return existente;
        });

        var response = service.salvar(request(), 42L, "estudante");

        assertTrue(response.sucesso());
        verify(questionarioRepository).save(existente);
    }

    @Test
    void deveRejeitarOperacaoDeProprietario() {
        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> service.salvar(request(), 42L, "proprietario"));

        assertEquals(ErrorCode.ACESSO_NEGADO, exception.getCodigo());
        verify(estudanteRepository, never()).findById(any(Long.class));
    }

    @Test
    void deveRejeitarOperacaoEmNomeDeOutroEstudante() {
        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> service.salvar(request(), 7L, "estudante"));

        assertEquals(ErrorCode.ACESSO_NEGADO, exception.getCodigo());
        verify(estudanteRepository, never()).findById(any(Long.class));
    }

    @Test
    void deveRejeitarEstudanteInexistente() {
        when(estudanteRepository.findById(42L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> service.salvar(request(), 42L, "estudante"));

        assertEquals(ErrorCode.ESTUDANTE_NAO_ENCONTRADO, exception.getCodigo());
        verify(questionarioRepository, never()).save(any(QuestionarioEstudante.class));
    }

    @Test
    void deveRejeitarQuestionarioIncompleto() {
        Estudante estudante = estudante(42L);
        when(estudanteRepository.findById(42L)).thenReturn(Optional.of(estudante));

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> service.salvar(
                        new QuestionarioDtos.SalvarRequest(42L, null),
                        42L,
                        "estudante"));

        assertEquals(ErrorCode.QUESTIONARIO_INCOMPLETO, exception.getCodigo());
        verify(questionarioRepository, never()).save(any(QuestionarioEstudante.class));
    }

    @Test
    void deveObterQuestionarioConvertendoRespostas() {
        Estudante estudante = estudante(42L);
        QuestionarioEstudante questionario = new QuestionarioEstudante(estudante, dados());
        when(estudanteRepository.findById(42L)).thenReturn(Optional.of(estudante));
        when(questionarioRepository.findByEstudanteId(42L)).thenReturn(Optional.of(questionario));

        QuestionarioDtos.Respostas response = service.obter(42L, 42L, "estudante");

        assertEquals(true, response.aceitaBarulhoDepoisDas22());
        assertEquals(false, response.fuma());
        assertEquals(true, response.aceitaAnimais());
        assertEquals(false, response.comunicativo());
    }

    @Test
    void deveRejeitarQuestionarioAusenteAoObter() {
        when(estudanteRepository.findById(42L)).thenReturn(Optional.of(estudante(42L)));
        when(questionarioRepository.findByEstudanteId(42L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> service.obter(42L, 42L, "estudante"));

        assertEquals(ErrorCode.QUESTIONARIO_NAO_PREENCHIDO, exception.getCodigo());
    }

    private QuestionarioDtos.SalvarRequest request() {
        return new QuestionarioDtos.SalvarRequest(42L, respostas());
    }

    private QuestionarioDtos.Respostas respostas() {
        return new QuestionarioDtos.Respostas(
                true, false, true, true, true,
                true, true, false, true, false);
    }

    private QuestionarioEstudante.Dados dados() {
        return new QuestionarioEstudante.Dados(
                true, false, true, true, true,
                true, true, false, true, false);
    }

    private Estudante estudante(long id) {
        Usuario usuario = new Usuario(
                "Ana",
                "ana@puccampinas.edu.br",
                "telefone",
                "hash",
                Usuario.Perfil.ESTUDANTE);
        ReflectionTestUtils.setField(usuario, "id", id);
        Estudante estudante = new Estudante(
                usuario,
                "Ana",
                "ana@puccampinas.edu.br",
                "telefone",
                "Engenharia");
        ReflectionTestUtils.setField(estudante, "id", id);
        return estudante;
    }
}
