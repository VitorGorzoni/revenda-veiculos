package org.com.revenda.infrastructure.web.dto.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes para VeiculoVendidoResponse")
class VeiculoVendidoResponseTest {

    @Nested
    @DisplayName("Testes de criação")
    class CriacaoTests {

        @Test
        @DisplayName("Deve criar VeiculoVendidoResponse com construtor completo")
        void deveCriarVeiculoVendidoResponseComConstrutorCompleto() {
            // Arrange
            LocalDateTime dataVenda = LocalDateTime.now();

            // Act
            VeiculoVendidoResponse response = new VeiculoVendidoResponse(
                1L, "12345678900", dataVenda, "Toyota", "Corolla",
                2023, "Branco", new BigDecimal("75000.00")
            );

            // Assert
            assertNotNull(response);
            assertEquals(1L, response.getVendaId());
            assertEquals("12345678900", response.getCpfComprador());
            assertEquals(dataVenda, response.getDataVenda());
            assertEquals("Toyota", response.getMarca());
            assertEquals("Corolla", response.getModelo());
            assertEquals(2023, response.getAno());
            assertEquals("Branco", response.getCor());
            assertEquals(new BigDecimal("75000.00"), response.getPreco());
        }

        @Test
        @DisplayName("Deve criar VeiculoVendidoResponse vazia com construtor sem argumentos")
        void deveCriarVeiculoVendidoResponseVaziaComConstrutorSemArgumentos() {
            // Act
            VeiculoVendidoResponse response = new VeiculoVendidoResponse();

            // Assert
            assertNotNull(response);
            assertNull(response.getVendaId());
            assertNull(response.getCpfComprador());
            assertNull(response.getDataVenda());
            assertNull(response.getMarca());
            assertNull(response.getModelo());
            assertNull(response.getAno());
            assertNull(response.getCor());
            assertNull(response.getPreco());
        }
    }

    @Nested
    @DisplayName("Testes de getters e setters")
    class GettersSettersTests {

        @Test
        @DisplayName("Deve permitir modificar todos os campos via setters")
        void devePermitirModificarTodosCamposViaSetters() {
            // Arrange
            VeiculoVendidoResponse response = new VeiculoVendidoResponse();
            LocalDateTime dataVenda = LocalDateTime.now();

            // Act
            response.setVendaId(1L);
            response.setCpfComprador("12345678900");
            response.setDataVenda(dataVenda);
            response.setMarca("Toyota");
            response.setModelo("Corolla");
            response.setAno(2023);
            response.setCor("Branco");
            response.setPreco(new BigDecimal("75000.00"));

            // Assert
            assertEquals(1L, response.getVendaId());
            assertEquals("12345678900", response.getCpfComprador());
            assertEquals(dataVenda, response.getDataVenda());
            assertEquals("Toyota", response.getMarca());
            assertEquals("Corolla", response.getModelo());
            assertEquals(2023, response.getAno());
            assertEquals("Branco", response.getCor());
            assertEquals(new BigDecimal("75000.00"), response.getPreco());
        }
    }

    @Nested
    @DisplayName("Testes de igualdade e hashCode")
    class IgualdadeHashCodeTests {

        @Test
        @DisplayName("Deve considerar dois objetos iguais quando todos os campos são iguais")
        void deveConsiderarDoisObjetosIguaisQuandoTodosCamposSaoIguais() {
            // Arrange
            LocalDateTime dataVenda = LocalDateTime.now();
            VeiculoVendidoResponse response1 = new VeiculoVendidoResponse(
                1L, "12345678900", dataVenda, "Toyota", "Corolla",
                2023, "Branco", new BigDecimal("75000.00")
            );
            VeiculoVendidoResponse response2 = new VeiculoVendidoResponse(
                1L, "12345678900", dataVenda, "Toyota", "Corolla",
                2023, "Branco", new BigDecimal("75000.00")
            );

            // Act & Assert
            assertEquals(response1, response2);
            assertEquals(response1.hashCode(), response2.hashCode());
        }

        @Test
        @DisplayName("Deve considerar dois objetos diferentes quando algum campo difere")
        void deveConsiderarDoisObjetosDiferentesQuandoAlgumCampoDifere() {
            // Arrange
            LocalDateTime dataVenda = LocalDateTime.now();
            VeiculoVendidoResponse response1 = new VeiculoVendidoResponse(
                1L, "12345678900", dataVenda, "Toyota", "Corolla",
                2023, "Branco", new BigDecimal("75000.00")
            );
            VeiculoVendidoResponse response2 = new VeiculoVendidoResponse(
                2L, "98765432100", dataVenda, "Honda", "Civic",
                2023, "Preto", new BigDecimal("80000.00")
            );

            // Act & Assert
            assertNotEquals(response1, response2);
        }
    }

    @Nested
    @DisplayName("Testes de toString")
    class ToStringTests {

        @Test
        @DisplayName("Deve retornar representação em string com todos os campos")
        void deveRetornarRepresentacaoEmStringComTodosCampos() {
            // Arrange
            LocalDateTime dataVenda = LocalDateTime.now();
            VeiculoVendidoResponse response = new VeiculoVendidoResponse(
                1L, "12345678900", dataVenda, "Toyota", "Corolla",
                2023, "Branco", new BigDecimal("75000.00")
            );

            // Act
            String resultado = response.toString();

            // Assert
            assertNotNull(resultado);
            assertTrue(resultado.contains("12345678900"));
            assertTrue(resultado.contains("Toyota"));
            assertTrue(resultado.contains("Corolla"));
        }
    }

    @Nested
    @DisplayName("Testes com diferentes veículos")
    class DiferentesVeiculosTests {

        @Test
        @DisplayName("Deve aceitar veículo vendido Toyota")
        void deveAceitarVeiculoVendidoToyota() {
            // Arrange & Act
            VeiculoVendidoResponse response = new VeiculoVendidoResponse();
            response.setMarca("Toyota");
            response.setModelo("Corolla");

            // Assert
            assertEquals("Toyota", response.getMarca());
            assertEquals("Corolla", response.getModelo());
        }

        @Test
        @DisplayName("Deve aceitar veículo vendido Honda")
        void deveAceitarVeiculoVendidoHonda() {
            // Arrange & Act
            VeiculoVendidoResponse response = new VeiculoVendidoResponse();
            response.setMarca("Honda");
            response.setModelo("Civic");

            // Assert
            assertEquals("Honda", response.getMarca());
            assertEquals("Civic", response.getModelo());
        }

        @Test
        @DisplayName("Deve aceitar veículo vendido Volkswagen")
        void deveAceitarVeiculoVendidoVolkswagen() {
            // Arrange & Act
            VeiculoVendidoResponse response = new VeiculoVendidoResponse();
            response.setMarca("Volkswagen");
            response.setModelo("Golf");

            // Assert
            assertEquals("Volkswagen", response.getMarca());
            assertEquals("Golf", response.getModelo());
        }
    }

    @Nested
    @DisplayName("Testes de diferentes anos")
    class DiferentesAnosTests {

        @Test
        @DisplayName("Deve aceitar veículo de ano recente")
        void deveAceitarVeiculoAnoRecente() {
            // Arrange & Act
            VeiculoVendidoResponse response = new VeiculoVendidoResponse();
            response.setAno(2024);

            // Assert
            assertEquals(2024, response.getAno());
        }

        @Test
        @DisplayName("Deve aceitar veículo de ano anterior")
        void deveAceitarVeiculoAnoAnterior() {
            // Arrange & Act
            VeiculoVendidoResponse response = new VeiculoVendidoResponse();
            response.setAno(2020);

            // Assert
            assertEquals(2020, response.getAno());
        }
    }
}

