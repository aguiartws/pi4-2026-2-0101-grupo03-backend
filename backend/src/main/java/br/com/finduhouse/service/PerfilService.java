package br.com.finduhouse.service;

import org.springframework.web.multipart.MultipartFile;

import br.com.finduhouse.dto.PerfilDtos;

public interface PerfilService {

    PerfilDtos.PerfilResponse obter(AuthenticatedUser usuario);

    PerfilDtos.PerfilResponse atualizar(
            AuthenticatedUser usuario,
            String tipoUsuario,
            String nome,
            String email,
            String telefone,
            String cidade,
            String curso,
            String novaSenha,
            boolean removerFoto,
            MultipartFile fotoPerfil);

    PerfilDtos.PerfilResponse atualizarSobreMim(
            AuthenticatedUser usuario,
            String sobreMim
    );

    
}
