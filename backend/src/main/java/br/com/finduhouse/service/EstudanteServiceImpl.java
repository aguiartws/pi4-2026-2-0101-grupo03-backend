package br.com.finduhouse.service;

import br.com.finduhouse.dto.EstudanteDtos;
import br.com.finduhouse.entity.Estudante;
import br.com.finduhouse.entity.Usuario;
import br.com.finduhouse.exception.BusinessException;
import br.com.finduhouse.exception.ErrorCode;
import br.com.finduhouse.repository.EstudanteRepository;
import br.com.finduhouse.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EstudanteServiceImpl implements EstudanteService {

    private final UsuarioRepository usuarioRepository;
    private final EstudanteRepository estudanteRepository;
    private final PasswordService passwordService;
    private final String institutionalDomain;

    public EstudanteServiceImpl(
            UsuarioRepository usuarioRepository,
            EstudanteRepository estudanteRepository,
            PasswordService passwordService,
            @Value("${finduhouse.email.institutional-domain:puc-campinas.edu.br}")
            String institutionalDomain) {
        this.usuarioRepository = usuarioRepository;
        this.estudanteRepository = estudanteRepository;
        this.passwordService = passwordService;
        this.institutionalDomain = institutionalDomain.toLowerCase();
    }

    @Override
    @Transactional
    public EstudanteDtos.CadastroResponse cadastrar(EstudanteDtos.CadastroRequest request) {
        String email = request.email().trim().toLowerCase();
        if (!email.endsWith("@" + institutionalDomain)
                || email.length() <= institutionalDomain.length() + 1) {
            throw new BusinessException(
                    ErrorCode.EMAIL_INSTITUCIONAL_INVALIDO,
                    "O e-mail deve pertencer ao domínio institucional permitido");
        }

        if (usuarioRepository.existsByEmail(email)) {
            throw new BusinessException(
                    ErrorCode.EMAIL_JA_EXISTE,
                    "Já existe um usuário cadastrado com este e-mail");
        }

        Usuario usuario = new Usuario(
                request.nome(),
                email,
                request.telefone(),
                passwordService.hash(request.senha()),
                Usuario.Perfil.ESTUDANTE);

        try {
            Usuario usuarioSalvo = usuarioRepository.save(usuario);
            Estudante estudante = estudanteRepository.save(new Estudante(
                    usuarioSalvo,
                    request.nome(),
                    email,
                    request.telefone(),
                    request.curso()));

            return new EstudanteDtos.CadastroResponse(
                    estudante.getId(),
                    estudante.getNome(),
                    estudante.getEmail(),
                    estudante.getTelefone(),
                    estudante.getCurso(),
                    estudante.getCriadoEm());
        } catch (DataIntegrityViolationException exception) {
            throw new BusinessException(
                    ErrorCode.EMAIL_JA_EXISTE,
                    "Já existe um usuário cadastrado com este e-mail");
        }
    }
}
