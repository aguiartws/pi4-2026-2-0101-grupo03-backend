package br.com.finduhouse.dto;

public class CepDtos {
    public record Response(
            boolean valido,
            String mensagem,
            String cep,
            String logradouro,
            String bairro,
            String cidade,
            String uf) {

        public static Response invalido(String mensagem) {
            return new Response(false, mensagem, null, null, null, null, null);
        }
    }
}