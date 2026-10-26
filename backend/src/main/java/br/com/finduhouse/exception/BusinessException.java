package br.com.finduhouse.exception;

public class BusinessException extends RuntimeException {

    private final ErrorCode codigo;

    public BusinessException(ErrorCode codigo, String mensagem) {
        super(mensagem);
        this.codigo = codigo;
    }

    public ErrorCode getCodigo() {
        return codigo;
    }
}
