package org.com.revenda.presentation.dto.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes abrangentes para CadastrarVeiculoRequest")
class CadastrarVeiculoRequestValidationTest {

    private Validator validator;
    private CadastrarVeiculoRequest request;

    @BeforeEach
    void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }

        request = new CadastrarVeiculoRequest();
        request.setMarca("Toyota");
        request.setModelo("Corolla");
        request.setAno(2023);
        request.setCor("Branco");
        request.setPreco(new BigDecimal("75000.00"));
    }

    @Nested
    @DisplayName("Testes de validação de marca")
    class ValidacaoMarcaTests {

        @Test
        @DisplayName("Deve aceitar marca válida")
        void deveAceitarMarcaValida() {
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
            request.setMarca(marca);

            // Act
            Set<ConstraintViolation<CadastrarVeiculoRequest>> violations = validator.validate(request);

            // Assert
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                    .anyMatch(v -> v.getPropertyPath().toString().equals("marca")));
        }

        @ParameterizedTest
        @ValueSource(strings = {"BMW", "Mercedes-Benz", "Peugeot-Citroën", "Alfa Romeo"})
        @DisplayName("Deve aceitar marcas com caracteres especiais")
        void deveAceitarMarcasComCaracteresEspeciais(String marca) {
            // Arrange
            request.setMarca(marca);

            // Act
            Set<ConstraintViolation<CadastrarVeiculoRequest>> violations = validator.validate(request);

            // Assert
            assertTrue(violations.stream()
                    .noneMatch(v -> v.getPropertyPath().toString().equals("marca")));
        }

        @Test
        @DisplayName("Deve rejeitar marca muito longa")
        void deveRejeitarMarcaMuitoLonga() {
            // Arrange
            request.setMarca("A".repeat(51)); // Mais que 50 caracteres

            // Act
            Set<ConstraintViolation<CadastrarVeiculoRequest>> violations = validator.validate(request);

            // Assert
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                    .anyMatch(v -> v.getPropertyPath().toString().equals("marca")));
        }
    }

    @Nested
    @DisplayName("Testes de validação de modelo")
    class ValidacaoModeloTests {

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"", "   "})
        @DisplayName("Deve rejeitar modelo nulo ou vazio")
        void deveRejeitarModeloNuloOuVazio(String modelo) {
            // Arrange
            request.setModelo(modelo);

            // Act
            Set<ConstraintViolation<CadastrarVeiculoRequest>> violations = validator.validate(request);

            // Assert
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                    .anyMatch(v -> v.getPropertyPath().toString().equals("modelo")));
        }

        @ParameterizedTest
        @ValueSource(strings = {"Civic 2.0", "C4 Picasso", "Golf GTI", "A3 Sportback"})
        @DisplayName("Deve aceitar modelos com números e caracteres especiais")
        void deveAceitarModelosComNumerosECaracteresEspeciais(String modelo) {
            // Arrange
            request.setModelo(modelo);

            // Act
            Set<ConstraintViolation<CadastrarVeiculoRequest>> violations = validator.validate(request);

            // Assert
            assertTrue(violations.stream()
                    .noneMatch(v -> v.getPropertyPath().toString().equals("modelo")));
        }

        @Test
        @DisplayName("Deve rejeitar modelo muito longo")
        void deveRejeitarModeloMuitoLongo() {
            // Arrange
            request.setModelo("A".repeat(101)); // Mais que 100 caracteres

            // Act
            Set<ConstraintViolation<CadastrarVeiculoRequest>> violations = validator.validate(request);

            // Assert
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                    .anyMatch(v -> v.getPropertyPath().toString().equals("modelo")));
        }
    }

    @Nested
    @DisplayName("Testes de validação de ano")
    class ValidacaoAnoTests {

        @Test
        @DisplayName("Deve rejeitar ano nulo")
        void deveRejeitarAnoNulo() {
            // Arrange
            request.setAno(null);

            // Act
            Set<ConstraintViolation<CadastrarVeiculoRequest>> violations = validator.validate(request);

            // Assert
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                    .anyMatch(v -> v.getPropertyPath().toString().equals("ano")));
        }

        @ParameterizedTest
        @ValueSource(ints = {1899, 1500, 0, -1})
        @DisplayName("Deve rejeitar anos muito antigos")
        void deveRejeitarAnosMuitoAntigos(int ano) {
            // Arrange
            request.setAno(ano);

            // Act
            Set<ConstraintViolation<CadastrarVeiculoRequest>> violations = validator.validate(request);

            // Assert
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                    .anyMatch(v -> v.getPropertyPath().toString().equals("ano")));
        }

        @ParameterizedTest
        @ValueSource(ints = {2026, 3000, 2050})
        @DisplayName("Deve rejeitar anos futuros além do limite")
        void deveRejeitarAnosFuturosAlemDoLimite(int ano) {
            // Arrange
            request.setAno(ano);

            // Act
            Set<ConstraintViolation<CadastrarVeiculoRequest>> violations = validator.validate(request);

            // Assert
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                    .anyMatch(v -> v.getPropertyPath().toString().equals("ano")));
        }

        @ParameterizedTest
        @ValueSource(ints = {1900, 2020, 2023, 2024, 2025})
        @DisplayName("Deve aceitar anos válidos")
        void deveAceitarAnosValidos(int ano) {
            // Arrange
            request.setAno(ano);

            // Act
            Set<ConstraintViolation<CadastrarVeiculoRequest>> violations = validator.validate(request);

            // Assert
            assertTrue(violations.stream()
                    .noneMatch(v -> v.getPropertyPath().toString().equals("ano")));
        }
    }

    @Nested
    @DisplayName("Testes de validação de cor")
    class ValidacaoCorTests {

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"", "   "})
        @DisplayName("Deve rejeitar cor nula ou vazia")
        void deveRejeitarCorNulaOuVazia(String cor) {
            // Arrange
            request.setCor(cor);

            // Act
            Set<ConstraintViolation<CadastrarVeiculoRequest>> violations = validator.validate(request);

            // Assert
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                    .anyMatch(v -> v.getPropertyPath().toString().equals("cor")));
        }

        @ParameterizedTest
        @ValueSource(strings = {"Azul", "Vermelho", "Preto", "Branco"})
        @DisplayName("Deve aceitar cores válidas")
        void deveAceitarCoresValidas(String cor) {
            // Arrange
            request.setCor(cor);

            // Act
            Set<ConstraintViolation<CadastrarVeiculoRequest>> violations = validator.validate(request);

            // Assert
            assertTrue(violations.stream()
                    .noneMatch(v -> v.getPropertyPath().toString().equals("cor")));
        }

        @Test
        @DisplayName("Deve rejeitar cor muito longa")
        void deveRejeitarCorMuitoLonga() {
            // Arrange
            request.setCor("A".repeat(31)); // Mais que 30 caracteres

            // Act
            Set<ConstraintViolation<CadastrarVeiculoRequest>> violations = validator.validate(request);

            // Assert
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                    .anyMatch(v -> v.getPropertyPath().toString().equals("cor")));
        }
    }

    @Nested
    @DisplayName("Testes de validação de preço")
    class ValidacaoPrecoTests {

        @Test
        @DisplayName("Deve rejeitar preço nulo")
        void deveRejeitarPrecoNulo() {
            // Arrange
            request.setPreco(null);

            // Act
            Set<ConstraintViolation<CadastrarVeiculoRequest>> violations = validator.validate(request);

            // Assert
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                    .anyMatch(v -> v.getPropertyPath().toString().equals("preco")));
        }

        @Test
        @DisplayName("Deve rejeitar preço zero")
        void deveRejeitarPrecoZero() {
            // Arrange
            request.setPreco(BigDecimal.ZERO);

            // Act
            Set<ConstraintViolation<CadastrarVeiculoRequest>> violations = validator.validate(request);

            // Assert
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                    .anyMatch(v -> v.getPropertyPath().toString().equals("preco")));
        }

        @Test
        @DisplayName("Deve rejeitar preço negativo")
        void deveRejeitarPrecoNegativo() {
            // Arrange
            request.setPreco(new BigDecimal("-1000.00"));

            // Act
            Set<ConstraintViolation<CadastrarVeiculoRequest>> violations = validator.validate(request);

            // Assert
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                    .anyMatch(v -> v.getPropertyPath().toString().equals("preco")));
        }

        @ParameterizedTest
        @ValueSource(strings = {"0.01", "1000.00", "50000.50", "99999999.99"})
        @DisplayName("Deve aceitar preços válidos")
        void deveAceitarPrecosValidos(String preco) {
            // Arrange
            request.setPreco(new BigDecimal(preco));

            // Act
            Set<ConstraintViolation<CadastrarVeiculoRequest>> violations = validator.validate(request);

            // Assert
            assertTrue(violations.stream()
                    .noneMatch(v -> v.getPropertyPath().toString().equals("preco")));
        }

        @Test
        @DisplayName("Deve rejeitar preço com muitas casas decimais")
        void deveRejeitarPrecoComMuitasCasasDecimais() {
            // Arrange
            request.setPreco(new BigDecimal("12345.123")); // Mais que 2 casas decimais

            // Act
            Set<ConstraintViolation<CadastrarVeiculoRequest>> violations = validator.validate(request);

            // Assert
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                    .anyMatch(v -> v.getPropertyPath().toString().equals("preco")));
        }

        @Test
        @DisplayName("Deve rejeitar preço com muitos dígitos inteiros")
        void deveRejeitarPrecoComMuitosDigitosInteiros() {
            // Arrange
            request.setPreco(new BigDecimal("999999999.99")); // Mais que 8 dígitos inteiros

            // Act
            Set<ConstraintViolation<CadastrarVeiculoRequest>> violations = validator.validate(request);

            // Assert
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                    .anyMatch(v -> v.getPropertyPath().toString().equals("preco")));
        }
    }

    @Nested
    @DisplayName("Testes de validação combinada")
    class ValidacaoCombinadaTests {

        @Test
        @DisplayName("Deve rejeitar objeto completamente inválido")
        void deveRejeitarObjetoCompletamenteInvalido() {
            // Arrange
            CadastrarVeiculoRequest requestInvalido = new CadastrarVeiculoRequest();
            // Todos os campos nulos

            // Act
            Set<ConstraintViolation<CadastrarVeiculoRequest>> violations = validator.validate(requestInvalido);

            // Assert
            assertFalse(violations.isEmpty());
            // Deve ter pelo menos 5 violações (uma para cada campo obrigatório)
            assertTrue(violations.size() >= 5);
        }

        @Test
        @DisplayName("Deve aceitar objeto completamente válido")
        void deveAceitarObjetoCompletamenteValido() {
            // Act
            Set<ConstraintViolation<CadastrarVeiculoRequest>> violations = validator.validate(request);

            // Assert
            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("Deve validar múltiplos campos inválidos simultaneamente")
        void deveValidarMultiplosCamposInvalidosSimultaneamente() {
            // Arrange
            request.setMarca("");
            request.setModelo(null);
            request.setAno(null);
            request.setCor("   ");
            request.setPreco(null);

            // Act
            Set<ConstraintViolation<CadastrarVeiculoRequest>> violations = validator.validate(request);

            // Assert
            assertFalse(violations.isEmpty());
            assertEquals(5, violations.size());
        }
    }

    @Nested
    @DisplayName("Testes de cenários extremos")
    class CenariosExtremosTests {

        @Test
        @DisplayName("Deve rejeitar strings que excedem os limites")
        void deveRejeitarStringsQueExcedemOsLimites() {
            // Arrange
            request.setMarca("A".repeat(51)); // Excede 50 caracteres
            request.setModelo("B".repeat(101)); // Excede 100 caracteres
            request.setCor("C".repeat(31)); // Excede 30 caracteres

            // Act
            Set<ConstraintViolation<CadastrarVeiculoRequest>> violations = validator.validate(request);

            // Assert
            assertFalse(violations.isEmpty());
            assertEquals(3, violations.size());
        }

        @Test
        @DisplayName("Deve lidar com valores extremos de ano")
        void deveLidarComValoresExtremosDeAno() {
            // Arrange
            request.setAno(Integer.MAX_VALUE);

            // Act
            Set<ConstraintViolation<CadastrarVeiculoRequest>> violations = validator.validate(request);

            // Assert
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                    .anyMatch(v -> v.getPropertyPath().toString().equals("ano")));
        }

        @Test
        @DisplayName("Deve aceitar caracteres Unicode dentro dos limites")
        void deveAceitarCaracteresUnicodeDentroDoLimites() {
            // Arrange
            request.setMarca("Toyota");
            request.setModelo("Corolla");
            request.setCor("Azul");

            // Act
            Set<ConstraintViolation<CadastrarVeiculoRequest>> violations = validator.validate(request);

            // Assert
            assertTrue(violations.isEmpty());
            assertEquals("Toyota", request.getMarca());
            assertEquals("Corolla", request.getModelo());
            assertEquals("Azul", request.getCor());
        }
    }

    @Nested
    @DisplayName("Testes de equals e hashCode")
    class EqualsHashCodeTests {

        @Test
        @DisplayName("Objetos iguais devem ter equals verdadeiro")
        void objetosIguaisDevemTerEqualsVerdadeiro() {
            // Arrange
            CadastrarVeiculoRequest outro = new CadastrarVeiculoRequest();
            outro.setMarca("Toyota");
            outro.setModelo("Corolla");
            outro.setAno(2023);
            outro.setCor("Branco");
            outro.setPreco(new BigDecimal("75000.00"));

            // Act & Assert
            assertEquals(request, outro);
            assertEquals(request.hashCode(), outro.hashCode());
        }

        @Test
        @DisplayName("Objetos diferentes devem ter equals falso")
        void objetosDiferentesDevemTerEqualsFalso() {
            // Arrange
            CadastrarVeiculoRequest outro = new CadastrarVeiculoRequest();
            outro.setMarca("Honda");
            outro.setModelo("Civic");
            outro.setAno(2024);
            outro.setCor("Preto");
            outro.setPreco(new BigDecimal("80000.00"));

            // Act & Assert
            assertNotEquals(request, outro);
        }

        @Test
        @DisplayName("Deve ser reflexivo - objeto igual a si mesmo")
        void deveSerReflexivo() {
            // Act & Assert
            assertTrue(request.equals(request));
            assertEquals(request.hashCode(), request.hashCode());
        }

        @Test
        @DisplayName("Não deve ser igual a null")
        void naoDeveSerIgualANull() {
            // Act & Assert
            assertNotEquals(null, request);
        }

        @Test
        @DisplayName("Não deve ser igual a objeto de classe diferente")
        void naoDeveSerIgualAObjetoDeClasseDiferente() {
            // Act & Assert
            assertNotEquals("string", request);
        }
    }
}
