package org.com.revenda.infrastructure.web.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes abrangentes para CadastrarVeiculoRequest")
class CadastrarVeiculoRequestValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Nested
    @DisplayName("Testes de validação de marca")
    class ValidacaoMarcaTests {

        @Test
        @DisplayName("Deve aceitar marca válida")
        void deveAceitarMarcaValida() {
            // Arrange
            CadastrarVeiculoRequest request = new CadastrarVeiculoRequest(
                "Toyota", "Corolla", 2023, new BigDecimal("75000.00"), "Branco", "Gasolina"
            );

            // Act
            Set<ConstraintViolation<CadastrarVeiculoRequest>> violations = validator.validate(request);

            // Assert
            assertTrue(violations.isEmpty());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"", "   ", "\t", "\n"})
        @DisplayName("Deve rejeitar marca nula ou vazia")
        void deveRejeitarMarcaNulaOuVazia(String marca) {
            // Arrange
            CadastrarVeiculoRequest request = new CadastrarVeiculoRequest(
                marca, "Corolla", 2023, new BigDecimal("75000.00"), "Branco", "Gasolina"
            );

            // Act
            Set<ConstraintViolation<CadastrarVeiculoRequest>> violations = validator.validate(request);

            // Assert
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("marca")));
        }

        @ParameterizedTest
        @ValueSource(strings = {"A", "AB", "Toyota", "Mercedes-Benz", "Volkswagen"})
        @DisplayName("Deve aceitar marcas de diferentes tamanhos")
        void deveAceitarMarcasDeDiferentesTamanhos(String marca) {
            // Arrange
            CadastrarVeiculoRequest request = new CadastrarVeiculoRequest(
                marca, "Modelo", 2023, new BigDecimal("75000.00"), "Branco", "Gasolina"
            );

            // Act
            Set<ConstraintViolation<CadastrarVeiculoRequest>> violations = validator.validate(request);

            // Assert
            assertTrue(violations.isEmpty());
        }
    }

    @Nested
    @DisplayName("Testes de validação de modelo")
    class ValidacaoModeloTests {

        @Test
        @DisplayName("Deve aceitar modelo válido")
        void deveAceitarModeloValido() {
            // Arrange
            CadastrarVeiculoRequest request = new CadastrarVeiculoRequest(
                "Toyota", "Corolla", 2023, new BigDecimal("75000.00"), "Branco", "Gasolina"
            );

            // Act
            Set<ConstraintViolation<CadastrarVeiculoRequest>> violations = validator.validate(request);

            // Assert
            assertTrue(violations.isEmpty());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"", "   ", "\t", "\n"})
        @DisplayName("Deve rejeitar modelo nulo ou vazio")
        void deveRejeitarModeloNuloOuVazio(String modelo) {
            // Arrange
            CadastrarVeiculoRequest request = new CadastrarVeiculoRequest(
                "Toyota", modelo, 2023, new BigDecimal("75000.00"), "Branco", "Gasolina"
            );

            // Act
            Set<ConstraintViolation<CadastrarVeiculoRequest>> violations = validator.validate(request);

            // Assert
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("modelo")));
        }

        @ParameterizedTest
        @ValueSource(strings = {"A", "AB", "Corolla", "Civic Type R", "Golf GTI Turbo"})
        @DisplayName("Deve aceitar modelos de diferentes tamanhos")
        void deveAceitarModelosDeDiferentesTamanhos(String modelo) {
            // Arrange
            CadastrarVeiculoRequest request = new CadastrarVeiculoRequest(
                "Toyota", modelo, 2023, new BigDecimal("75000.00"), "Branco", "Gasolina"
            );

            // Act
            Set<ConstraintViolation<CadastrarVeiculoRequest>> violations = validator.validate(request);

            // Assert
            assertTrue(violations.isEmpty());
        }
    }

    @Nested
    @DisplayName("Testes de validação de ano")
    class ValidacaoAnoTests {

        @Test
        @DisplayName("Deve rejeitar ano nulo")
        void deveRejeitarAnoNulo() {
            // Arrange
            CadastrarVeiculoRequest request = new CadastrarVeiculoRequest(
                "Toyota", "Corolla", null, new BigDecimal("75000.00"), "Branco", "Gasolina"
            );

            // Act
            Set<ConstraintViolation<CadastrarVeiculoRequest>> violations = validator.validate(request);

            // Assert
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("ano")));
        }

        @ParameterizedTest
        @ValueSource(ints = {1900, 1950, 2000, 2023, 2024})
        @DisplayName("Deve aceitar anos válidos")
        void deveAceitarAnosValidos(int ano) {
            // Arrange
            CadastrarVeiculoRequest request = new CadastrarVeiculoRequest(
                "Toyota", "Corolla", ano, new BigDecimal("75000.00"), "Branco", "Gasolina"
            );

            // Act
            Set<ConstraintViolation<CadastrarVeiculoRequest>> violations = validator.validate(request);

            // Assert
            assertTrue(violations.isEmpty());
        }
    }

    @Nested
    @DisplayName("Testes de validação de cor")
    class ValidacaoCorTests {

        @Test
        @DisplayName("Deve aceitar cor nula (campo opcional)")
        void deveAceitarCorNula() {
            // Arrange
            CadastrarVeiculoRequest request = new CadastrarVeiculoRequest(
                "Toyota", "Corolla", 2023, new BigDecimal("75000.00"), null, "Gasolina"
            );

            // Act
            Set<ConstraintViolation<CadastrarVeiculoRequest>> violations = validator.validate(request);

            // Assert
            assertTrue(violations.isEmpty());
        }

        @ParameterizedTest
        @ValueSource(strings = {"Branco", "Preto", "Prata", "Vermelho", "Azul"})
        @DisplayName("Deve aceitar cores válidas")
        void deveAceitarCoresValidas(String cor) {
            // Arrange
            CadastrarVeiculoRequest request = new CadastrarVeiculoRequest(
                "Toyota", "Corolla", 2023, new BigDecimal("75000.00"), cor, "Gasolina"
            );

            // Act
            Set<ConstraintViolation<CadastrarVeiculoRequest>> violations = validator.validate(request);

            // Assert
            assertTrue(violations.isEmpty());
        }
    }

    @Nested
    @DisplayName("Testes de validação de preço")
    class ValidacaoPrecoTests {

        @Test
        @DisplayName("Deve rejeitar preço nulo")
        void deveRejeitarPrecoNulo() {
            // Arrange
            CadastrarVeiculoRequest request = new CadastrarVeiculoRequest(
                "Toyota", "Corolla", 2023, null, "Branco", "Gasolina"
            );

            // Act
            Set<ConstraintViolation<CadastrarVeiculoRequest>> violations = validator.validate(request);

            // Assert
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("preco")));
        }

        @ParameterizedTest
        @ValueSource(strings = {"0.01", "1000.00", "75000.00", "999999.99"})
        @DisplayName("Deve aceitar preços positivos")
        void deveAceitarPrecosPositivos(String preco) {
            // Arrange
            CadastrarVeiculoRequest request = new CadastrarVeiculoRequest(
                "Toyota", "Corolla", 2023, new BigDecimal(preco), "Branco", "Gasolina"
            );

            // Act
            Set<ConstraintViolation<CadastrarVeiculoRequest>> violations = validator.validate(request);

            // Assert
            assertTrue(violations.isEmpty());
        }

        @ParameterizedTest
        @ValueSource(strings = {"0", "-1", "-100.00", "-999999.99"})
        @DisplayName("Deve rejeitar preços zero ou negativos")
        void deveRejeitarPrecosZeroOuNegativos(String preco) {
            // Arrange
            CadastrarVeiculoRequest request = new CadastrarVeiculoRequest(
                "Toyota", "Corolla", 2023, new BigDecimal(preco), "Branco", "Gasolina"
            );

            // Act
            Set<ConstraintViolation<CadastrarVeiculoRequest>> violations = validator.validate(request);

            // Assert
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("preco")));
        }

        @Test
        @DisplayName("Deve aceitar preços com casas decimais")
        void deveAceitarPrecosComCasasDecimais() {
            // Arrange
            CadastrarVeiculoRequest request = new CadastrarVeiculoRequest(
                "Toyota", "Corolla", 2023, new BigDecimal("75123.45"), "Branco", "Gasolina"
            );

            // Act
            Set<ConstraintViolation<CadastrarVeiculoRequest>> violations = validator.validate(request);

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
            CadastrarVeiculoRequest request = new CadastrarVeiculoRequest(
                "", "", null, null, "Branco", "Gasolina"
            );

            // Act
            Set<ConstraintViolation<CadastrarVeiculoRequest>> violations = validator.validate(request);

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
            CadastrarVeiculoRequest request1 = new CadastrarVeiculoRequest(
                "Toyota", "Corolla", 2023, new BigDecimal("75000.00"), "Branco", "Gasolina"
            );
            CadastrarVeiculoRequest request2 = new CadastrarVeiculoRequest(
                "Toyota", "Corolla", 2023, new BigDecimal("75000.00"), "Branco", "Gasolina"
            );

            // Act & Assert
            assertEquals(request1, request2);
            assertEquals(request1.hashCode(), request2.hashCode());
        }

        @Test
        @DisplayName("Deve considerar dois objetos diferentes quando algum campo difere")
        void deveConsiderarDoisObjetosDiferentesQuandoAlgumCampoDifere() {
            // Arrange
            CadastrarVeiculoRequest request1 = new CadastrarVeiculoRequest(
                "Toyota", "Corolla", 2023, new BigDecimal("75000.00"), "Branco", "Gasolina"
            );
            CadastrarVeiculoRequest request2 = new CadastrarVeiculoRequest(
                "Honda", "Civic", 2023, new BigDecimal("80000.00"), "Preto", "Flex"
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
            CadastrarVeiculoRequest request = new CadastrarVeiculoRequest(
                "Toyota", "Corolla", 2023, new BigDecimal("75000.00"), "Branco", "Gasolina"
            );

            // Act
            String resultado = request.toString();

            // Assert
            assertNotNull(resultado);
            assertTrue(resultado.contains("Toyota"));
            assertTrue(resultado.contains("Corolla"));
            assertTrue(resultado.contains("Branco"));
        }
    }
}
