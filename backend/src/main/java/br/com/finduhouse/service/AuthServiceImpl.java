package br.com.finduhouse.service;

import br.com.finduhouse.dto.AuthDtos;
import br.com.finduhouse.entity.Usuario;
import br.com.finduhouse.exception.BusinessException;
import br.com.finduhouse.exception.ErrorCode;
import br.com.finduhouse.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordService passwordService;
    private final TokenService tokenService;
    private final String institutionalDomain;

    public AuthServiceImpl(
            UsuarioRepository usuarioRepository,
            PasswordService passwordService,
            TokenService tokenService,
            @Value("${finduhouse.email.institutional-domain:puc-campinas.edu.br}")
            String institutionalDomain) {
        this.usuarioRepository = usuarioRepository;
        this.passwordService = passwordService;
        this.tokenService = tokenService;
        this.institutionalDomain = institutionalDomain.toLowerCase();
    }

    @Override
    public AuthDtos.LoginResponse autenticar(AuthDtos.LoginRequest request) {
        String email = request.email().trim().toLowerCase();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> credenciaisInvalidas());

        if (usuario.getPerfil() == Usuario.Perfil.ESTUDANTE
                && !email.endsWith("@" + institutionalDomain)) {
            throw new BusinessException(
                    ErrorCode.EMAIL_INSTITUCIONAL_INVALIDO,
                    "O e-mail do estudante deve pertencer ao domínio institucional permitido");
        }

        if (!passwordService.matches(request.senha(), usuario.getSenhaHash())) {
            throw credenciaisInvalidas();
        }

        String token = tokenService.gerar(usuario.getId(), usuario.getPerfil().name().toLowerCase());
        return new AuthDtos.LoginResponse(
                token,
                usuario.getId(),
                AuthDtos.Perfil.valueOf(usuario.getPerfil().name()));
    }

    private BusinessException credenciaisInvalidas() {
        return new BusinessException(
                ErrorCode.CREDENCIAIS_INVALIDAS,
                "E-mail ou senha incorretos");
    }
}
