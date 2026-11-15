package br.com.finduhouse.service;

import br.com.finduhouse.dto.FotoPerfilDtos;
import br.com.finduhouse.entity.Estudante;
import br.com.finduhouse.entity.Proprietario;
import br.com.finduhouse.exception.BusinessException;
import br.com.finduhouse.exception.ErrorCode;
import br.com.finduhouse.repository.EstudanteRepository;
import br.com.finduhouse.repository.ProprietarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FotoPerfilServiceImpl implements FotoPerfilService {

    private final EstudanteRepository estudanteRepository;
    private final ProprietarioRepository proprietarioRepository;
    private final ArmazenamentoImagens armazenamento;

    public FotoPerfilServiceImpl(
            EstudanteRepository estudanteRepository,
            ProprietarioRepository proprietarioRepository,
            ArmazenamentoImagens armazenamento) {
        this.estudanteRepository = estudanteRepository;
        this.proprietarioRepository = proprietarioRepository;
        this.armazenamento = armazenamento;
    }

    @Override
    @Transactional
    public FotoPerfilDtos.Response atualizarFoto(
            Tipo tipo,
            long idDaRota,
            AuthenticatedUser usuario,
            org.springframework.web.multipart.MultipartFile foto) {
        String perfilEsperado = tipo == Tipo.ESTUDANTE ? "estudante" : "proprietario";
        if (!perfilEsperado.equalsIgnoreCase(usuario.perfil())) {
            throw new BusinessException(
                    ErrorCode.ACESSO_NEGADO,
                    "Perfil sem permissão para esta operação");
        }
        if (idDaRota != usuario.usuarioId()) {
            throw new BusinessException(
                    ErrorCode.ACESSO_NEGADO,
                    "Só é possível alterar a própria foto");
        }

        if (tipo == Tipo.ESTUDANTE) {
            Estudante estudante = estudanteRepository.findById(idDaRota)
                    .orElseThrow(() -> new BusinessException(
                            ErrorCode.ESTUDANTE_NAO_ENCONTRADO,
                            "Estudante não encontrado"));
            armazenamento.validar(foto);
            String publicId = "perfis/" + perfilEsperado + "-" + idDaRota;
            ArmazenamentoImagens.ImagemArmazenada imagem =
                    armazenamento.salvar(foto, publicId, true);
            estudante.atualizarFoto(imagem.url());
            estudanteRepository.save(estudante);
            return new FotoPerfilDtos.Response(imagem.url());
        } else {
            Proprietario proprietario = proprietarioRepository.findById(idDaRota)
                    .orElseThrow(() -> new BusinessException(
                            ErrorCode.RESPONSAVEL_NAO_ENCONTRADO,
                            "Proprietário não encontrado"));
            armazenamento.validar(foto);
            String publicId = "perfis/" + perfilEsperado + "-" + idDaRota;
            ArmazenamentoImagens.ImagemArmazenada imagem =
                    armazenamento.salvar(foto, publicId, true);
            proprietario.atualizarFoto(imagem.url());
            proprietarioRepository.save(proprietario);
            return new FotoPerfilDtos.Response(imagem.url());
        }

    }
}
