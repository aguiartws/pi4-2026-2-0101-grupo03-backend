package br.com.finduhouse.service;

import java.util.List;
import java.math.BigDecimal;
import java.util.Comparator;

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
    private final CalculadoraDistancia calculadoraDistancia;

    public VagaServiceImpl(
            UsuarioRepository usuarioRepository,
            VagaRepository vagaRepository,
            CalculadoraDistancia calculadoraDistancia) {
        this.usuarioRepository = usuarioRepository;
        this.vagaRepository = vagaRepository;
        this.calculadoraDistancia = calculadoraDistancia;
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
                request.regras().aceitaBarulhoDepoisDas22(),
                request.regras().fuma(),
                request.regras().aceitaAnimais(),
                request.regras().gostaReceberVisitas(),
                request.regras().gostaCozinhar(),
                request.regras().estudaEmCasa(),
                request.regras().aceitaDividirTarefas(),
                request.regras().aceitaDividirQuarto(),
                request.regras().aceitaFestas(),
                request.regras().comunicativo());

        Vaga salva = vagaRepository.save(vaga);
        return new VagaDtos.CadastroResponse(
                salva.getId(),
                VagaDtos.Status.valueOf(salva.getStatus().name()),
                salva.getCriadoEm());
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
            BigDecimal valorAluguelMaximo,
            BigDecimal distanciaMaxima) {
        if (valorAluguelMaximo == null
                || distanciaMaxima == null
                || valorAluguelMaximo.signum() < 0
                || distanciaMaxima.signum() < 0) {
            throw new BusinessException(
                    ErrorCode.FILTROS_INVALIDOS,
                    "Os filtros devem ser números não negativos");
        }

        List<VagaComDistancia> vagas = vagaRepository
                .findByStatusAndValorAluguelLessThanEqual(Vaga.Status.ATIVA, valorAluguelMaximo)
                .stream()
                .map(vaga -> new VagaComDistancia(
                        vaga,
                        calculadoraDistancia.calcular(vaga.getLatitude(), vaga.getLongitude())))
                .filter(vaga -> vaga.distancia().compareTo(distanciaMaxima) <= 0)
                .sorted(Comparator.comparing(VagaComDistancia::distancia)
                        .thenComparing(vaga -> vaga.vaga().getId()))
                .toList();

        return new VagaDtos.BuscaResponse(vagas.stream()
                .map(vaga -> new VagaDtos.BuscaItem(
                        vaga.vaga().getId(),
                        vaga.vaga().getTitulo(),
                        vaga.vaga().getValorAluguel(),
                        vaga.distancia(),
                        vaga.vaga().getDescricao(),
                        vaga.vaga().getEndereco(),
                        vaga.vaga().getLatitude(),
                        vaga.vaga().getLongitude()))
                .toList());
    }

    @Override
    public VagaDtos.MapaResponse listarMapa() {
        return new VagaDtos.MapaResponse(vagaRepository.findByStatus(Vaga.Status.ATIVA)
                .stream()
                .map(vaga -> new VagaDtos.MapaItem(
                        vaga.getId(),
                        vaga.getTitulo(),
                        vaga.getEndereco(),
                        vaga.getValorAluguel(),
                        vaga.getLatitude(),
                        vaga.getLongitude()))
                .toList());
    }

    @Override
    @Transactional(readOnly = true)
    public VagaDtos.DetalheResponse obterDetalhes(long vagaId) {
        Vaga vaga = vagaRepository.findById(vagaId)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.VAGA_NAO_ENCONTRADA,
                        "Vaga não encontrada"));
        return new VagaDtos.DetalheResponse(
                vaga.getId(),
                vaga.getTitulo(),
                vaga.getDescricao(),
                vaga.getEndereco(),
                vaga.getValorAluguel(),
                vaga.getNumeroMoradores(),
                vaga.getRegrasCasa(),
                vaga.getLatitude(),
                vaga.getLongitude(),
                VagaDtos.Status.valueOf(vaga.getStatus().name()),
                RegrasConverter.regras(vaga));
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

    private record VagaComDistancia(Vaga vaga, BigDecimal distancia) {
    }
}
