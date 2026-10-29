package br.com.finduhouse.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.finduhouse.dto.EstudanteDtos;
import br.com.finduhouse.entity.Estudante;
import br.com.finduhouse.entity.Usuario;
import br.com.finduhouse.exception.BusinessException;
import br.com.finduhouse.exception.ErrorCode;
import br.com.finduhouse.repository.EstudanteRepository;
import br.com.finduhouse.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class EstudanteServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private EstudanteRepository estudanteRepository;

    @Mock
    private PasswordService passwordService;

    private EstudanteServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new EstudanteServiceImpl(
                usuarioRepository,
                estudanteRepository,
                passwordService,
                "puc-campinas.edu.br");
    }

    @Test
    void devePersistirUsuarioEEstudanteComSenhaHash() {
        var request = new EstudanteDtos.CadastroRequest(
                "Ana Souza",
                "ANA@PUC-CAMPINAS.EDU.BR",
                "+5519999990000",
                "MinhaSenha123",
                "Engenharia de Software");

        when(usuarioRepository.existsByEmail("ana@puc-campinas.edu.br")).thenReturn(false);
        when(passwordService.hash("MinhaSenha123")).thenReturn("hash");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario usuario = invocation.getArgument(0);
            ReflectionTestUtils.setField(usuario, "id", 42L);
            return usuario;
        });
        when(estudanteRepository.save(any(Estudante.class))).thenAnswer(invocation -> {
            Estudante estudante = invocation.getArgument(0);
            ReflectionTestUtils.setField(estudante, "id", 42L);
            ReflectionTestUtils.invokeMethod(estudante, "aoCriar");
            return estudante;
        });

        var response = service.cadastrar(request);

        assertEquals(42L, response.estudanteId());
        assertEquals("ana@puc-campinas.edu.br", response.email());
        assertEquals("Ana Souza", response.nome());

        var usuarioCaptor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(usuarioCaptor.capture());
        assertEquals(Usuario.Perfil.ESTUDANTE,
                ReflectionTestUtils.getField(usuarioCaptor.getValue(), "perfil"));
        assertEquals("hash", ReflectionTestUtils.getField(usuarioCaptor.getValue(), "senhaHash"));
        verify(estudanteRepository).save(any(Estudante.class));
    }

    @Test
    void deveRejeitarEmailDuplicado() {
        var request = new EstudanteDtos.CadastroRequest(
                "Ana Souza",
                "ana@puc-campinas.edu.br",
                "+5519999990000",
                "MinhaSenha123",
                "Engenharia de Software");
        when(usuarioRepository.existsByEmail(request.email())).thenReturn(true);

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> service.cadastrar(request));

        assertEquals(ErrorCode.EMAIL_JA_EXISTE, exception.getCodigo());
        verify(usuarioRepository, never()).save(any(Usuario.class));
        verify(estudanteRepository, never()).save(any(Estudante.class));
    }

    @Test
    void deveRejeitarEmailForaDoDominioInstitucional() {
        var request = new EstudanteDtos.CadastroRequest(
                "Ana Souza",
                "ana@email.com",
                "+5519999990000",
                "MinhaSenha123",
                "Engenharia de Software");

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> service.cadastrar(request));

        assertEquals(ErrorCode.EMAIL_INSTITUCIONAL_INVALIDO, exception.getCodigo());
        verify(usuarioRepository, never()).existsByEmail(any(String.class));
    }
}
