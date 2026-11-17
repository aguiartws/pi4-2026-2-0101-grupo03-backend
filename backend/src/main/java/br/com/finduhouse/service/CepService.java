package br.com.finduhouse.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.finduhouse.dto.CepDtos;

@Service
public class CepService {

    private static final int TIMEOUT_MS = 5000;

    @Value("${cep.validador.host:localhost}")
    private String host;

    @Value("${cep.validador.porta:5000}")
    private int porta;

    private final ObjectMapper mapper = new ObjectMapper();
    private final HttpClient http = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    public CepDtos.Response consultar(String cep) {
        // 1) Validação feita pelo ServidorValidador (socket TCP)
        String respostaSocket = validarNoServidorSocket(cep);

        if (!respostaSocket.contains("VÁLIDO") || respostaSocket.contains("INVÁLIDO")) {
            // Ex.: ">> INVÁLIDO - CEP 00000-000 não existe"
            String msg = respostaSocket.contains(" - ")
                    ? respostaSocket.substring(respostaSocket.indexOf(" - ") + 3)
                    : "CEP inválido";
            return CepDtos.Response.invalido(msg);
        }

        // 2) CEP com formato válido: busca o endereço na ViaCEP
        String digitos = cep.replace("-", "");
        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create("https://viacep.com.br/ws/" + digitos + "/json/"))
                    .timeout(Duration.ofSeconds(5))
                    .GET()
                    .build();
            HttpResponse<String> resp = http.send(req, HttpResponse.BodyHandlers.ofString());

            if (resp.statusCode() != 200) {
                throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Serviço de endereços indisponível.");
            }

            JsonNode json = mapper.readTree(resp.body());
            if (json.has("erro")) {
                return CepDtos.Response.invalido("CEP não encontrado");
            }

            return new CepDtos.Response(
                    true,
                    "CEP válido",
                    json.path("cep").asText(""),
                    json.path("logradouro").asText(""),
                    json.path("bairro").asText(""),
                    json.path("localidade").asText(""),
                    json.path("uf").asText(""));

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Consulta interrompida.");
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Serviço de endereços indisponível.");
        }
    }

    /** Fala o protocolo do ServidorValidador: 2 linhas de boas-vindas, 1 CEP por linha, "sair" encerra. */
    private String validarNoServidorSocket(String cep) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, porta), TIMEOUT_MS);
            socket.setSoTimeout(TIMEOUT_MS);

            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter saida = new PrintWriter(socket.getOutputStream(), true);

            entrada.readLine(); // "Bem-vindo ao Validador de CEP!"
            entrada.readLine(); // "Envie um CEP ..."

            saida.println(cep);
            String resposta = entrada.readLine(); // ">> VÁLIDO - CEP válido: 12345-678"

            saida.println("sair");

            if (resposta == null) {
                throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                        "O validador de CEP não respondeu.");
            }
            return resposta;

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Validador de CEP indisponível.");
        }
    }
}