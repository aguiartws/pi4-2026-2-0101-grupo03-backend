package br.com.finduhouse.service;

public interface PasswordService {

    String hash(String senha);

    boolean matches(String senha, String hash);
}
