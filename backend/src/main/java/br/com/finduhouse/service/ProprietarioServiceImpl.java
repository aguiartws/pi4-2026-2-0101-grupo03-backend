package br.com.finduhouse.service;

import br.com.finduhouse.dto.ProprietarioDtos;
import br.com.finduhouse.entity.Proprietario;
import br.com.finduhouse.entity.Usuario;
import br.com.finduhouse.exception.BusinessException;
import br.com.finduhouse.exception.ErrorCode;
import br.com.finduhouse.repository.ProprietarioRepository;
import br.com.finduhouse.repository.UsuarioRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProprietarioServiceImpl implements ProprietarioService {

    private final UsuarioRepository usuarioRepository;
    private final ProprietarioRepository proprietarioRepository;
    private final PasswordService passwordService;

    public ProprietarioServiceImpl(
            UsuarioRepository usuarioRepository,
            ProprietarioRepository proprietarioRepository,
            PasswordService passwordService) {
        this.usuarioRepository = usuarioRepository;
        this.proprietarioRepository = proprietarioRepository;
        this.passwordService = passwordService;
    }

    @Override
    @Transactional
    public ProprietarioDtos.CadastroResponse cadastrar(
            ProprietarioDtos.CadastroRequest request) {
        String email = request.email().trim().toLowerCase();
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
                Usuario.Perfil.PROPRIETARIO);

        try {
            Usuario usuarioSalvo = usuarioRepository.save(usuario);
            Proprietario proprietario = proprietarioRepository.save(new Proprietario(
                    usuarioSalvo,
                    request.nome(),
                    email,
                    request.telefone()));

            return new ProprietarioDtos.CadastroResponse(
                    proprietario.getId(),
                    proprietario.getNome(),
                    proprietario.getEmail(),
                    proprietario.getTelefone(),
                    proprietario.getCriadoEm());
        } catch (DataIntegrityViolationException exception) {
            throw new BusinessException(
                    ErrorCode.EMAIL_JA_EXISTE,
                    "Já existe um usuário cadastrado com este e-mail");
        }
    }
}
