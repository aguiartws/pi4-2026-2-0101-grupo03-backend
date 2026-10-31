package br.com.finduhouse.service;

import java.util.Locale;
import java.util.List;

import br.com.finduhouse.dto.VagaDtos;
import br.com.finduhouse.entity.Usuario;
import br.com.finduhouse.entity.Vaga;
import br.com.finduhouse.exception.BusinessException;
import br.com.finduhouse.exception.ErrorCode;
import br.com.finduhouse.repository.UsuarioRepository;
import br.com.finduhouse.repository.VagaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VagaServiceImpl implements VagaService {

    private final UsuarioRepository usuarioRepository;
    private final VagaRepository vagaRepository;

    public VagaServiceImpl(
            UsuarioRepository usuarioRepository,
            VagaRepository vagaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.vagaRepository = vagaRepository;
    }

    @Override
    @Transactional
    public VagaDtos.CadastroResponse cadastrar(
            VagaDtos.CadastroRequest request,
            long usuarioAutenticadoId) {
        if (request.responsavelId() != usuarioAutenticadoId) {
            throw new BusinessException(
                    ErrorCode.ACESSO_NEGADO,
                    "O responsável da vaga deve ser o usuário autenticado");
        }
        Usuario responsavel = usuarioRepository
                .findByIdAndPerfil(usuarioAutenticadoId, Usuario.Perfil.PROPRIETARIO)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.RESPONSAVEL_NAO_ENCONTRADO,
                        "O responsável informado não é um proprietário cadastrado"));

        Vaga vaga = new Vaga(
                responsavel,
                request.titulo(),
                request.descricao(),
                request.endereco(),
                request.valorAluguel(),
                request.numeroMoradores(),
                request.regrasCasa(),
                request.latitude(),
                request.longitude(),
                valor(request.regras().horarioDormirSemana()),
                valor(request.regras().horarioDormirFimSemana()),
                valor(request.regras().horarioAcordarSemana()),
                valor(request.regras().horarioAcordarFimSemana()),
                valor(request.regras().nivelLimpeza()),
                valor(request.regras().nivelOrganizacao()),
                valor(request.regras().toleranciaRuido()),
                valor(request.regras().frequenciaVisitas()),
                valor(request.regras().frequenciaFestas()),
                request.regras().temPet());

        Vaga salva = vagaRepository.save(vaga);
        return new VagaDtos.CadastroResponse(
                salva.getId(),
                VagaDtos.Status.valueOf(salva.getStatus().name()),
                salva.getCriadoEm());
    }

    private String valor(Enum<?> valor) {
        return valor.name().toLowerCase(Locale.ROOT);
    }

    @Override
    @Transactional
    public VagaDtos.EdicaoResponse editar(
            long vagaId,
            VagaDtos.EdicaoRequest request,
            long usuarioAutenticadoId) {
        if (request.responsavelId() != usuarioAutenticadoId) {
            throw new BusinessException(
                    ErrorCode.ACESSO_NEGADO,
                    "A vaga só pode ser editada pelo responsável autenticado");
        }

        Vaga vaga = vagaRepository.findById(vagaId)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.VAGA_NAO_ENCONTRADA,
                        "Vaga não encontrada"));
        if (!vaga.getResponsavel().getId().equals(usuarioAutenticadoId)) {
            throw new BusinessException(
                    ErrorCode.ACESSO_NEGADO,
                    "A vaga não pertence ao responsável autenticado");
        }

        vaga.atualizarValorAluguel(request.valorAluguel());
        Vaga atualizada = vagaRepository.save(vaga);
        return new VagaDtos.EdicaoResponse(
                atualizada.getId(),
                atualizada.getValorAluguel(),
                atualizada.getAtualizadoEm());
    }

    @Override
    public VagaDtos.BuscaResponse buscar(
            java.math.BigDecimal valorAluguelMaximo,
            java.math.BigDecimal distanciaMaxima) {
        throw new UnsupportedOperationException("Busca de vagas ainda não implementada");
    }

    @Override
    public VagaDtos.MapaResponse listarMapa() {
        throw new UnsupportedOperationException("Listagem de vagas no mapa ainda não implementada");
    }

    @Override
    @Transactional(readOnly = true)
    public VagaDtos.ListaResponsavelResponse listarPorResponsavel(
            long responsavelId,
            long usuarioAutenticadoId) {
        if (responsavelId != usuarioAutenticadoId) {
            throw new BusinessException(
                    ErrorCode.ACESSO_NEGADO,
                    "Só é possível listar as vagas do usuário autenticado");
        }
        if (usuarioRepository.findByIdAndPerfil(responsavelId, Usuario.Perfil.PROPRIETARIO).isEmpty()) {
            throw new BusinessException(
                    ErrorCode.RESPONSAVEL_NAO_ENCONTRADO,
                    "Responsável não encontrado");
        }

        List<VagaDtos.ListaResponsavelItem> vagas = vagaRepository.findByResponsavelId(responsavelId)
                .stream()
                .map(vaga -> new VagaDtos.ListaResponsavelItem(
                        vaga.getId(),
                        vaga.getTitulo(),
                        vaga.getEndereco(),
                        vaga.getValorAluguel(),
                        VagaDtos.Status.valueOf(vaga.getStatus().name()),
                        vaga.getCriadoEm()))
                .toList();
        return new VagaDtos.ListaResponsavelResponse(vagas);
    }
}
