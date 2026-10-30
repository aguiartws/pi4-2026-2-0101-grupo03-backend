package br.com.finduhouse.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Locale;

import br.com.finduhouse.dto.VagaDtos;
import br.com.finduhouse.entity.Usuario;
import br.com.finduhouse.entity.Vaga;
import br.com.finduhouse.exception.BusinessException;
import br.com.finduhouse.exception.ErrorCode;
import br.com.finduhouse.repository.UsuarioRepository;
import br.com.finduhouse.repository.VagaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class VagaServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private VagaRepository vagaRepository;

    private VagaServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new VagaServiceImpl(usuarioRepository, vagaRepository);
    }

    @Test
    void devePersistirVagaAtivaParaProprietario() {
        Usuario proprietario = new Usuario(
                "Maria Souza",
                "maria@email.com",
                "telefone",
                "hash",
                Usuario.Perfil.PROPRIETARIO);
        ReflectionTestUtils.setField(proprietario, "id", 7L);
        when(usuarioRepository.findByIdAndPerfil(7L, Usuario.Perfil.PROPRIETARIO))
                .thenReturn(java.util.Optional.of(proprietario));
        when(vagaRepository.save(any(Vaga.class))).thenAnswer(invocation -> {
            Vaga vaga = invocation.getArgument(0);
            ReflectionTestUtils.setField(vaga, "id", 101L);
            ReflectionTestUtils.invokeMethod(vaga, "aoCriar");
            return vaga;
        });

        var response = service.cadastrar(request());

        assertEquals(101L, response.vagaId());
        assertEquals(VagaDtos.Status.ATIVA, response.status());
        verify(vagaRepository).save(any(Vaga.class));
    }

    @Test
    void deveRejeitarResponsavelQueNaoEProprietario() {
        when(usuarioRepository.findByIdAndPerfil(7L, Usuario.Perfil.PROPRIETARIO))
                .thenReturn(java.util.Optional.empty());

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> service.cadastrar(request()));

        assertEquals(ErrorCode.RESPONSAVEL_NAO_ENCONTRADO, exception.getCodigo());
        verify(vagaRepository, never()).save(any(Vaga.class));
    }

    private VagaDtos.CadastroRequest request() {
        return new VagaDtos.CadastroRequest(
                7L,
                "Quarto próximo ao campus",
                "Quarto mobiliado",
                "Rua das Acácias, 123",
                new BigDecimal("1200.00"),
                3,
                "Silêncio após 22h",
                new BigDecimal("-22.905"),
                new BigDecimal("-47.060"),
                new VagaDtos.Regras(
                        VagaDtos.HorarioDormir.ENTRE_22_E_0,
                        VagaDtos.HorarioDormir.DEPOIS_DA_0,
                        VagaDtos.HorarioAcordar.ENTRE_6_E_8,
                        VagaDtos.HorarioAcordar.DEPOIS_DAS_8,
                        VagaDtos.Nivel.ALTO,
                        VagaDtos.Nivel.MEDIO,
                        VagaDtos.ToleranciaRuido.MEDIA,
                        VagaDtos.Frequencia.AS_VEZES,
                        VagaDtos.Frequencia.AS_VEZES,
                        false));
    }
}
