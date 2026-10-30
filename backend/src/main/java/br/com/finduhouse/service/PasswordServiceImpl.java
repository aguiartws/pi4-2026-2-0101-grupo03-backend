package br.com.finduhouse.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordServiceImpl implements PasswordService {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public String hash(String senha) {
        return encoder.encode(senha);
    }

    @Override
    public boolean matches(String senha, String hash) {
        return encoder.matches(senha, hash);
    }
}
