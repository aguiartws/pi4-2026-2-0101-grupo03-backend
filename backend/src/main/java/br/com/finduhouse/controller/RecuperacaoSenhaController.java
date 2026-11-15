package br.com.finduhouse.controller;

import br.com.finduhouse.dto.RecuperacaoSenhaDtos;
import br.com.finduhouse.service.RecuperacaoSenhaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class RecuperacaoSenhaController {

    private final RecuperacaoSenhaService recuperacaoSenhaService;

    public RecuperacaoSenhaController(RecuperacaoSenhaService recuperacaoSenhaService) {
        this.recuperacaoSenhaService = recuperacaoSenhaService;
    }

    @PostMapping("/solicitar-recuperacao")
    public ResponseEntity<RecuperacaoSenhaDtos.MensagemResponse> solicitarCodigo(
            @Valid @RequestBody RecuperacaoSenhaDtos.SolicitarRequest request) {
        recuperacaoSenhaService.solicitarCodigo(request.email());
        return ResponseEntity.ok(new RecuperacaoSenhaDtos.MensagemResponse(
                "Se o e-mail estiver cadastrado, você receberá um código."));
    }

    @PostMapping("/verificar-codigo")
    public ResponseEntity<RecuperacaoSenhaDtos.TokenRecuperacaoResponse> verificarCodigo(
            @Valid @RequestBody RecuperacaoSenhaDtos.VerificarRequest request) {
        String token = recuperacaoSenhaService.verificarCodigo(request.email(), request.codigo());
        return ResponseEntity.ok(new RecuperacaoSenhaDtos.TokenRecuperacaoResponse(token));
    }

    @PostMapping("/redefinir-senha")
    public ResponseEntity<RecuperacaoSenhaDtos.MensagemResponse> redefinirSenha(
            @Valid @RequestBody RecuperacaoSenhaDtos.RedefinirRequest request) {
        recuperacaoSenhaService.redefinirSenha(request.tokenRecuperacao(), request.novaSenha());
        return ResponseEntity.ok(new RecuperacaoSenhaDtos.MensagemResponse(
                "Senha alterada com sucesso."));
    }
}
