package br.com.finduhouse.service;

import br.com.finduhouse.dto.AuthDtos;

public interface AuthService {

    AuthDtos.LoginResponse autenticar(AuthDtos.LoginRequest request);
}
