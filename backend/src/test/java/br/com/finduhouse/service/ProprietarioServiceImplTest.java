package br.com.finduhouse.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.finduhouse.dto.ProprietarioDtos;
import br.com.finduhouse.entity.Proprietario;
import br.com.finduhouse.entity.Usuario;
import br.com.finduhouse.exception.BusinessException;
import br.com.finduhouse.exception.ErrorCode;
import br.com.finduhouse.repository.ProprietarioRepository;
import br.com.finduhouse.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class ProprietarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ProprietarioRepository proprietarioRepository;

    @Mock
    private PasswordService passwordService;

    private ProprietarioServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ProprietarioServiceImpl(
                usuarioRepository,
                proprietarioRepository,
                passwordService);
    }

    @Test
    void devePersistirUsuarioEProprietarioComSenhaHash() {
        var request = new ProprietarioDtos.CadastroRequest(
                "Maria Souza",
                "MARIA@EMAIL.COM",
                "+5519999990000",
                "MinhaSenha123");

        when(usuarioRepository.existsByEmail("maria@email.com")).thenReturn(false);
        when(passwordService.hash("MinhaSenha123")).thenReturn("hash");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario usuario = invocation.getArgument(0);
            ReflectionTestUtils.setField(usuario, "id", 42L);
            return usuario;
        });
        when(proprietarioRepository.save(any(Proprietario.class))).thenAnswer(invocation -> {
            Proprietario proprietario = invocation.getArgument(0);
            ReflectionTestUtils.setField(proprietario, "id", 42L);
            ReflectionTestUtils.invokeMethod(proprietario, "aoCriar");
            return proprietario;
        });

        var response = service.cadastrar(request);

        assertEquals(42L, response.proprietarioId());
        assertEquals("maria@email.com", response.email());
        assertEquals("Maria Souza", response.nome());

        var usuarioCaptor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(usuarioCaptor.capture());
        assertEquals(Usuario.Perfil.PROPRIETARIO,
                ReflectionTestUtils.getField(usuarioCaptor.getValue(), "perfil"));
        assertEquals("hash", ReflectionTestUtils.getField(usuarioCaptor.getValue(), "senhaHash"));
        verify(proprietarioRepository).save(any(Proprietario.class));
    }

    @Test
    void deveRejeitarEmailDuplicado() {
        var request = new ProprietarioDtos.CadastroRequest(
                "Maria Souza",
                "maria@email.com",
                "+5519999990000",
                "MinhaSenha123");
        when(usuarioRepository.existsByEmail(request.email())).thenReturn(true);

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> service.cadastrar(request));

        assertEquals(ErrorCode.EMAIL_JA_EXISTE, exception.getCodigo());
        verify(usuarioRepository, never()).save(any(Usuario.class));
        verify(proprietarioRepository, never()).save(any(Proprietario.class));
    }
}
