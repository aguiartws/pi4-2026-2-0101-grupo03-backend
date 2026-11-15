package br.com.finduhouse.service;

import br.com.finduhouse.dto.FotoPerfilDtos;
import org.springframework.web.multipart.MultipartFile;

public interface FotoPerfilService {

    enum Tipo {
        ESTUDANTE,
        PROPRIETARIO
    }

    FotoPerfilDtos.Response atualizarFoto(
            Tipo tipo,
            long idDaRota,
            AuthenticatedUser usuario,
            MultipartFile foto);
}
