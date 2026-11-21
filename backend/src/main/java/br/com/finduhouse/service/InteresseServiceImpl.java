package br.com.finduhouse.service;

import java.util.List; 

import org.springframework.dao.DataIntegrityViolationException;
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

    public InteresseServiceImpl(
            EstudanteRepository estudanteRepository,
            VagaRepository vagaRepository,
            InteresseRepository interesseRepository,
            JavaMailSender mailSender) {
        this.estudanteRepository = estudanteRepository;
        this.vagaRepository = vagaRepository;
        this.interesseRepository = interesseRepository;
        this.mailSender = mailSender;
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

    // --- MÉTODO ADICIONADO AQUI ---
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
    // ------------------------------

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

        List<InteresseDtos.ProprietarioMatchItem> matches =
                interesseRepository.findByResponsavelId(responsavelId)
                        .stream()
                        .map(interesse -> new InteresseDtos.ProprietarioMatchItem(
                                interesse.getId(),
                                interesse.getVaga().getId(),
                                interesse.getVaga().getTitulo(),
                                interesse.getEstudante().getNome(),
                                interesse.getEstudante().getCurso(),
                                interesse.getEstudante().getEmail(),
                                interesse.getEstudante().getTelefone(),
                                interesse.getEstudante().getFotoUrl(),
                                interesse.getCriadoEm()))
                        .toList();

        return new InteresseDtos.ListaProprietarioMatchesResponse(matches);
    }

    private BusinessException interesseDuplicado() {
        return new BusinessException(
                ErrorCode.INTERESSE_JA_REGISTRADO,
                "O interesse já foi registrado para esta vaga pelo estudante");
    }
}