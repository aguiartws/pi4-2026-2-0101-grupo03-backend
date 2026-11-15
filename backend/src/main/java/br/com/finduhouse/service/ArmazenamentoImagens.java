package br.com.finduhouse.service;

import org.springframework.web.multipart.MultipartFile;

public interface ArmazenamentoImagens {

    record ImagemArmazenada(String url, String publicId) {
    }

    void validar(MultipartFile arquivo);

    ImagemArmazenada salvar(MultipartFile arquivo, String publicId, boolean sobrescrever);

    void remover(String publicId);
}
