package org.com.revenda.infrastructure.web.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes de validação para VenderVeiculoRequest")
class VenderVeiculoRequestValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Nested
    @DisplayName("Testes de validação de CPF do cliente")
    class ValidacaoCpfClienteTests {

        @Test
        @DisplayName("Deve aceitar CPF válido")
        void deveAceitarCpfValido() {
            // Arrange
            VenderVeiculoRequest request = new VenderVeiculoRequest(
                "12345678900", "João Silva", new BigDecimal("50000.00")
            );

            // Act
            Set<ConstraintViolation<VenderVeiculoRequest>> violations = validator.validate(request);

            // Assert
            assertTrue(violations.isEmpty());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"", "   ", "\t", "\n"})
        @DisplayName("Deve rejeitar CPF nulo ou vazio")
        void deveRejeitarCpfNuloOuVazio(String cpf) {
            // Arrange
            VenderVeiculoRequest request = new VenderVeiculoRequest(
                cpf, "João Silva", new BigDecimal("50000.00")
            );

            // Act
            Set<ConstraintViolation<VenderVeiculoRequest>> violations = validator.validate(request);

            // Assert
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("cpfCliente")));
        }
    }

    @Nested
    @DisplayName("Testes de validação de nome do cliente")
    class ValidacaoNomeClienteTests {

        @Test
        @DisplayName("Deve aceitar nome válido")
        void deveAceitarNomeValido() {
            // Arrange
            VenderVeiculoRequest request = new VenderVeiculoRequest(
                "12345678900", "João Silva", new BigDecimal("50000.00")
            );

            // Act
            Set<ConstraintViolation<VenderVeiculoRequest>> violations = validator.validate(request);

            // Assert
            assertTrue(violations.isEmpty());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"", "   ", "\t", "\n"})
        @DisplayName("Deve rejeitar nome nulo ou vazio")
        void deveRejeitarNomeNuloOuVazio(String nome) {
            // Arrange
            VenderVeiculoRequest request = new VenderVeiculoRequest(
                "12345678900", nome, new BigDecimal("50000.00")
            );

            // Act
            Set<ConstraintViolation<VenderVeiculoRequest>> violations = validator.validate(request);

            // Assert
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("nomeCliente")));
        }

        @ParameterizedTest
        @ValueSource(strings = {"João Silva", "Maria Santos", "José da Silva"})
        @DisplayName("Deve aceitar diferentes nomes válidos")
        void deveAceitarDiferentesNomesValidos(String nome) {
            // Arrange
            VenderVeiculoRequest request = new VenderVeiculoRequest(
                "12345678900", nome, new BigDecimal("50000.00")
            );

            // Act
            Set<ConstraintViolation<VenderVeiculoRequest>> violations = validator.validate(request);

            // Assert
            assertTrue(violations.isEmpty());
        }
    }

    @Nested
    @DisplayName("Testes de validação de valor da venda")
    class ValidacaoValorVendaTests {

        @Test
        @DisplayName("Deve rejeitar valor nulo")
        void deveRejeitarValorNulo() {
            // Arrange
            VenderVeiculoRequest request = new VenderVeiculoRequest(
                "12345678900", "João Silva", null
            );

            // Act
            Set<ConstraintViolation<VenderVeiculoRequest>> violations = validator.validate(request);

            // Assert
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("valorVenda")));
        }

        @ParameterizedTest
        @ValueSource(strings = {"0.01", "1000.00", "50000.00", "999999.99"})
        @DisplayName("Deve aceitar valores positivos")
        void deveAceitarValoresPositivos(String valor) {
            // Arrange
            VenderVeiculoRequest request = new VenderVeiculoRequest(
                "12345678900", "João Silva", new BigDecimal(valor)
            );

            // Act
            Set<ConstraintViolation<VenderVeiculoRequest>> violations = validator.validate(request);

            // Assert
            assertTrue(violations.isEmpty());
        }

        @ParameterizedTest
        @ValueSource(strings = {"0", "-1", "-100.00", "-999999.99"})
        @DisplayName("Deve rejeitar valores zero ou negativos")
        void deveRejeitarValoresZeroOuNegativos(String valor) {
            // Arrange
            VenderVeiculoRequest request = new VenderVeiculoRequest(
                "12345678900", "João Silva", new BigDecimal(valor)
            );

            // Act
            Set<ConstraintViolation<VenderVeiculoRequest>> violations = validator.validate(request);

            // Assert
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("valorVenda")));
        }
    }

    @Nested
    @DisplayName("Testes de múltiplos erros")
    class MultiplosErrosTests {

        @Test
        @DisplayName("Deve retornar múltiplos erros quando vários campos são inválidos")
        void deveRetornarMultiplosErrosQuandoVariosCamposSaoInvalidos() {
            // Arrange
            VenderVeiculoRequest request = new VenderVeiculoRequest(
                "", "", new BigDecimal("-100.00")
            );

            // Act
            Set<ConstraintViolation<VenderVeiculoRequest>> violations = validator.validate(request);

            // Assert
            assertFalse(violations.isEmpty());
            assertTrue(violations.size() >= 3);
        }
    }

    @Nested
    @DisplayName("Testes de igualdade e hashCode")
    class IgualdadeHashCodeTests {

        @Test
        @DisplayName("Deve considerar dois objetos iguais quando todos os campos são iguais")
        void deveConsiderarDoisObjetosIguaisQuandoTodosCamposSaoIguais() {
            // Arrange
            VenderVeiculoRequest request1 = new VenderVeiculoRequest(
                "12345678900", "João Silva", new BigDecimal("50000.00")
            );
            VenderVeiculoRequest request2 = new VenderVeiculoRequest(
                "12345678900", "João Silva", new BigDecimal("50000.00")
            );

            // Act & Assert
            assertEquals(request1, request2);
            assertEquals(request1.hashCode(), request2.hashCode());
        }

        @Test
        @DisplayName("Deve considerar dois objetos diferentes quando algum campo difere")
        void deveConsiderarDoisObjetosDiferentesQuandoAlgumCampoDifere() {
            // Arrange
            VenderVeiculoRequest request1 = new VenderVeiculoRequest(
                "12345678900", "João Silva", new BigDecimal("50000.00")
            );
            VenderVeiculoRequest request2 = new VenderVeiculoRequest(
                "98765432100", "Maria Santos", new BigDecimal("60000.00")
            );

            // Act & Assert
            assertNotEquals(request1, request2);
        }
    }

    @Nested
    @DisplayName("Testes de toString")
    class ToStringTests {

        @Test
        @DisplayName("Deve retornar representação em string com todos os campos")
        void deveRetornarRepresentacaoEmStringComTodosCampos() {
            // Arrange
            VenderVeiculoRequest request = new VenderVeiculoRequest(
                "12345678900", "João Silva", new BigDecimal("50000.00")
            );

            // Act
            String resultado = request.toString();

            // Assert
            assertNotNull(resultado);
            assertTrue(resultado.contains("12345678900"));
            assertTrue(resultado.contains("João Silva"));
            assertTrue(resultado.contains("50000.00"));
        }
    }
}

