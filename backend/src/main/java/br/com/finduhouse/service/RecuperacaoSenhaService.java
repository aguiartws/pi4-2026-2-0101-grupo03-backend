package br.com.finduhouse.service;

public interface RecuperacaoSenhaService {

    /**
     * Envia um código de 4 dígitos por e-mail. Se o e-mail não estiver cadastrado, não faz
     * nada (e não revela isso a quem chamou).
     */
    void solicitarCodigo(String email);

    /** Confere o código e devolve um token de recuperação de uso único. */
    String verificarCodigo(String email, String codigo);

    /** Troca a senha do usuário dono do token de recuperação. */
    void redefinirSenha(String tokenRecuperacao, String novaSenha);
}
