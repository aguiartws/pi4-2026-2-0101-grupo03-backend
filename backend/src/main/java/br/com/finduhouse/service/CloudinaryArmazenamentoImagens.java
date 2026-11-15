package br.com.finduhouse.service;

import java.io.IOException;
import java.util.Map;

import br.com.finduhouse.exception.BusinessException;
import br.com.finduhouse.exception.ErrorCode;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CloudinaryArmazenamentoImagens implements ArmazenamentoImagens {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(CloudinaryArmazenamentoImagens.class);
    private static final long TAMANHO_MAXIMO = 5 * 1024 * 1024;

    private final Cloudinary cloudinary;
    private final String cloudName;
    private final String apiKey;
    private final String apiSecret;

    public CloudinaryArmazenamentoImagens(
            Cloudinary cloudinary,
            @Value("${finduhouse.cloudinary.cloud-name:}") String cloudName,
            @Value("${finduhouse.cloudinary.api-key:}") String apiKey,
            @Value("${finduhouse.cloudinary.api-secret:}") String apiSecret) {
        this.cloudinary = cloudinary;
        this.cloudName = cloudName;
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
    }

    @Override
    public void validar(MultipartFile arquivo) {
        if (arquivo == null || arquivo.isEmpty() || arquivo.getSize() > TAMANHO_MAXIMO) {
            throw arquivoInvalido();
        }

        String contentType = arquivo.getContentType();
        if (!"image/jpeg".equals(contentType)
                && !"image/png".equals(contentType)
                && !"image/webp".equals(contentType)) {
            throw arquivoInvalido();
        }

        try {
            byte[] bytes = arquivo.getBytes();
            if (!formatoValido(contentType, bytes)) {
                throw arquivoInvalido();
            }
        } catch (IOException exception) {
            throw arquivoInvalido();
        }
    }

    @Override
    public ImagemArmazenada salvar(MultipartFile arquivo, String publicId, boolean sobrescrever) {
        validar(arquivo);
        if (cloudName.isBlank() || apiKey.isBlank() || apiSecret.isBlank()) {
            LOGGER.error("Armazenamento Cloudinary não está configurado");
            throw new BusinessException(
                    ErrorCode.FALHA_UPLOAD_IMAGEM,
                    "O armazenamento de imagens não está configurado");
        }

        try {
            Map<?, ?> resultado = cloudinary.uploader().upload(
                    arquivo.getBytes(),
                    ObjectUtils.asMap(
                            "public_id", publicId,
                            "overwrite", sobrescrever,
                            "invalidate", sobrescrever,
                            "unique_filename", false,
                            "resource_type", "image"));
            return new ImagemArmazenada(
                    String.valueOf(resultado.get("secure_url")),
                    String.valueOf(resultado.get("public_id")));
        } catch (Exception exception) {
            LOGGER.error("Falha ao enviar imagem para o Cloudinary", exception);
            throw new BusinessException(
                    ErrorCode.FALHA_UPLOAD_IMAGEM,
                    "Não foi possível enviar a imagem. Tente novamente.");
        }
    }

    @Override
    public void remover(String publicId) {
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.asMap("invalidate", true));
        } catch (Exception exception) {
            LOGGER.warn("Falha ao remover imagem do Cloudinary: {}", publicId, exception);
        }
    }

    private static boolean formatoValido(String contentType, byte[] bytes) {
        return switch (contentType) {
            case "image/jpeg" -> bytes.length >= 3
                    && (bytes[0] & 0xFF) == 0xFF
                    && (bytes[1] & 0xFF) == 0xD8
                    && (bytes[2] & 0xFF) == 0xFF;
            case "image/png" -> bytes.length >= 8
                    && bytes[0] == (byte) 0x89 && bytes[1] == 0x50
                    && bytes[2] == 0x4E && bytes[3] == 0x47
                    && bytes[4] == 0x0D && bytes[5] == 0x0A
                    && bytes[6] == 0x1A && bytes[7] == 0x0A;
            case "image/webp" -> bytes.length >= 12
                    && ascii(bytes, 0, "RIFF") && ascii(bytes, 8, "WEBP");
            default -> false;
        };
    }

    private static boolean ascii(byte[] bytes, int offset, String value) {
        for (int i = 0; i < value.length(); i++) {
            if (bytes[offset + i] != (byte) value.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    private static BusinessException arquivoInvalido() {
        return new BusinessException(
                ErrorCode.ARQUIVO_INVALIDO,
                "Arquivo inválido. Envie uma imagem JPG, PNG ou WEBP de até 5 MB.");
    }
}
