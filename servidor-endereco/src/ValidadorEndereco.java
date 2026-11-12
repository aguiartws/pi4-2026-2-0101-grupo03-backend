import java.util.regex.Pattern;

public class ValidadorEndereco {

    // CEP com hífen: 12345-678
    private static final Pattern CEP_COM_HIFEN = Pattern.compile("^\\d{5}-\\d{3}$");

    // CEP sem hífen: 12345678
    private static final Pattern CEP_SEM_HIFEN = Pattern.compile("^\\d{8}$");

    public static ResultadoValidacao validar(String entrada) {
        if (entrada == null || entrada.trim().isEmpty()) {
            return new ResultadoValidacao(false, "CEP vazio");
        }

        String cep = entrada.trim();

        // Valida formato
        boolean formatoOk = CEP_COM_HIFEN.matcher(cep).matches()
                         || CEP_SEM_HIFEN.matcher(cep).matches();

        if (!formatoOk) {
            return new ResultadoValidacao(false,
                "Formato inválido. Use 12345-678 ou 12345678");
        }

        // Normaliza (remove hífen) para checagens extras
        String somenteDigitos = cep.replace("-", "");

        // CEPs inválidos conhecidos: todos zeros (00000-000) não existe
        if (somenteDigitos.equals("00000000")) {
            return new ResultadoValidacao(false, "CEP 00000-000 não existe");
        }

        // Retorna formatado no padrão XXXXX-XXX
        String formatado = somenteDigitos.substring(0, 5) + "-" + somenteDigitos.substring(5);

        return new ResultadoValidacao(true, "CEP válido: " + formatado);
    }

    public static class ResultadoValidacao {
        private final boolean valido;
        private final String mensagem;

        public ResultadoValidacao(boolean valido, String mensagem) {
            this.valido = valido;
            this.mensagem = mensagem;
        }

        public boolean isValido() { return valido; }
        public String getMensagem() { return mensagem; }

        @Override
        public String toString() {
            return (valido ? "VÁLIDO" : "INVÁLIDO") + " - " + mensagem;
        }
    }
}