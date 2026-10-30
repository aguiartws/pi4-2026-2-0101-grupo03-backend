
package com.finduhouse.servidor_email;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

public class ServidorEmailApplication {

    private static final Logger log =
        LoggerFactory.getLogger(ServidorEmailApplication.class);

    private static final int SMTP_PORT = 25;
    private static final String BIND_HOST = "0.0.0.0";

    private static GreenMail greenMail;

    public static void main(String[] args) throws InterruptedException {

        ServerSetup smtp =
            new ServerSetup(SMTP_PORT, BIND_HOST, "smtp");

        greenMail = new GreenMail(smtp);
        greenMail.start();

        log.info("Servidor SMTP iniciado na porta {}", SMTP_PORT);

        Thread monitor = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(10000);

                    var emails = greenMail.getReceivedMessages();

                    log.info("Total de emails recebidos: {}", emails.length);

                    for (var email : emails) {
                        log.info("Assunto: {}", email.getSubject());
                        log.info("Remetente: {}", email.getFrom()[0]);
                    }

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                    log.error("Erro ao consultar emails", e);
                }
            }
        });

        monitor.setDaemon(true);
        monitor.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Encerrando servidor SMTP...");
            if (greenMail != null) greenMail.stop();
        }));

        new CountDownLatch(1).await();
    }
}
