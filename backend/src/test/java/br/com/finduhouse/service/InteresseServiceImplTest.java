package br.com.finduhouse.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import br.com.finduhouse.entity.Estudante;
import br.com.finduhouse.entity.Interesse;
import br.com.finduhouse.entity.Usuario;
import br.com.finduhouse.entity.Vaga;
import br.com.finduhouse.exception.BusinessException;
import br.com.finduhouse.exception.ErrorCode;
import br.com.finduhouse.repository.EstudanteRepository;
import br.com.finduhouse.repository.InteresseRepository;
import br.com.finduhouse.repository.VagaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class InteresseServiceImplTest {

    @Mock
    private EstudanteRepository estudanteRepository;

    @Mock
    private VagaRepository vagaRepository;

    @Mock
    private InteresseRepository interesseRepository;

    private InteresseServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new InteresseServiceImpl(
                estudanteRepository,
                vagaRepository,
                interesseRepository);
    }

    @Test
    void deveRegistrarInteresse() {
        when(estudanteRepository.findById(42L)).thenReturn(Optional.of(estudante(42L)));
        when(vagaRepository.findById(15L)).thenReturn(Optional.of(vaga(15L, Vaga.Status.ATIVA)));
        when(interesseRepository.existsByEstudanteIdAndVagaId(42L, 15L)).thenReturn(false);

        var response = service.solicitar(15L, 42L, 42L);

        assertTrue(response.sucesso());
        assertFalse(response.emailEnviado());
        assertEquals("Interesse registrado.", response.mensagem());
        verify(interesseRepository).save(any(Interesse.class));
    }

    @Test
    void deveImpedirSolicitacaoEmNomeDeOutroEstudante() {
        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> service.solicitar(15L, 42L, 7L));

        assertEquals(ErrorCode.ACESSO_NEGADO, exception.getCodigo());
        verify(estudanteRepository, never()).findById(any(Long.class));
        verify(interesseRepository, never()).save(any(Interesse.class));
    }

    @Test
    void deveRejeitarEstudanteInexistente() {
        when(estudanteRepository.findById(42L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> service.solicitar(15L, 42L, 42L));

        assertEquals(ErrorCode.ESTUDANTE_NAO_ENCONTRADO, exception.getCodigo());
        verify(interesseRepository, never()).save(any(Interesse.class));
    }

    @Test
    void deveRejeitarVagaInexistente() {
        when(estudanteRepository.findById(42L)).thenReturn(Optional.of(estudante(42L)));
        when(vagaRepository.findById(15L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> service.solicitar(15L, 42L, 42L));

        assertEquals(ErrorCode.VAGA_NAO_ENCONTRADA, exception.getCodigo());
        verify(interesseRepository, never()).save(any(Interesse.class));
    }

    @Test
    void deveRejeitarVagaInativa() {
        when(estudanteRepository.findById(42L)).thenReturn(Optional.of(estudante(42L)));
        when(vagaRepository.findById(15L)).thenReturn(Optional.of(vaga(15L, Vaga.Status.INATIVA)));

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> service.solicitar(15L, 42L, 42L));

        assertEquals(ErrorCode.VAGA_NAO_ENCONTRADA, exception.getCodigo());
        verify(interesseRepository, never()).save(any(Interesse.class));
    }

    @Test
    void deveRejeitarInteresseDuplicado() {
        when(estudanteRepository.findById(42L)).thenReturn(Optional.of(estudante(42L)));
        when(vagaRepository.findById(15L)).thenReturn(Optional.of(vaga(15L, Vaga.Status.ATIVA)));
        when(interesseRepository.existsByEstudanteIdAndVagaId(42L, 15L)).thenReturn(true);

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> service.solicitar(15L, 42L, 42L));

        assertEquals(ErrorCode.INTERESSE_JA_REGISTRADO, exception.getCodigo());
        verify(interesseRepository, never()).save(any(Interesse.class));
    }

    @Test
    void deveTratarViolacaoDeUnicidadeComoInteresseDuplicado() {
        when(estudanteRepository.findById(42L)).thenReturn(Optional.of(estudante(42L)));
        when(vagaRepository.findById(15L)).thenReturn(Optional.of(vaga(15L, Vaga.Status.ATIVA)));
        when(interesseRepository.existsByEstudanteIdAndVagaId(42L, 15L)).thenReturn(false);
        when(interesseRepository.save(any(Interesse.class)))
                .thenThrow(new DataIntegrityViolationException("duplicado"));

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> service.solicitar(15L, 42L, 42L));

        assertEquals(ErrorCode.INTERESSE_JA_REGISTRADO, exception.getCodigo());
    }

    private Estudante estudante(long id) {
        Usuario usuario = new Usuario(
                "Ana", "ana@puccampinas.edu.br", "telefone", "hash", Usuario.Perfil.ESTUDANTE);
        ReflectionTestUtils.setField(usuario, "id", id);
        Estudante estudante = new Estudante(
                usuario, "Ana", "ana@puccampinas.edu.br", "telefone", "Engenharia");
        ReflectionTestUtils.setField(estudante, "id", id);
        return estudante;
    }

    private Vaga vaga(long id, Vaga.Status status) {
        Vaga vaga = new Vaga(
                new Usuario(
                        "Maria", "maria@email.com", "telefone", "hash", Usuario.Perfil.PROPRIETARIO),
                "Kitnet", "Kitnet mobiliada", "Rua das Flores", new BigDecimal("1200.00"),
                2, "Sem festas", new BigDecimal("-22.90"), new BigDecimal("-47.05"),
                true, false, true, true, true, true, true, false, true, true);
        ReflectionTestUtils.setField(vaga, "id", id);
        ReflectionTestUtils.setField(vaga, "status", status);
        return vaga;
    }
}
