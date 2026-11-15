package br.com.finduhouse.service;

import br.com.finduhouse.dto.PerfilDtos;
import br.com.finduhouse.entity.Estudante;
import br.com.finduhouse.entity.Proprietario;
import br.com.finduhouse.entity.Usuario;
import br.com.finduhouse.exception.BusinessException;
import br.com.finduhouse.exception.ErrorCode;
import br.com.finduhouse.repository.EstudanteRepository;
import br.com.finduhouse.repository.ProprietarioRepository;
import br.com.finduhouse.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PerfilServiceImpl implements PerfilService {

    private final UsuarioRepository usuarioRepository;
    private final EstudanteRepository estudanteRepository;
    private final ProprietarioRepository proprietarioRepository;
    private final PasswordService passwordService;
    private final ArmazenamentoImagens armazenamento;
    private final String institutionalDomain;

    public PerfilServiceImpl(
            UsuarioRepository usuarioRepository,
            EstudanteRepository estudanteRepository,
            ProprietarioRepository proprietarioRepository,
            PasswordService passwordService,
            ArmazenamentoImagens armazenamento,
            @Value("${finduhouse.email.institutional-domain:puccampinas.edu.br}")
            String institutionalDomain) {
        this.usuarioRepository = usuarioRepository;
        this.estudanteRepository = estudanteRepository;
        this.proprietarioRepository = proprietarioRepository;
        this.passwordService = passwordService;
        this.armazenamento = armazenamento;
        this.institutionalDomain = institutionalDomain.toLowerCase();
    }

    @Override
    @Transactional(readOnly = true)
    public PerfilDtos.PerfilResponse obter(AuthenticatedUser usuario) {
        Usuario entidade = usuarioRepository.findById(usuario.usuarioId())
                .orElseThrow(() -> usuarioNaoEncontrado(usuario));
        if (ehEstudante(usuario)) {
            Estudante estudante = estudanteRepository.findById(usuario.usuarioId())
                    .orElseThrow(() -> estudanteNaoEncontrado());
            return respostaEstudante(entidade, estudante);
        }
        Proprietario proprietario = proprietarioRepository.findById(usuario.usuarioId())
                .orElseThrow(() -> proprietarioNaoEncontrado());
        return respostaProprietario(entidade, proprietario);
    }

    @Override
    @Transactional
    public PerfilDtos.PerfilResponse atualizar(
            AuthenticatedUser usuario,
            String tipoUsuario,
            String nome,
            String email,
            String telefone,
            String cidade,
            String curso,
            String novaSenha,
            boolean removerFoto,
            MultipartFile fotoPerfil) {
        validarTipo(tipoUsuario, usuario);
        String novoNome = obrigatorio(nome, "nome");
        String novoTelefone = obrigatorio(telefone, "telefone");
        if (novoTelefone.length() > 20) {
            throw dadosObrigatorios("telefone deve ter no máximo 20 caracteres");
        }
        String novoEmail = obrigatorio(email, "email").trim().toLowerCase();
        String novaCidade = cidade == null || cidade.isBlank() ? null : cidade.trim();
        if (novaCidade != null && novaCidade.length() > 100) {
            throw dadosObrigatorios("cidade deve ter no máximo 100 caracteres");
        }

        Usuario entidade = usuarioRepository.findById(usuario.usuarioId())
                .orElseThrow(() -> usuarioNaoEncontrado(usuario));
        boolean estudantePerfil = ehEstudante(usuario);
        Estudante estudante = null;
        Proprietario proprietario = null;
        if (estudantePerfil) {
            estudante = estudanteRepository.findById(usuario.usuarioId())
                    .orElseThrow(PerfilServiceImpl::estudanteNaoEncontrado);
            boolean emailMudou = !novoEmail.equalsIgnoreCase(entidade.getEmail());
            if (emailMudou
                    && (!novoEmail.endsWith("@" + institutionalDomain)
                    || novoEmail.length() <= institutionalDomain.length() + 1)) {
                throw new BusinessException(
                        ErrorCode.EMAIL_INSTITUCIONAL_INVALIDO,
                        "O e-mail deve pertencer ao domínio institucional permitido");
            }
            if (curso == null || curso.isBlank()) {
                throw dadosObrigatorios("curso é obrigatório para estudantes");
            }
        } else {
            proprietario = proprietarioRepository.findById(usuario.usuarioId())
                    .orElseThrow(PerfilServiceImpl::proprietarioNaoEncontrado);
        }

        validarEmailDisponivel(entidade, novoEmail);

        ArmazenamentoImagens.ImagemArmazenada novaImagem = null;
        String publicId = "perfis/" + (estudantePerfil ? "estudante-" : "proprietario-")
                + usuario.usuarioId();
        if (fotoPerfil != null && !fotoPerfil.isEmpty()) {
            armazenamento.validar(fotoPerfil);
            novaImagem = armazenamento.salvar(fotoPerfil, publicId, true);
        } else if (removerFoto
                && ((estudantePerfil && estudante.getFotoUrl() != null)
                || (!estudantePerfil && proprietario.getFotoUrl() != null))) {
            armazenamento.remover(publicId);
        }

        entidade.atualizarDadosBasicos(novoNome, novoEmail, novoTelefone, novaCidade);
        if (novaSenha != null && !novaSenha.isBlank()) {
            entidade.atualizarSenhaHash(passwordService.hash(novaSenha));
        }
        if (estudantePerfil) {
            estudante.atualizarDados(novoNome, novoEmail, novoTelefone, curso);
            if (novaImagem != null) {
                estudante.atualizarFoto(novaImagem.url());
            } else if (removerFoto) {
                estudante.atualizarFoto(null);
            }
        } else {
            proprietario.atualizarDados(novoNome, novoEmail, novoTelefone);
            if (novaImagem != null) {
                proprietario.atualizarFoto(novaImagem.url());
            } else if (removerFoto) {
                proprietario.atualizarFoto(null);
            }
        }

        try {
            usuarioRepository.save(entidade);
            if (estudantePerfil) {
                estudanteRepository.save(estudante);
            } else {
                proprietarioRepository.save(proprietario);
            }
        } catch (DataIntegrityViolationException exception) {
            throw new BusinessException(
                    ErrorCode.EMAIL_JA_EXISTE,
                    "Já existe um usuário cadastrado com este e-mail");
        }
        return estudantePerfil
                ? respostaEstudante(entidade, estudante)
                : respostaProprietario(entidade, proprietario);
    }

    private void validarTipo(String tipoUsuario, AuthenticatedUser usuario) {
        if (tipoUsuario == null || tipoUsuario.isBlank()) {
            return;
        }
        boolean corresponde = (ehEstudante(usuario) && "aluno".equalsIgnoreCase(tipoUsuario))
                || (!ehEstudante(usuario) && "proprietario".equalsIgnoreCase(tipoUsuario));
        if (!corresponde) {
            throw new BusinessException(
                    ErrorCode.ACESSO_NEGADO,
                    "Perfil sem permissão para esta operação");
        }
    }

    private void validarEmailDisponivel(Usuario atual, String email) {
        if (!email.equalsIgnoreCase(atual.getEmail())) {
            usuarioRepository.findByEmail(email).ifPresent(outro -> {
                if (!outro.getId().equals(atual.getId())) {
                    throw new BusinessException(
                            ErrorCode.EMAIL_JA_EXISTE,
                            "Já existe um usuário cadastrado com este e-mail");
                }
            });
        }
    }

    private PerfilDtos.PerfilResponse respostaEstudante(Usuario usuario, Estudante estudante) {
        return new PerfilDtos.PerfilResponse(
                usuario.getId(),
                "aluno",
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTelefone(),
                usuario.getCidade(),
                estudante.getCurso(),
                estudante.getFotoUrl());
    }

    private PerfilDtos.PerfilResponse respostaProprietario(
            Usuario usuario,
            Proprietario proprietario) {
        return new PerfilDtos.PerfilResponse(
                usuario.getId(),
                "proprietario",
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTelefone(),
                usuario.getCidade(),
                null,
                proprietario.getFotoUrl());
    }

    private boolean ehEstudante(AuthenticatedUser usuario) {
        return "estudante".equalsIgnoreCase(usuario.perfil());
    }

    private static String obrigatorio(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw dadosObrigatorios(campo + " é obrigatório");
        }
        return valor.trim();
    }

    private static BusinessException dadosObrigatorios(String mensagem) {
        return new BusinessException(ErrorCode.DADOS_OBRIGATORIOS, mensagem);
    }

    private BusinessException usuarioNaoEncontrado(AuthenticatedUser usuario) {
        return new BusinessException(
                ehEstudante(usuario)
                        ? ErrorCode.ESTUDANTE_NAO_ENCONTRADO
                        : ErrorCode.RESPONSAVEL_NAO_ENCONTRADO,
                "Usuário não encontrado");
    }

    private static BusinessException estudanteNaoEncontrado() {
        return new BusinessException(ErrorCode.ESTUDANTE_NAO_ENCONTRADO, "Estudante não encontrado");
    }

    private static BusinessException proprietarioNaoEncontrado() {
        return new BusinessException(
                ErrorCode.RESPONSAVEL_NAO_ENCONTRADO,
                "Proprietário não encontrado");
    }
}
