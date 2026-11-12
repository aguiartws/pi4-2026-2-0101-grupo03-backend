import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServidorValidador {

    private static final int PORTA = 5000;
    private static final ExecutorService POOL = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {
        System.out.println("=== Servidor Validador de CEP ===");
        System.out.println("Porta: " + PORTA);

        try (ServerSocket serverSocket = new ServerSocket(PORTA)) {
            System.out.println("Aguardando conexões...\n");

            while (true) {
                Socket cliente = serverSocket.accept();
                System.out.println("Cliente conectado: " + cliente.getInetAddress().getHostAddress());
                POOL.execute(() -> tratarCliente(cliente));
            }
        } catch (IOException e) {
            System.err.println("Erro no servidor: " + e.getMessage());
        }
    }

    private static void tratarCliente(Socket cliente) {
        try (
            BufferedReader entrada = new BufferedReader(
                new InputStreamReader(cliente.getInputStream()));
            PrintWriter saida = new PrintWriter(cliente.getOutputStream(), true)
        ) {
            saida.println("Bem-vindo ao Validador de CEP!");
            saida.println("Envie um CEP (12345-678 ou 12345678) por linha. Digite 'sair' para encerrar.");

            String linha;
            while ((linha = entrada.readLine()) != null) {
                if (linha.equalsIgnoreCase("sair")) {
                    saida.println("Encerrando conexão. Até logo!");
                    break;
                }

                ValidadorEndereco.ResultadoValidacao resultado =
                    ValidadorEndereco.validar(linha);

                saida.println(">> " + resultado.toString());
                System.out.println("[" + cliente.getInetAddress().getHostAddress() +
                    "] " + linha + " -> " + resultado);
            }
        } catch (IOException e) {
            System.err.println("Erro ao tratar cliente: " + e.getMessage());
        } finally {
            try { cliente.close(); } catch (IOException ignored) {}
            System.out.println("Cliente desconectado.\n");
        }
    }
}