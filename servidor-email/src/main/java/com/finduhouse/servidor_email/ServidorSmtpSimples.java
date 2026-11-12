package com.finduhouse.servidor_email;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorSmtpSimples {

    private static final int PORTA = 25;

    public static void main(String[] args) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(PORTA)) {
            System.out.println("SMTP escutando na porta " + PORTA);

            while (true) {
                Socket cliente = serverSocket.accept();
                new Thread(() -> tratarCliente(cliente)).start();
            }
        }
    }

    private static void tratarCliente(Socket socket) {
        try (
            BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(
                socket.getOutputStream(), true)
        ) {
            out.println("220 localhost SMTP simples");

            String linha;
            boolean emData = false;
            StringBuilder corpo = new StringBuilder();

            while ((linha = in.readLine()) != null) {
                if (emData) {
                    if (linha.equals(".")) {
                        emData = false;
                        System.out.println("--- EMAIL RECEBIDO ---");
                        System.out.println(corpo);
                        System.out.println("----------------------");
                        out.println("250 OK mensagem aceita");
                        corpo.setLength(0);
                    } else {
                        corpo.append(linha).append("\n");
                    }
                    continue;
                }

                if (linha.startsWith("EHLO") || linha.startsWith("HELO")) {
                    out.println("250-localhost");
                    out.println("250 OK");
                } else if (linha.startsWith("MAIL FROM")) {
                    out.println("250 OK");
                } else if (linha.startsWith("RCPT TO")) {
                    out.println("250 OK");
                } else if (linha.equals("DATA")) {
                    emData = true;
                    out.println("354 Envie o conteúdo, termine com '.'");
                } else if (linha.equals("QUIT")) {
                    out.println("221 Tchau");
                    break;
                } else {
                    out.println("500 Comando desconhecido");
                }
            }
        } catch (IOException e) {
            System.err.println("Erro no cliente: " + e.getMessage());
        } finally {
            try { socket.close(); } catch (IOException ignored) {}
        }
    }
}
