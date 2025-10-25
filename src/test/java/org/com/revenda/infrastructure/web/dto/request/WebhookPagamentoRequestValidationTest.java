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

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes de validação para WebhookPagamentoRequest")
class WebhookPagamentoRequestValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Nested
    @DisplayName("Testes de validação de código de pagamento")
    class ValidacaoCodigoPagamentoTests {

        @Test
        @DisplayName("Deve aceitar código de pagamento válido")
        void deveAceitarCodigoPagamentoValido() {
            // Arrange
            WebhookPagamentoRequest request = new WebhookPagamentoRequest();
            request.setCodigoPagamento("PAG-ABC12345");
            request.setStatus("CONFIRMADO");

            // Act
            Set<ConstraintViolation<WebhookPagamentoRequest>> violations = validator.validate(request);

            // Assert
            assertTrue(violations.isEmpty());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"", "   ", "\t", "\n"})
        @DisplayName("Deve rejeitar código de pagamento nulo ou vazio")
        void deveRejeitarCodigoPagamentoNuloOuVazio(String codigo) {
            // Arrange
            WebhookPagamentoRequest request = new WebhookPagamentoRequest();
            request.setCodigoPagamento(codigo);
            request.setStatus("CONFIRMADO");

            // Act
            Set<ConstraintViolation<WebhookPagamentoRequest>> violations = validator.validate(request);

            // Assert
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("codigoPagamento")));
        }

        @ParameterizedTest
        @ValueSource(strings = {"PAG-123", "PAG-ABC12345", "PAYMENT-XYZ789"})
        @DisplayName("Deve aceitar diferentes códigos de pagamento válidos")
        void deveAceitarDiferentesCodigosPagamentoValidos(String codigo) {
            // Arrange
            WebhookPagamentoRequest request = new WebhookPagamentoRequest();
            request.setCodigoPagamento(codigo);
            request.setStatus("CONFIRMADO");

            // Act
            Set<ConstraintViolation<WebhookPagamentoRequest>> violations = validator.validate(request);

            // Assert
            assertTrue(violations.isEmpty());
        }
    }

    @Nested
    @DisplayName("Testes de validação de status")
    class ValidacaoStatusTests {

        @Test
        @DisplayName("Deve aceitar status válido")
        void deveAceitarStatusValido() {
            // Arrange
            WebhookPagamentoRequest request = new WebhookPagamentoRequest();
            request.setCodigoPagamento("PAG-ABC12345");
            request.setStatus("CONFIRMADO");

            // Act
            Set<ConstraintViolation<WebhookPagamentoRequest>> violations = validator.validate(request);

            // Assert
            assertTrue(violations.isEmpty());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"", "   ", "\t", "\n"})
        @DisplayName("Deve rejeitar status nulo ou vazio")
        void deveRejeitarStatusNuloOuVazio(String status) {
            // Arrange
            WebhookPagamentoRequest request = new WebhookPagamentoRequest();
            request.setCodigoPagamento("PAG-ABC12345");
            request.setStatus(status);

            // Act
            Set<ConstraintViolation<WebhookPagamentoRequest>> violations = validator.validate(request);

            // Assert
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("status")));
        }

        @ParameterizedTest
        @ValueSource(strings = {"CONFIRMADO", "CANCELADO", "PENDENTE"})
        @DisplayName("Deve aceitar diferentes status válidos")
        void deveAceitarDiferentesStatusValidos(String status) {
            // Arrange
            WebhookPagamentoRequest request = new WebhookPagamentoRequest();
            request.setCodigoPagamento("PAG-ABC12345");
            request.setStatus(status);

            // Act
            Set<ConstraintViolation<WebhookPagamentoRequest>> violations = validator.validate(request);

            // Assert
            assertTrue(violations.isEmpty());
        }
    }

    @Nested
    @DisplayName("Testes de múltiplos erros")
    class MultiplosErrosTests {

        @Test
        @DisplayName("Deve retornar múltiplos erros quando vários campos são inválidos")
        void deveRetornarMultiplosErrosQuandoVariosCamposSaoInvalidos() {
            // Arrange
            WebhookPagamentoRequest request = new WebhookPagamentoRequest();
            request.setCodigoPagamento("");
            request.setStatus("");

            // Act
            Set<ConstraintViolation<WebhookPagamentoRequest>> violations = validator.validate(request);

            // Assert
            assertFalse(violations.isEmpty());
            assertTrue(violations.size() >= 2);
        }
    }

    @Nested
    @DisplayName("Testes de igualdade e hashCode")
    class IgualdadeHashCodeTests {

        @Test
        @DisplayName("Deve considerar dois objetos iguais quando todos os campos são iguais")
        void deveConsiderarDoisObjetosIguaisQuandoTodosCamposSaoIguais() {
            // Arrange
            WebhookPagamentoRequest request1 = new WebhookPagamentoRequest();
            request1.setCodigoPagamento("PAG-ABC12345");
            request1.setStatus("CONFIRMADO");

            WebhookPagamentoRequest request2 = new WebhookPagamentoRequest();
            request2.setCodigoPagamento("PAG-ABC12345");
            request2.setStatus("CONFIRMADO");

            // Act & Assert
            assertEquals(request1, request2);
            assertEquals(request1.hashCode(), request2.hashCode());
        }

        @Test
        @DisplayName("Deve considerar dois objetos diferentes quando algum campo difere")
        void deveConsiderarDoisObjetosDiferentesQuandoAlgumCampoDifere() {
            // Arrange
            WebhookPagamentoRequest request1 = new WebhookPagamentoRequest();
            request1.setCodigoPagamento("PAG-ABC12345");
            request1.setStatus("CONFIRMADO");

            WebhookPagamentoRequest request2 = new WebhookPagamentoRequest();
            request2.setCodigoPagamento("PAG-XYZ789");
            request2.setStatus("CANCELADO");

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
            WebhookPagamentoRequest request = new WebhookPagamentoRequest();
            request.setCodigoPagamento("PAG-ABC12345");
            request.setStatus("CONFIRMADO");

            // Act
            String resultado = request.toString();

            // Assert
            assertNotNull(resultado);
            assertTrue(resultado.contains("PAG-ABC12345"));
            assertTrue(resultado.contains("CONFIRMADO"));
        }
    }

    @Nested
    @DisplayName("Testes de getters e setters")
    class GettersSettersTests {

        @Test
        @DisplayName("Deve permitir modificar código de pagamento via setter")
        void devePermitirModificarCodigoPagamentoViaSetter() {
            // Arrange
            WebhookPagamentoRequest request = new WebhookPagamentoRequest();

            // Act
            request.setCodigoPagamento("PAG-123");

            // Assert
            assertEquals("PAG-123", request.getCodigoPagamento());
        }

        @Test
        @DisplayName("Deve permitir modificar status via setter")
        void devePermitirModificarStatusViaSetter() {
            // Arrange
            WebhookPagamentoRequest request = new WebhookPagamentoRequest();

            // Act
            request.setStatus("CONFIRMADO");

            // Assert
            assertEquals("CONFIRMADO", request.getStatus());
        }
    }
}

