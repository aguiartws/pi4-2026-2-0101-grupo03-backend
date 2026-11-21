package br.com.finduhouse.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import br.com.finduhouse.dto.FavoritoDtos;
import br.com.finduhouse.entity.Estudante;
import br.com.finduhouse.entity.Favorito;
import br.com.finduhouse.entity.Usuario;
import br.com.finduhouse.entity.Vaga;
import br.com.finduhouse.exception.BusinessException;
import br.com.finduhouse.exception.ErrorCode;
import br.com.finduhouse.repository.EstudanteRepository;
import br.com.finduhouse.repository.FavoritoRepository;
import br.com.finduhouse.repository.VagaRepository;

@ExtendWith(MockitoExtension.class)
class FavoritoServiceImplTest {

    @Mock
    private EstudanteRepository estudanteRepository;

    @Mock
    private VagaRepository vagaRepository;

    @Mock
    private FavoritoRepository favoritoRepository;

    @Mock
    private CalculadoraDistancia calculadoraDistancia;

    private FavoritoServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new FavoritoServiceImpl(
                estudanteRepository,
                vagaRepository,
                favoritoRepository,
                calculadoraDistancia);
    }

    @Test
    void deveFavoritarVagaParaEstudanteAutenticado() {
        Estudante estudante = estudante(42L);
        Vaga vaga = vaga(15L);
        var request = new FavoritoDtos.CriacaoRequest(42L, 15L);

        when(estudanteRepository.findById(42L)).thenReturn(Optional.of(estudante));
        when(vagaRepository.findById(15L)).thenReturn(Optional.of(vaga));
        when(favoritoRepository.existsByIdEstudanteIdAndIdVagaId(42L, 15L))
                .thenReturn(false);

        var response = service.favoritar(request, 42L);

        assertEquals("Vaga favoritada com sucesso", response.mensagem());
        assertEquals(42L, response.estudanteId());
        assertEquals(15L, response.vagaId());
        verify(favoritoRepository).save(any(Favorito.class));
    }

    @Test
    void deveRejeitarFavoritoDuplicado() {
        when(estudanteRepository.findById(42L)).thenReturn(Optional.of(estudante(42L)));
        when(vagaRepository.findById(15L)).thenReturn(Optional.of(vaga(15L)));
        when(favoritoRepository.existsByIdEstudanteIdAndIdVagaId(42L, 15L))
                .thenReturn(true);

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> service.favoritar(
                        new FavoritoDtos.CriacaoRequest(42L, 15L),
                        42L));

        assertEquals(ErrorCode.VAGA_JA_FAVORITADA, exception.getCodigo());
        verify(favoritoRepository, never()).save(any(Favorito.class));
    }

    @Test
    void deveImpedirFavoritarEmNomeDeOutroEstudante() {
        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> service.favoritar(
                        new FavoritoDtos.CriacaoRequest(42L, 15L),
                        7L));

        assertEquals(ErrorCode.ACESSO_NEGADO, exception.getCodigo());
        verify(estudanteRepository, never()).findById(any(Long.class));
        verify(favoritoRepository, never()).save(any(Favorito.class));
    }

    @Test
    void deveListarFavoritosDoEstudanteAutenticado() {
        Vaga vaga = vaga(15L);
        when(estudanteRepository.findById(42L)).thenReturn(Optional.of(estudante(42L)));
        when(favoritoRepository.findByIdEstudanteId(42L))
                .thenReturn(List.of(new Favorito(estudante(42L), vaga)));
        when(calculadoraDistancia.calcular(vaga.getLatitude(), vaga.getLongitude()))
                .thenReturn(new BigDecimal("3.45"));

        var response = service.listar(42L, 42L);

        assertEquals(1, response.favoritos().size());
        assertEquals(15L, response.favoritos().get(0).id());
        assertEquals("Kitnet", response.favoritos().get(0).titulo());
        assertEquals(new BigDecimal("1200.00"), response.favoritos().get(0).valorAluguel());
        assertEquals(new BigDecimal("3.45"), response.favoritos().get(0).distanciaCampus());
        assertEquals(0, new BigDecimal("-22.90").compareTo(response.favoritos().get(0).latitude()));
        assertEquals(0, new BigDecimal("-47.05").compareTo(response.favoritos().get(0).longitude()));
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

    private Vaga vaga(long id) {
        Vaga vaga = new Vaga(
                new Usuario(
                        "Maria",
                        "maria@email.com",
                        "telefone",
                        "hash",
                        Usuario.Perfil.PROPRIETARIO
                ),
                "Kitnet",
                "Kitnet mobiliada",
                "Rua das Flores",
                new BigDecimal("1200.00"),
                2,
                "Sem festas",
                new BigDecimal("-22.90"),
                new BigDecimal("-47.05"),
                true, false, true, true, true,
                true, true, false, true, true
        );

        ReflectionTestUtils.setField(vaga, "id", id);
        return vaga;
    }
}