package br.com.finduhouse.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // Mantido para compatibilidade (se algo ainda usa)
    public void enviarEmailContato(String emailEstudante, String emailProprietario,
                                   String assunto, String corpo) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailEstudante);
        message.setTo(emailProprietario);
        message.setSubject(assunto);
        message.setText(corpo);
        mailSender.send(message);
    }

    // ===== NOVO: envia DOIS e-mails =====
    public void enviarDoisEmails(
            String emailEstudante, String nomeEstudante, String telefoneEstudante,
            String emailProprietario, String nomeProprietario, String telefoneProprietario,
            String tituloVaga) {

        // E-mail 1: para o ESTUDANTE (com dados do PROPRIETÁRIO)
        SimpleMailMessage paraEstudante = new SimpleMailMessage();
        paraEstudante.setFrom("nao-responda@finduhouse.com");
        paraEstudante.setTo(emailEstudante);
        paraEstudante.setSubject("Interesse registrado na vaga: " + tituloVaga);
        paraEstudante.setText(
                "Olá " + nomeEstudante + ",\n\n" +
                "Seu interesse na vaga \"" + tituloVaga + "\" foi registrado.\n\n" +
                "Dados do proprietário:\n" +
                "  Nome: " + nomeProprietario + "\n" +
                "  E-mail: " + emailProprietario + "\n" +
                "  WhatsApp: https://wa.me/" + telefoneProprietario + "\n\n" +
                "Atenciosamente,\nFindUHouse"
        );

        // E-mail 2: para o PROPRIETÁRIO (com dados do ESTUDANTE)
        SimpleMailMessage paraProprietario = new SimpleMailMessage();
        paraProprietario.setFrom("nao-responda@finduhouse.com");
        paraProprietario.setTo(emailProprietario);
        paraProprietario.setSubject("Novo interesse na sua vaga: " + tituloVaga);
        paraProprietario.setText(
                "Olá " + nomeProprietario + ",\n\n" +
                "O estudante " + nomeEstudante + " demonstrou interesse na sua vaga \"" +
                tituloVaga + "\".\n\n" +
                "Dados do estudante:\n" +
                "  Nome: " + nomeEstudante + "\n" +
                "  E-mail: " + emailEstudante + "\n" +
                "  WhatsApp: https://wa.me/" + telefoneEstudante + "\n\n" +
                "Atenciosamente,\nFindUHouse"
        );

        mailSender.send(paraEstudante);
        mailSender.send(paraProprietario);
    }
}