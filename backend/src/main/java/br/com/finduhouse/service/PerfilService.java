package br.com.finduhouse.service;

import br.com.finduhouse.dto.PerfilDtos;
import org.springframework.web.multipart.MultipartFile;

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
}
