package br.com.finduhouse.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Map;

import br.com.finduhouse.exception.BusinessException;
import br.com.finduhouse.exception.ErrorCode;
import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

@ExtendWith(MockitoExtension.class)
class CloudinaryArmazenamentoImagensTest {

    @Mock
    private Cloudinary cloudinary;

    @Mock
    private Uploader uploader;

    @Test
    void validaFormatosSuportados() {
        CloudinaryArmazenamentoImagens service = service("cloud", "key", "secret");

        assertDoesNotThrow(() -> service.validar(
                arquivo("image/jpeg", new byte[] {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF})));
        assertDoesNotThrow(() -> service.validar(
                arquivo("image/png", new byte[] {
                        (byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A})));
        assertDoesNotThrow(() -> service.validar(
                arquivo("image/webp", new byte[] {
                        'R', 'I', 'F', 'F', 0, 0, 0, 0, 'W', 'E', 'B', 'P'})));
    }

    @Test
    void rejeitaArquivoComContentTypeOuCabecalhoInvalido() {
        CloudinaryArmazenamentoImagens service = service("cloud", "key", "secret");

        assertCodigo(() -> service.validar(arquivo("text/plain", new byte[] {1, 2, 3})));
        assertCodigo(() -> service.validar(
                arquivo("image/png", new byte[] {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF})));
        assertCodigo(() -> service.validar(
                arquivo("image/jpeg", new byte[5 * 1024 * 1024 + 1])));
    }

    @Test
    void salvarEnviaPublicIdEOpcoesEDevolveResultado() throws Exception {
        CloudinaryArmazenamentoImagens service = service("cloud", "key", "secret");
        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.upload(any(byte[].class), anyMap()))
                .thenReturn(Map.of("secure_url", "https://cdn/foto.jpg", "public_id", "perfis/1"));

        ArmazenamentoImagens.ImagemArmazenada resultado = service.salvar(
                arquivo("image/jpeg", new byte[] {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF}),
                "perfis/1",
                true);

        assertEquals("https://cdn/foto.jpg", resultado.url());
        assertEquals("perfis/1", resultado.publicId());
        verify(uploader).upload(any(byte[].class), anyMap());
    }

    @Test
    void converteFalhaDoSdkEConfiguracaoAusente() throws Exception {
        CloudinaryArmazenamentoImagens service = service("cloud", "key", "secret");
        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.upload(any(byte[].class), anyMap())).thenThrow(new IOException("falha"));

        BusinessException falhaSdk = assertThrows(BusinessException.class, () -> service.salvar(
                arquivo("image/jpeg", new byte[] {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF}),
                "perfis/1",
                true));
        assertEquals(ErrorCode.FALHA_UPLOAD_IMAGEM, falhaSdk.getCodigo());

        BusinessException semConfiguracao = assertThrows(
                BusinessException.class,
                () -> service(" ", "key", "secret").salvar(
                        arquivo("image/jpeg", new byte[] {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF}),
                        "perfis/1",
                        true));
        assertEquals(ErrorCode.FALHA_UPLOAD_IMAGEM, semConfiguracao.getCodigo());
        verify(uploader, never()).destroy(any(), anyMap());
    }

    @Test
    void removerNaoPropagaFalhaDoSdk() throws Exception {
        CloudinaryArmazenamentoImagens service = service("cloud", "key", "secret");
        when(cloudinary.uploader()).thenReturn(uploader);
        doThrow(new IOException("falha")).when(uploader).destroy(any(), anyMap());

        assertDoesNotThrow(() -> service.remover("perfis/1"));
    }

    private CloudinaryArmazenamentoImagens service(
            String cloudName,
            String apiKey,
            String apiSecret) {
        return new CloudinaryArmazenamentoImagens(cloudinary, cloudName, apiKey, apiSecret);
    }

    private static MockMultipartFile arquivo(String contentType, byte[] bytes) {
        return new MockMultipartFile("foto", "foto", contentType, bytes);
    }

    private static void assertCodigo(Executable action) {
        BusinessException exception = assertThrows(BusinessException.class, action);
        assertEquals(ErrorCode.ARQUIVO_INVALIDO, exception.getCodigo());
    }
}
