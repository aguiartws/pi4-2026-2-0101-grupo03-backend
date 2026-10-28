package br.com.finduhouse.exception;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import br.com.finduhouse.dto.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(BusinessException exception) {
        HttpStatus status = statusFor(exception.getCodigo());
        return response(status, exception.getCodigo(), exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleBodyValidation(
            MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        return response(HttpStatus.BAD_REQUEST, ErrorCode.DADOS_OBRIGATORIOS, message);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintValidation(
            ConstraintViolationException exception) {
        String message = exception.getConstraintViolations()
                .stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining("; "));

        return response(HttpStatus.BAD_REQUEST, ErrorCode.DADOS_OBRIGATORIOS, message);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnreadableMessage(
            HttpMessageNotReadableException exception) {
        return response(
                HttpStatus.BAD_REQUEST,
                ErrorCode.DADOS_OBRIGATORIOS,
                "O corpo da requisição é inválido ou contém um valor não permitido");
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParameter(
            MissingServletRequestParameterException exception) {
        return response(
                HttpStatus.BAD_REQUEST,
                ErrorCode.DADOS_OBRIGATORIOS,
                "Parâmetro obrigatório não informado: " + exception.getParameterName());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(
            MethodArgumentTypeMismatchException exception) {
        return response(
                HttpStatus.BAD_REQUEST,
                ErrorCode.DADOS_OBRIGATORIOS,
                "Parâmetro inválido: " + exception.getName());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception exception) {
        LOGGER.error("Erro inesperado ao processar a requisição", exception);
        return response(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ErrorCode.ERRO_INTERNO,
                "Ocorreu um erro interno ao processar a requisição");
    }

    private ResponseEntity<ErrorResponse> response(
            HttpStatus status,
            ErrorCode codigo,
            String mensagem) {
        ErrorResponse body = new ErrorResponse(codigo.name(), mensagem, LocalDateTime.now());
        return ResponseEntity.status(status).body(body);
    }

    private HttpStatus statusFor(ErrorCode codigo) {
        return switch (codigo) {
            case DADOS_OBRIGATORIOS,
                    EMAIL_INSTITUCIONAL_INVALIDO,
                    VALOR_INVALIDO,
                    FILTROS_INVALIDOS,
                    QUESTIONARIO_INCOMPLETO,
                    RESPOSTA_INVALIDA,
                    MOTIVO_INVALIDO,
                    DESCRICAO_OBRIGATORIA -> HttpStatus.BAD_REQUEST;
            case CREDENCIAIS_INVALIDAS -> HttpStatus.UNAUTHORIZED;
            case ACESSO_NEGADO -> HttpStatus.FORBIDDEN;
            case RESPONSAVEL_NAO_ENCONTRADO,
                    VAGA_NAO_ENCONTRADA,
                    ESTUDANTE_NAO_ENCONTRADO,
                    QUESTIONARIO_NAO_PREENCHIDO -> HttpStatus.NOT_FOUND;
            case EMAIL_JA_EXISTE,
                    VAGA_JA_FAVORITADA,
                    INTERESSE_JA_REGISTRADO,
                    DENUNCIA_DUPLICADA -> HttpStatus.CONFLICT;
            case FALHA_ENVIO_EMAIL,
                    ERRO_INTERNO -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }
}
