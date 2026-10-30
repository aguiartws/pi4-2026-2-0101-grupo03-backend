
package com.finduhouse.servidor_email;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;

import java.util.concurrent.CountDownLatch;

public class ServidorEmailApplication {

    private static final int SMTP_PORT = 25;
    private static final String BIND_HOST = "0.0.0.0";

    private static GreenMail greenMail;

    public static void main(String[] args) throws InterruptedException {

        ServerSetup smtp =
            new ServerSetup(SMTP_PORT, BIND_HOST, "smtp");

        greenMail = new GreenMail(smtp);
        greenMail.start();

        System.out.println(
            "Servidor SMTP iniciado na porta " + SMTP_PORT
        );

        Thread monitor = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(10000);

                    var emails = greenMail.getReceivedMessages();

                    System.out.println(
                        "Total de emails recebidos: " + emails.length
                    );

                    for (var email : emails) {
                        System.out.println(
                            "Assunto: " + email.getSubject()
                        );

                        System.out.println(
                            "Remetente: " + email.getFrom()[0]
                        );
                    }

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();

                } catch (Exception e) {
                    System.out.println(
                        "Erro ao consultar emails: " + e.getMessage()
                    );
                }
            }
        });

        monitor.setDaemon(true);
        monitor.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Encerrando servidor SMTP...");

            if (greenMail != null) {
                greenMail.stop();
            }
        }));

        new CountDownLatch(1).await();
    }
}
