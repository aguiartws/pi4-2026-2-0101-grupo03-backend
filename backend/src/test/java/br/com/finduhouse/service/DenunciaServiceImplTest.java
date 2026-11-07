package br.com.finduhouse.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import br.com.finduhouse.dto.DenunciaDtos;
import br.com.finduhouse.entity.Denuncia;
import br.com.finduhouse.entity.Estudante;
import br.com.finduhouse.entity.Usuario;
import br.com.finduhouse.entity.Vaga;
import br.com.finduhouse.exception.BusinessException;
import br.com.finduhouse.exception.ErrorCode;
import br.com.finduhouse.repository.DenunciaRepository;
import br.com.finduhouse.repository.EstudanteRepository;
import br.com.finduhouse.repository.VagaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class DenunciaServiceImplTest {

    @Mock
    private EstudanteRepository estudanteRepository;

    @Mock
    private VagaRepository vagaRepository;

    @Mock
    private DenunciaRepository denunciaRepository;

    private DenunciaServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new DenunciaServiceImpl(
                estudanteRepository,
                vagaRepository,
                denunciaRepository);
    }

    @Test
    void deveRegistrarDenunciaEColocarVagaEmAnalise() {
        Estudante estudante = estudante(42L);
        Vaga vaga = vaga(15L);
        when(estudanteRepository.findById(42L)).thenReturn(Optional.of(estudante));
        when(vagaRepository.findById(15L)).thenReturn(Optional.of(vaga));
        when(denunciaRepository.existsByVagaIdAndEstudanteId(15L, 42L))
                .thenReturn(false);
        when(denunciaRepository.save(any(Denuncia.class))).thenAnswer(invocation -> {
            Denuncia denuncia = invocation.getArgument(0);
            ReflectionTestUtils.setField(denuncia, "id", 7L);
            return denuncia;
        });

        DenunciaDtos.CriacaoResponse response = service.denunciar(
                15L,
                request(15L, 42L),
                42L);

        assertEquals(true, response.sucesso());
        assertEquals(7L, response.denunciaId());
        assertEquals("Denúncia registrada. A vaga está em análise.", response.mensagem());
        assertEquals(Vaga.Status.EM_ANALISE, vaga.getStatus());
        verify(denunciaRepository).save(any(Denuncia.class));
        verify(vagaRepository).save(vaga);
    }

    @Test
    void deveRejeitarDenunciaDuplicada() {
        when(estudanteRepository.findById(42L)).thenReturn(Optional.of(estudante(42L)));
        when(vagaRepository.findById(15L)).thenReturn(Optional.of(vaga(15L)));
        when(denunciaRepository.existsByVagaIdAndEstudanteId(15L, 42L))
                .thenReturn(true);

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> service.denunciar(15L, request(15L, 42L), 42L));

        assertEquals(ErrorCode.DENUNCIA_DUPLICADA, exception.getCodigo());
        verify(denunciaRepository, never()).save(any(Denuncia.class));
    }

    @Test
    void deveImpedirDenunciaEmNomeDeOutroEstudante() {
        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> service.denunciar(15L, request(15L, 42L), 7L));

        assertEquals(ErrorCode.ACESSO_NEGADO, exception.getCodigo());
        verify(estudanteRepository, never()).findById(any(Long.class));
        verify(denunciaRepository, never()).save(any(Denuncia.class));
    }

    @Test
    void deveRejeitarDenunciaQuandoVagaNaoExiste() {
        when(estudanteRepository.findById(42L)).thenReturn(Optional.of(estudante(42L)));
        when(vagaRepository.findById(15L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> service.denunciar(15L, request(15L, 42L), 42L));

        assertEquals(ErrorCode.VAGA_NAO_ENCONTRADA, exception.getCodigo());
        verify(denunciaRepository, never()).save(any(Denuncia.class));
    }

    @Test
    void deveRejeitarDenunciaQuandoEstudanteNaoExiste() {
        when(estudanteRepository.findById(42L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> service.denunciar(15L, request(15L, 42L), 42L));

        assertEquals(ErrorCode.ESTUDANTE_NAO_ENCONTRADO, exception.getCodigo());
        verify(vagaRepository, never()).findById(any(Long.class));
        verify(denunciaRepository, never()).save(any(Denuncia.class));
    }

    @Test
    void deveManterVagaInativaAoRegistrarDenuncia() {
        Estudante estudante = estudante(42L);
        Vaga vaga = vaga(15L);
        ReflectionTestUtils.setField(vaga, "status", Vaga.Status.INATIVA);
        when(estudanteRepository.findById(42L)).thenReturn(Optional.of(estudante));
        when(vagaRepository.findById(15L)).thenReturn(Optional.of(vaga));
        when(denunciaRepository.existsByVagaIdAndEstudanteId(15L, 42L))
                .thenReturn(false);
        when(denunciaRepository.save(any(Denuncia.class))).thenAnswer(invocation -> {
            Denuncia denuncia = invocation.getArgument(0);
            ReflectionTestUtils.setField(denuncia, "id", 8L);
            return denuncia;
        });

        service.denunciar(15L, request(15L, 42L), 42L);

        assertEquals(Vaga.Status.INATIVA, vaga.getStatus());
        verify(vagaRepository).save(vaga);
    }

    private DenunciaDtos.CriacaoRequest request(long vagaId, long estudanteId) {
        return new DenunciaDtos.CriacaoRequest(
                vagaId,
                estudanteId,
                DenunciaDtos.Motivo.GOLPE_FINANCEIRO,
                "Descrição da denúncia");
    }

    private Estudante estudante(long id) {
        Usuario usuario = new Usuario(
                "Ana",
                "ana@puc-campinas.edu.br",
                "telefone",
                "hash",
                Usuario.Perfil.ESTUDANTE);
        ReflectionTestUtils.setField(usuario, "id", id);
        Estudante estudante = new Estudante(
                usuario,
                "Ana",
                "ana@puc-campinas.edu.br",
                "telefone",
                "Engenharia");
        ReflectionTestUtils.setField(estudante, "id", id);
        return estudante;
    }

    private Vaga vaga(long id) {
        Vaga vaga = new Vaga(
                new Usuario(
                        "Maria",
                        "maria@email.com",
                        "telefone",
                        "hash",
                        Usuario.Perfil.PROPRIETARIO),
                "Kitnet",
                "Kitnet mobiliada",
                "Rua das Flores",
                new BigDecimal("1200.00"),
                2,
                "Sem festas",
                new BigDecimal("-22.90"),
                new BigDecimal("-47.05"),
                "entre_22_e_0",
                "depois_da_0",
                "entre_6_e_8",
                "depois_das_8",
                "alto",
                "medio",
                "media",
                "as_vezes",
                "as_vezes",
                false);
        ReflectionTestUtils.setField(vaga, "id", id);
        return vaga;
    }
}
