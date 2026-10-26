package br.com.finduhouse.dto;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class DtosValidationTest {

    private static Validator validator;
    private static AutoCloseable validatorFactory;

    @BeforeAll
    static void setUp() {
        var factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        validatorFactory = factory;
    }

    @AfterAll
    static void tearDown() throws Exception {
        validatorFactory.close();
    }

    @Test
    void deveAceitarCadastroDeEstudanteValido() {
        var request = new EstudanteDtos.CadastroRequest(
                "Ana Souza",
                "ana@puc-campinas.edu.br",
                "+5519999990000",
                "MinhaSenha123",
                "Engenharia de Software");

        assertValid(request);
    }

    @Test
    void deveRejeitarCadastroDeEstudanteComCamposObrigatoriosInvalidos() {
        var request = new EstudanteDtos.CadastroRequest(
                "",
                "email-invalido",
                null,
                "",
                " ");

        var violations = violationsOf(request);

        assertTrue(hasViolation(violations, "nome"));
        assertTrue(hasViolation(violations, "email"));
        assertTrue(hasViolation(violations, "telefone"));
        assertTrue(hasViolation(violations, "senha"));
        assertTrue(hasViolation(violations, "curso"));
    }

    @Test
    void deveAceitarCadastroDeProprietarioValido() {
        var request = new ProprietarioDtos.CadastroRequest(
                "Maria Souza",
                "maria@email.com",
                "+5519999990000",
                "MinhaSenha123");

        assertValid(request);
    }

    @Test
    void deveRejeitarLoginSemEmailOuSenha() {
        var request = new AuthDtos.LoginRequest("email-invalido", "");

        var violations = violationsOf(request);

        assertTrue(hasViolation(violations, "email"));
        assertTrue(hasViolation(violations, "senha"));
    }

    @Test
    void deveAceitarCadastroDeVagaComRegrasValidas() {
        var request = new VagaDtos.CadastroRequest(
                7L,
                "Quarto próximo ao campus",
                "Quarto mobiliado",
                "Rua das Acácias, 123",
                new BigDecimal("1200.00"),
                3,
                "Silêncio após 22h",
                new BigDecimal("-22.905"),
                new BigDecimal("-47.060"),
                regrasValidas());

        assertValid(request);
    }

    @Test
    void deveRejeitarCadastroDeVagaComValorNegativoERegraAusente() {
        var request = new VagaDtos.CadastroRequest(
                7L,
                "Quarto",
                "Descrição",
                "Endereço",
                new BigDecimal("-1.00"),
                2,
                "Regra",
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                null);

        var violations = violationsOf(request);

        assertTrue(hasViolation(violations, "valorAluguel"));
        assertTrue(hasViolation(violations, "regras"));
    }

    @Test
    void deveAceitarQuestionarioCompleto() {
        var request = new QuestionarioDtos.SalvarRequest(42L, respostasValidas());

        assertValid(request);
    }

    @Test
    void deveRejeitarQuestionarioSemEstudanteEComRespostasAusentes() {
        var request = new QuestionarioDtos.SalvarRequest(null, null);

        var violations = violationsOf(request);

        assertTrue(hasViolation(violations, "estudanteId"));
        assertTrue(hasViolation(violations, "respostas"));
    }

    @Test
    void deveAceitarDenunciaValida() {
        var request = new DenunciaDtos.CriacaoRequest(
                17L,
                42L,
                DenunciaDtos.Motivo.GOLPE_FINANCEIRO,
                "O anúncio pede depósito antes da visita");

        assertValid(request);
    }

    @Test
    void deveRejeitarDenunciaSemDescricao() {
        var request = new DenunciaDtos.CriacaoRequest(
                17L,
                42L,
                DenunciaDtos.Motivo.OUTRO,
                " ");

        assertTrue(hasViolation(violationsOf(request), "descricao"));
    }

    @Test
    void deveAceitarFavoritoValido() {
        var request = new FavoritoDtos.CriacaoRequest(42L, 17L);

        assertValid(request);
    }

    @Test
    void deveRejeitarFavoritoComIdentificadoresNaoPositivos() {
        var request = new FavoritoDtos.CriacaoRequest(0L, -1L);

        var violations = violationsOf(request);

        assertTrue(hasViolation(violations, "estudanteId"));
        assertTrue(hasViolation(violations, "vagaId"));
    }

    private static VagaDtos.Regras regrasValidas() {
        return new VagaDtos.Regras(
                VagaDtos.HorarioDormir.ENTRE_22_E_0,
                VagaDtos.HorarioDormir.DEPOIS_DA_0,
                VagaDtos.HorarioAcordar.ENTRE_6_E_8,
                VagaDtos.HorarioAcordar.DEPOIS_DAS_8,
                VagaDtos.Nivel.ALTO,
                VagaDtos.Nivel.MEDIO,
                VagaDtos.ToleranciaRuido.MEDIA,
                VagaDtos.Frequencia.AS_VEZES,
                VagaDtos.Frequencia.AS_VEZES,
                false);
    }

    private static QuestionarioDtos.Respostas respostasValidas() {
        return new QuestionarioDtos.Respostas(
                VagaDtos.HorarioDormir.ENTRE_22_E_0,
                VagaDtos.HorarioDormir.DEPOIS_DA_0,
                VagaDtos.HorarioAcordar.ENTRE_6_E_8,
                VagaDtos.HorarioAcordar.DEPOIS_DAS_8,
                VagaDtos.Nivel.ALTO,
                VagaDtos.Nivel.MEDIO,
                VagaDtos.ToleranciaRuido.MEDIA,
                VagaDtos.Frequencia.AS_VEZES,
                VagaDtos.Frequencia.AS_VEZES,
                false);
    }

    private static void assertValid(Object value) {
        assertTrue(violationsOf(value).isEmpty());
    }

    private static Set<ConstraintViolation<Object>> violationsOf(Object value) {
        return validator.validate(value);
    }

    private static boolean hasViolation(
            Set<ConstraintViolation<Object>> violations,
            String property) {
        return violations.stream()
                .anyMatch(violation -> violation.getPropertyPath().toString().equals(property));
    }
}