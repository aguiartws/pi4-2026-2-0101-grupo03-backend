package br.com.finduhouse.service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import br.com.finduhouse.dto.VagaDtos;
import br.com.finduhouse.entity.Usuario;
import br.com.finduhouse.entity.Vaga;
import br.com.finduhouse.entity.VagaFoto;
import br.com.finduhouse.exception.BusinessException;
import br.com.finduhouse.exception.ErrorCode;
import br.com.finduhouse.repository.DenunciaRepository;
import br.com.finduhouse.repository.FavoritoRepository;
import br.com.finduhouse.repository.InteresseRepository;
import br.com.finduhouse.repository.UsuarioRepository;
import br.com.finduhouse.repository.VagaFotoRepository;
import br.com.finduhouse.repository.VagaRepository;

@Service
public class VagaServiceImpl implements VagaService {

    private final UsuarioRepository usuarioRepository;
    private final VagaRepository vagaRepository;
    private final CalculadoraDistancia calculadoraDistancia;
    private final ArmazenamentoImagens armazenamento;
    private final VagaFotoRepository vagaFotoRepository;
    private final FavoritoRepository favoritoRepository;
    private final InteresseRepository interesseRepository;
    private final DenunciaRepository denunciaRepository;

    @Autowired
    public VagaServiceImpl(
            UsuarioRepository usuarioRepository,
            VagaRepository vagaRepository,
            CalculadoraDistancia calculadoraDistancia,
            ArmazenamentoImagens armazenamento,
            VagaFotoRepository vagaFotoRepository,
            FavoritoRepository favoritoRepository,
            InteresseRepository interesseRepository,
            DenunciaRepository denunciaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.vagaRepository = vagaRepository;
        this.calculadoraDistancia = calculadoraDistancia;
        this.armazenamento = armazenamento;
        this.vagaFotoRepository = vagaFotoRepository;
        this.favoritoRepository = favoritoRepository;
        this.interesseRepository = interesseRepository;
        this.denunciaRepository = denunciaRepository;
    }

    public VagaServiceImpl(
            UsuarioRepository usuarioRepository,
            VagaRepository vagaRepository,
            CalculadoraDistancia calculadoraDistancia) {
        this(usuarioRepository, vagaRepository, calculadoraDistancia, null, null, null, null, null);
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

        // ---- AQUI ESTÁ O QUE MUDA TUDO ----
        VagaDtos.Regras r = request.regrasConvivencia();
        vaga.atualizarDados(
                request.titulo(),
                request.descricao(),
                request.endereco(),
                request.valorAluguel(),
                request.numeroMoradores(),
                request.regrasCasa(),
                request.latitude(),
                request.longitude(),
                Vaga.Status.valueOf(request.status().name()),
                r.aceitaBarulhoDepoisDas22(),
                r.fuma(),
                r.aceitaAnimais(),
                r.gostaReceberVisitas(),
                r.gostaCozinhar(),
                r.estudaEmCasa(),
                r.aceitaDividirTarefas(),
                r.aceitaDividirQuarto(),
                r.aceitaFestas(),
                r.comunicativo());

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
        Map<Long, List<String>> fotosPorVaga = fotosPorVaga(
                vagas.stream().map(vaga -> vaga.vaga().getId()).toList());

        return new VagaDtos.BuscaResponse(vagas.stream()
                .map(vaga -> new VagaDtos.BuscaItem(
                        vaga.vaga().getId(),
                        vaga.vaga().getTitulo(),
                        vaga.vaga().getValorAluguel(),
                        vaga.distancia(),
                        vaga.vaga().getDescricao(),
                        vaga.vaga().getEndereco(),
                        vaga.vaga().getLatitude(),
                        vaga.vaga().getLongitude(),
                        primeiraFoto(fotosPorVaga.get(vaga.vaga().getId()))))
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
        List<VagaDtos.FotoItem> fotos = vagaFotoRepository == null
        ? List.of()
        : vagaFotoRepository.findByVagaIdOrderByOrdemAscIdAsc(vagaId)
                .stream()
                .map(f -> new VagaDtos.FotoItem(f.getId(), f.getUrl()))
                .toList();
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
                RegrasConverter.regras(vaga),
                fotos);
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

        List<Vaga> vagasEncontradas = vagaRepository.findByResponsavelId(responsavelId);
        Map<Long, List<String>> fotosPorVaga = fotosPorVaga(
                vagasEncontradas.stream().map(Vaga::getId).toList());
        List<VagaDtos.ListaResponsavelItem> vagas = vagasEncontradas
                .stream()
                .map(vaga -> new VagaDtos.ListaResponsavelItem(
                        vaga.getId(),
                        vaga.getTitulo(),
                        vaga.getEndereco(),
                        vaga.getValorAluguel(),
                        VagaDtos.Status.valueOf(vaga.getStatus().name()),
                        vaga.getCriadoEm(),
                        primeiraFoto(fotosPorVaga.get(vaga.getId())))
                )
                .toList();
        return new VagaDtos.ListaResponsavelResponse(vagas);
    }

    @Override
    @Transactional
    public void excluir(long vagaId, long usuarioAutenticadoId) {
        Vaga vaga = vagaRepository.findById(vagaId)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.VAGA_NAO_ENCONTRADA,
                        "Vaga não encontrada"));

        if (!vaga.getResponsavel().getId().equals(usuarioAutenticadoId)) {
            throw new BusinessException(
                    ErrorCode.ACESSO_NEGADO,
                    "A vaga não pertence ao responsável autenticado");
        }

        if (favoritoRepository != null) {
            favoritoRepository.deleteByIdVagaId(vagaId);
        }
        if (interesseRepository != null) {
            interesseRepository.deleteByVagaId(vagaId);
        }
        if (denunciaRepository != null) {
            denunciaRepository.deleteByVagaId(vagaId);
        }
        vagaRepository.delete(vaga);
    }

    @Override
    @Transactional
    public VagaDtos.FotosResponse adicionarFotos(
            long vagaId,
            List<MultipartFile> fotos,
            long usuarioAutenticadoId) {
        if (armazenamento == null || vagaFotoRepository == null) {
            throw new IllegalStateException("Armazenamento de fotos não configurado");
        }
        Vaga vaga = vagaRepository.findById(vagaId)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.VAGA_NAO_ENCONTRADA, "Vaga não encontrada"));
        if (!vaga.getResponsavel().getId().equals(usuarioAutenticadoId)) {
            throw new BusinessException(
                    ErrorCode.ACESSO_NEGADO,
                    "A vaga não pertence ao responsável autenticado");
        }

        List<MultipartFile> arquivos = fotos == null ? List.of() : fotos.stream()
                .filter(foto -> foto != null && !foto.isEmpty())
                .toList();
        if (arquivos.isEmpty()) {
            throw new BusinessException(
                    ErrorCode.ARQUIVO_INVALIDO,
                    "É necessário enviar pelo menos uma foto");
        }
        arquivos.forEach(armazenamento::validar);
        List<VagaFoto> existentes = vagaFotoRepository.findByVagaIdOrderByOrdemAscIdAsc(vagaId);
        if (existentes.size() + arquivos.size() > 5) {
            throw new BusinessException(
                    ErrorCode.LIMITE_FOTOS_EXCEDIDO,
                    "Cada vaga aceita no máximo 5 fotos");
        }

        List<String> publicIdsEnviados = new java.util.ArrayList<>();
        try {
            int ordem = existentes.stream()
                    .map(VagaFoto::getOrdem)
                    .max(Integer::compareTo)
                    .map(valor -> valor + 1)
                    .orElse(0);
            for (MultipartFile arquivo : arquivos) {
                String publicId = "vagas/vaga-" + vagaId + "/" + UUID.randomUUID();
                ArmazenamentoImagens.ImagemArmazenada imagem =
                        armazenamento.salvar(arquivo, publicId, false);
                publicIdsEnviados.add(imagem.publicId());
                vagaFotoRepository.save(new VagaFoto(vaga, imagem.url(), ordem++));
            }
        } catch (RuntimeException exception) {
            publicIdsEnviados.forEach(armazenamento::remover);
            throw exception;
        }

        return new VagaDtos.FotosResponse(
                vagaFotoRepository.findByVagaIdOrderByOrdemAscIdAsc(vagaId)
                        .stream().map(VagaFoto::getUrl).toList());
    }

    @Override
    @Transactional
    public void removerFoto(long vagaId, long fotoId, long usuarioAutenticadoId) {
        if (armazenamento == null || vagaFotoRepository == null) {
            throw new IllegalStateException("Armazenamento de fotos não configurado");
        }
        Vaga vaga = vagaRepository.findById(vagaId)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.VAGA_NAO_ENCONTRADA, "Vaga não encontrada"));
        if (!vaga.getResponsavel().getId().equals(usuarioAutenticadoId)) {
            throw new BusinessException(
                    ErrorCode.ACESSO_NEGADO,
                    "A vaga não pertence ao responsável autenticado");
        }
        VagaFoto foto = vagaFotoRepository.findById(fotoId)
                .filter(item -> item.getVaga().getId().equals(vagaId))
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.VAGA_NAO_ENCONTRADA, "Foto não encontrada"));
        vagaFotoRepository.delete(foto);
        String publicId = derivarPublicId(foto.getUrl());
        if (publicId != null) {
            armazenamento.remover(publicId);
        }
    }

    static String derivarPublicId(String url) {
        if (url == null) {
            return null;
        }
        int upload = url.indexOf("/upload/");
        if (upload < 0) {
            return null;
        }
        String caminho = url.substring(upload + "/upload/".length());
        int query = caminho.indexOf('?');
        if (query >= 0) {
            caminho = caminho.substring(0, query);
        }
        if (caminho.startsWith("v") && caminho.indexOf('/') > 1
                && caminho.substring(1, caminho.indexOf('/')).chars().allMatch(Character::isDigit)) {
            caminho = caminho.substring(caminho.indexOf('/') + 1);
        }
        int ponto = caminho.lastIndexOf('.');
        return ponto > caminho.lastIndexOf('/') ? caminho.substring(0, ponto) : caminho;
    }

    private Map<Long, List<String>> fotosPorVaga(List<Long> vagaIds) {
        if (vagaFotoRepository == null || vagaIds.isEmpty()) {
            return Map.of();
        }
        Map<Long, List<String>> resultado = new HashMap<>();
        vagaFotoRepository.findByVagaIdInOrderByOrdemAscIdAsc(vagaIds)
                .forEach(foto -> resultado.computeIfAbsent(
                        foto.getVaga().getId(), ignored -> new java.util.ArrayList<>()).add(foto.getUrl()));
        return resultado;
    }

    private static String primeiraFoto(List<String> fotos) {
        return fotos == null || fotos.isEmpty() ? null : fotos.get(0);
    }

    private record VagaComDistancia(Vaga vaga, BigDecimal distancia) {

    }
}
