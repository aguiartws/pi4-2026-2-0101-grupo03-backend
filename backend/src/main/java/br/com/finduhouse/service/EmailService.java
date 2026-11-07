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

    public void enviarEmailContato(String emailEstudante, String emailProprietario, String assunto, String corpo) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailEstudante); // O e-mail do estudante
        message.setTo(emailProprietario); // O e-mail do dono da vaga
        message.setSubject(assunto);
        message.setText(corpo);
        
        mailSender.send(message);
    }
}