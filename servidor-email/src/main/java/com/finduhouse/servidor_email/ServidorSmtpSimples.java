package com.finduhouse.servidor_email;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class ServidorSmtpSimples {

    private static final int PORTA = Integer.parseInt(env("SMTP_LISTEN_PORT", "25"));

    // Provedor real (obrigatórios via variável de ambiente)
    private static final String RELAY_HOST = env("RELAY_HOST", null);
    private static final String RELAY_PORT = env("RELAY_PORT", "587");
    private static final String RELAY_USER = env("RELAY_USER", null);
    private static final String RELAY_PASS = env("RELAY_PASS", null);

    private static final Pattern ENDERECO = Pattern.compile("<([^>]*)>");

    private static Session sessaoRelay;

    public static void main(String[] args) throws IOException {
        sessaoRelay = criarSessao();

        try (ServerSocket serverSocket = new ServerSocket(PORTA)) {
            System.out.println("SMTP relay escutando na porta " + PORTA
                    + " -> " + RELAY_HOST + ":" + RELAY_PORT);

            while (true) {
                Socket cliente = serverSocket.accept();
                Thread.ofVirtual().start(() -> tratarCliente(cliente));
            }
        }
    }

    private static Session criarSessao() {
        Properties p = new Properties();
        p.put("mail.smtp.host", RELAY_HOST);
        p.put("mail.smtp.port", RELAY_PORT);
        p.put("mail.smtp.auth", "true");
        p.put("mail.smtp.starttls.enable", "true");
        p.put("mail.smtp.starttls.required", "true");
        p.put("mail.smtp.connectiontimeout", "10000");
        p.put("mail.smtp.timeout", "20000");
        p.put("mail.smtp.writetimeout", "20000");

        return Session.getInstance(p, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(RELAY_USER, RELAY_PASS);
            }
        });
    }

    private static void tratarCliente(Socket socket) {
        try (
            // ISO_8859_1 preserva os bytes exatamente como chegaram
            BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream(), StandardCharsets.ISO_8859_1));
            BufferedWriter out = new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.ISO_8859_1))
        ) {
            responder(out, "220 finduhouse-smtp pronto");

            String remetente = null;
            List<String> destinatarios = new ArrayList<>();
            boolean emData = false;
            StringBuilder corpo = new StringBuilder();

            String linha;
            while ((linha = in.readLine()) != null) {

                if (emData) {
                    if (linha.equals(".")) {
                        emData = false;
                        try {
                            enviar(remetente, destinatarios,
                                   corpo.toString().getBytes(StandardCharsets.ISO_8859_1));
                            System.out.println("Enviado para " + destinatarios);
                            responder(out, "250 OK mensagem enviada");
                        } catch (Exception e) {
                            System.err.println("Falha no envio: " + e.getMessage());
                            responder(out, "554 Falha ao enviar: " + limpar(e.getMessage()));
                        }
                        remetente = null;
                        destinatarios.clear();
                        corpo.setLength(0);
                    } else {
                        // dot-unstuffing (RFC 5321): ".." no início vira "."
                        if (linha.startsWith("..")) linha = linha.substring(1);
                        corpo.append(linha).append("\r\n");
                    }
                    continue;
                }

                String cmd = linha.toUpperCase();

                if (cmd.startsWith("EHLO") || cmd.startsWith("HELO")) {
                    responder(out, "250-finduhouse-smtp");
                    responder(out, "250 8BITMIME");
                } else if (cmd.startsWith("MAIL FROM")) {
                    remetente = extrair(linha);
                    destinatarios.clear();
                    responder(out, "250 OK");
                } else if (cmd.startsWith("RCPT TO")) {
                    String dest = extrair(linha);
                    if (dest == null || dest.isBlank()) {
                        responder(out, "501 Destinatário inválido");
                    } else {
                        destinatarios.add(dest);
                        responder(out, "250 OK");
                    }
                } else if (cmd.equals("DATA")) {
                    if (destinatarios.isEmpty()) {
                        responder(out, "503 Informe RCPT TO antes");
                    } else {
                        emData = true;
                        responder(out, "354 Envie o conteúdo, termine com '.'");
                    }
                } else if (cmd.equals("RSET")) {
                    remetente = null;
                    destinatarios.clear();
                    responder(out, "250 OK");
                } else if (cmd.equals("NOOP")) {
                    responder(out, "250 OK");
                } else if (cmd.equals("QUIT")) {
                    responder(out, "221 Tchau");
                    break;
                } else {
                    responder(out, "500 Comando desconhecido");
                }
            }
        } catch (IOException e) {
            System.err.println("Erro no cliente: " + e.getMessage());
        } finally {
            try { socket.close(); } catch (IOException ignored) {}
        }
    }

    private static void enviar(String remetente, List<String> destinatarios, byte[] raw)
            throws Exception {
        // Cópia da sessão com o envelope sender correto
        Properties props = new Properties();
        props.putAll(sessaoRelay.getProperties());
        if (remetente != null && !remetente.isBlank()) {
            props.put("mail.smtp.from", remetente);
        }
        Session sessao = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(RELAY_USER, RELAY_PASS);
            }
        });

        MimeMessage msg = new MimeMessage(sessao, new ByteArrayInputStream(raw));

        InternetAddress[] to = new InternetAddress[destinatarios.size()];
        for (int i = 0; i < to.length; i++) {
            to[i] = new InternetAddress(destinatarios.get(i));
        }
        Transport.send(msg, to);
    }

    private static String extrair(String linha) {
        Matcher m = ENDERECO.matcher(linha);
        return m.find() ? m.group(1).trim() : null;
    }

    private static void responder(BufferedWriter out, String texto) throws IOException {
        out.write(texto + "\r\n");
        out.flush();
    }

    private static String limpar(String s) {
        return s == null ? "erro" : s.replaceAll("[\\r\\n]+", " ");
    }

    private static String env(String nome, String padrao) {
        String v = System.getenv(nome);
        if (v == null || v.isBlank()) {
            if (padrao == null) {
                throw new IllegalStateException("Variável de ambiente ausente: " + nome);
            }
            return padrao;
        }
        return v;
    }
}