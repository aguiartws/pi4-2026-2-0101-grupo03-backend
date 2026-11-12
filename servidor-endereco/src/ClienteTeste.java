import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClienteTeste {
    public static void main(String[] args) {
        try (
            Socket socket = new Socket("localhost", 5000);
            BufferedReader entrada = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
            PrintWriter saida = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader teclado = new BufferedReader(
                new InputStreamReader(System.in))
        ) {
            // Thread para ler respostas do servidor
            new Thread(() -> {
                try {
                    String resposta;
                    while ((resposta = entrada.readLine()) != null) {
                        System.out.println(resposta);
                    }
                } catch (IOException e) {
                    System.err.println("Conexão encerrada.");
                }
            }).start();

            // Envia entradas do usuário
            String linha;
            while ((linha = teclado.readLine()) != null) {
                saida.println(linha);
                if (linha.equalsIgnoreCase("sair")) break;
            }
        } catch (IOException e) {
            System.err.println("Erro no cliente: " + e.getMessage());
        }
    }
}