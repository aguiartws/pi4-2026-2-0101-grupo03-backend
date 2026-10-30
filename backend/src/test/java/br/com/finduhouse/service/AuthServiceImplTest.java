package br.com.finduhouse.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import br.com.finduhouse.dto.AuthDtos;
import br.com.finduhouse.entity.Usuario;
import br.com.finduhouse.exception.BusinessException;
import br.com.finduhouse.exception.ErrorCode;
import br.com.finduhouse.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordService passwordService;

    @Mock
    private TokenService tokenService;

    private AuthServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new AuthServiceImpl(
                usuarioRepository,
                passwordService,
                tokenService,
                "puc-campinas.edu.br");
    }

    @Test
    void deveAutenticarEstudanteEEmitirToken() {
        Usuario usuario = usuario("ana@puc-campinas.edu.br", Usuario.Perfil.ESTUDANTE);
        var request = new AuthDtos.LoginRequest(
                "ANA@PUC-CAMPINAS.EDU.BR",
                "MinhaSenha123");

        when(usuarioRepository.findByEmail(request.email().toLowerCase()))
                .thenReturn(Optional.of(usuario));
        when(passwordService.matches(request.senha(), "hash")).thenReturn(true);
        when(tokenService.gerar(42L, "estudante")).thenReturn("token");

        var response = service.autenticar(request);

        assertEquals("token", response.token());
        assertEquals(42L, response.usuarioId());
        assertEquals(AuthDtos.Perfil.ESTUDANTE, response.perfil());
        verify(tokenService).gerar(42L, "estudante");
    }

    @Test
    void devePermitirProprietarioComEmailNaoInstitucional() {
        Usuario usuario = usuario("maria@email.com", Usuario.Perfil.PROPRIETARIO);
        when(usuarioRepository.findByEmail("maria@email.com")).thenReturn(Optional.of(usuario));
        when(passwordService.matches("senha", "hash")).thenReturn(true);
        when(tokenService.gerar(42L, "proprietario")).thenReturn("token");

        var response = service.autenticar(
                new AuthDtos.LoginRequest("maria@email.com", "senha"));

        assertEquals(AuthDtos.Perfil.PROPRIETARIO, response.perfil());
    }

    @Test
    void deveRejeitarSenhaIncorreta() {
        Usuario usuario = usuario("ana@puc-campinas.edu.br", Usuario.Perfil.ESTUDANTE);
        when(usuarioRepository.findByEmail("ana@puc-campinas.edu.br"))
                .thenReturn(Optional.of(usuario));
        when(passwordService.matches("errada", "hash")).thenReturn(false);

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> service.autenticar(new AuthDtos.LoginRequest(
                        "ana@puc-campinas.edu.br",
                        "errada")));

        assertEquals(ErrorCode.CREDENCIAIS_INVALIDAS, exception.getCodigo());
    }

    private Usuario usuario(String email, Usuario.Perfil perfil) {
        Usuario usuario = new Usuario("Nome", email, "telefone", "hash", perfil);
        ReflectionTestUtils.setField(usuario, "id", 42L);
        return usuario;
    }
}
