package br.com.finduhouse.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.finduhouse.dto.InteresseDtos;
import br.com.finduhouse.entity.Estudante;
import br.com.finduhouse.entity.Interesse;
import br.com.finduhouse.entity.Usuario;
import br.com.finduhouse.entity.Vaga;
import br.com.finduhouse.exception.BusinessException;
import br.com.finduhouse.exception.ErrorCode;
import br.com.finduhouse.repository.EstudanteRepository;
import br.com.finduhouse.repository.InteresseRepository;
import br.com.finduhouse.repository.VagaRepository;

@Service
public class InteresseServiceImpl implements InteresseService {

    private final EstudanteRepository estudanteRepository;
    private final VagaRepository vagaRepository;
    private final InteresseRepository interesseRepository;
    private final JavaMailSender mailSender;
    private final JdbcTemplate jdbcTemplate;

    public InteresseServiceImpl(
            EstudanteRepository estudanteRepository,
            VagaRepository vagaRepository,
            InteresseRepository interesseRepository,
            JavaMailSender mailSender,
            JdbcTemplate jdbcTemplate) {
        this.estudanteRepository = estudanteRepository;
        this.vagaRepository = vagaRepository;
        this.interesseRepository = interesseRepository;
        this.mailSender = mailSender;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public InteresseDtos.Response solicitar(
            long vagaId,
            long estudanteId,
            long estudanteAutenticadoId) {
        if (estudanteId != estudanteAutenticadoId) {
            throw new BusinessException(
                    ErrorCode.ACESSO_NEGADO,
                    "A operação só pode ser realizada pelo estudante autenticado");
        }

        Estudante estudante = estudanteRepository.findById(estudanteId)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.ESTUDANTE_NAO_ENCONTRADO,
                        "Estudante não encontrado"));
        Vaga vaga = vagaRepository.findById(vagaId)
                .filter(vagaEncontrada -> vagaEncontrada.getStatus() == Vaga.Status.ATIVA)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.VAGA_NAO_ENCONTRADA,
                        "Vaga não encontrada"));

        if (interesseRepository.existsByEstudanteIdAndVagaId(estudanteId, vagaId)) {
            throw interesseDuplicado();
        }

        try {
            interesseRepository.save(new Interesse(estudante, vaga));
        } catch (DataIntegrityViolationException exception) {
            throw interesseDuplicado();
        }

        // --- LÓGICA DO SERVIDOR DE E-MAIL ---
        try {
            Usuario proprietario = vaga.getResponsavel();

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("nao-responda@finduhouse.com");
            message.setTo(proprietario.getEmail());
            message.setSubject("Novo Interesse na sua vaga: " + vaga.getTitulo());

            String textoEmail = "Olá " + proprietario.getEmail() + ",\n\n"
                    + "O estudante " + estudante.getNome() + " demonstrou interesse na sua vaga '" + vaga.getTitulo() + "'.\n"
                    + "Entre em contato:\n"
                    + "E-mail: " + estudante.getEmail() + "\n"
                    + "Telefone: " + estudante.getTelefone() + "\n\n"
                    + "Atenciosamente,\nEquipe FindUHouse";

            message.setText(textoEmail);
            mailSender.send(message);
            System.out.println("E-mail enviado com sucesso para: " + proprietario.getEmail());

        } catch (Exception e) {
            System.err.println("Erro ao enviar e-mail de interesse: " + e.getMessage());
        }

        return new InteresseDtos.Response(true, false, "Interesse registrado.");
    }

    @Override
    @Transactional
    public void desfazer(long vagaId, long estudanteId, long estudanteAutenticadoId) {
        if (estudanteId != estudanteAutenticadoId) {
            throw new BusinessException(
                    ErrorCode.ACESSO_NEGADO,
                    "A operação só pode ser realizada pelo estudante autenticado");
        }
        // idempotente: se já não existir, não faz nada
        interesseRepository.deleteByEstudanteIdAndVagaId(estudanteId, vagaId);
    }

    @Override
    @Transactional(readOnly = true)
    public InteresseDtos.ListaMatchesResponse listarMatches(long estudanteId, long estudanteAutenticadoId) {
        if (estudanteId != estudanteAutenticadoId) {
            throw new BusinessException(
                    ErrorCode.ACESSO_NEGADO,
                    "A operação só pode ser realizada pelo estudante autenticado");
        }

        List<Interesse> interesses = interesseRepository.findByEstudanteId(estudanteId);

        List<InteresseDtos.MatchItem> matches = interesses.stream()
                .map(interesse -> new InteresseDtos.MatchItem(
                        interesse.getVaga().getId(),
                        interesse.getVaga().getTitulo(),
                        interesse.getVaga().getEndereco(),
                        interesse.getVaga().getValorAluguel()))
                .toList();

        return new InteresseDtos.ListaMatchesResponse(matches);
    }

    @Override
    @Transactional(readOnly = true)
    public InteresseDtos.ListaProprietarioMatchesResponse listarMatchesDoProprietario(
            long responsavelId,
            long responsavelAutenticadoId) {
        if (responsavelId != responsavelAutenticadoId) {
            throw new BusinessException(
                    ErrorCode.ACESSO_NEGADO,
                    "A operação só pode ser realizada pelo proprietário autenticado");
        }

        List<Interesse> interesses = interesseRepository.findByResponsavelId(responsavelId);

        // Uma única consulta para buscar o questionário de todos os estudantes
        List<Long> estudanteIds = interesses.stream()
                .map(interesse -> interesse.getEstudante().getId())
                .distinct()
                .toList();
        Map<Long, Map<String, Boolean>> questionarios = buscarQuestionarios(estudanteIds);

        List<InteresseDtos.ProprietarioMatchItem> matches = interesses.stream()
                .map(interesse -> {
                    Estudante estudante = interesse.getEstudante();

                    return new InteresseDtos.ProprietarioMatchItem(
                            interesse.getId(),
                            interesse.getVaga().getId(),
                            interesse.getVaga().getTitulo(),
                            estudante.getNome(),
                            estudante.getCurso(),
                            estudante.getEmail(),
                            estudante.getTelefone(),
                            estudante.getFotoUrl(),
                            estudante.getSobreMim(),
                            interesse.getCriadoEm(),
                            estudante.getId(),
                            // null se o estudante ainda não respondeu o questionário
                            questionarios.get(estudante.getId()));
                })
                .toList();

        return new InteresseDtos.ListaProprietarioMatchesResponse(matches);
    }

    /**
     * Lê a tabela questionario_estudante e devolve, por id de estudante,
     * um mapa com as chaves em camelCase que o front-end espera.
     */
    private Map<Long, Map<String, Boolean>> buscarQuestionarios(Collection<Long> estudanteIds) {
        Map<Long, Map<String, Boolean>> resultado = new HashMap<>();
        if (estudanteIds.isEmpty()) {
            return resultado;
        }

        String placeholders = estudanteIds.stream()
                .map(id -> "?")
                .collect(Collectors.joining(","));

        String sql = "SELECT estudante_id, aceita_barulho_depois_das_22, fuma, aceita_animais, "
                + "gosta_receber_visitas, gosta_cozinhar, estuda_em_casa, aceita_dividir_tarefas, "
                + "aceita_dividir_quarto, aceita_festas, comunicativo "
                + "FROM questionario_estudante WHERE estudante_id IN (" + placeholders + ")";

        jdbcTemplate.query(sql, (RowCallbackHandler) rs -> {
            Map<String, Boolean> respostas = new LinkedHashMap<>();
            respostas.put("aceitaBarulhoDepoisDas22", lerBoolean(rs, "aceita_barulho_depois_das_22"));
            respostas.put("fuma", lerBoolean(rs, "fuma"));
            respostas.put("aceitaAnimais", lerBoolean(rs, "aceita_animais"));
            respostas.put("gostaReceberVisitas", lerBoolean(rs, "gosta_receber_visitas"));
            respostas.put("gostaCozinhar", lerBoolean(rs, "gosta_cozinhar"));
            respostas.put("estudaEmCasa", lerBoolean(rs, "estuda_em_casa"));
            respostas.put("aceitaDividirTarefas", lerBoolean(rs, "aceita_dividir_tarefas"));
            respostas.put("aceitaDividirQuarto", lerBoolean(rs, "aceita_dividir_quarto"));
            respostas.put("aceitaFestas", lerBoolean(rs, "aceita_festas"));
            respostas.put("comunicativo", lerBoolean(rs, "comunicativo"));
            resultado.put(rs.getLong("estudante_id"), respostas);
        }, estudanteIds.toArray());

        return resultado;
    }

    private static Boolean lerBoolean(ResultSet rs, String coluna) throws SQLException {
        boolean valor = rs.getBoolean(coluna);
        return rs.wasNull() ? null : valor;
    }

    private BusinessException interesseDuplicado() {
        return new BusinessException(
                ErrorCode.INTERESSE_JA_REGISTRADO,
                "O interesse já foi registrado para esta vaga pelo estudante");
    }
}