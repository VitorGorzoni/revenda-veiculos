package org.com.revenda.infrastructure.web.dto.response;

import org.com.revenda.domain.enums.StatusVeiculo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes para VeiculoResponse")
class VeiculoResponseTest {

    @Nested
    @DisplayName("Testes de criação")
    class CriacaoTests {

        @Test
        @DisplayName("Deve criar VeiculoResponse com construtor completo")
        void deveCriarVeiculoResponseComConstrutorCompleto() {
            // Arrange
            LocalDateTime dataCadastro = LocalDateTime.now();

            // Act
            VeiculoResponse response = new VeiculoResponse(
                1L, "Toyota", "Corolla", 2023, "Branco",
                new BigDecimal("75000.00"), StatusVeiculo.DISPONIVEL, dataCadastro
            );

            // Assert
            assertNotNull(response);
            assertEquals(1L, response.getId());
            assertEquals("Toyota", response.getMarca());
            assertEquals("Corolla", response.getModelo());
            assertEquals(2023, response.getAno());
            assertEquals("Branco", response.getCor());
            assertEquals(new BigDecimal("75000.00"), response.getPreco());
            assertEquals(StatusVeiculo.DISPONIVEL, response.getStatus());
            assertEquals(dataCadastro, response.getDataCadastro());
        }

        @Test
        @DisplayName("Deve criar VeiculoResponse vazio com construtor sem argumentos")
        void deveCriarVeiculoResponseVazioComConstrutorSemArgumentos() {
            // Act
            VeiculoResponse response = new VeiculoResponse();

            // Assert
            assertNotNull(response);
            assertNull(response.getId());
            assertNull(response.getMarca());
            assertNull(response.getModelo());
            assertNull(response.getAno());
            assertNull(response.getCor());
            assertNull(response.getPreco());
            assertNull(response.getStatus());
            assertNull(response.getDataCadastro());
        }
    }

    @Nested
    @DisplayName("Testes de getters e setters")
    class GettersSettersTests {

        @Test
        @DisplayName("Deve permitir modificar todos os campos via setters")
        void devePermitirModificarTodosCamposViaSetters() {
            // Arrange
            VeiculoResponse response = new VeiculoResponse();
            LocalDateTime dataCadastro = LocalDateTime.now();

            // Act
            response.setId(1L);
            response.setMarca("Toyota");
            response.setModelo("Corolla");
            response.setAno(2023);
            response.setCor("Branco");
            response.setPreco(new BigDecimal("75000.00"));
            response.setStatus(StatusVeiculo.DISPONIVEL);
            response.setDataCadastro(dataCadastro);

            // Assert
            assertEquals(1L, response.getId());
            assertEquals("Toyota", response.getMarca());
            assertEquals("Corolla", response.getModelo());
            assertEquals(2023, response.getAno());
            assertEquals("Branco", response.getCor());
            assertEquals(new BigDecimal("75000.00"), response.getPreco());
            assertEquals(StatusVeiculo.DISPONIVEL, response.getStatus());
            assertEquals(dataCadastro, response.getDataCadastro());
        }
    }

    @Nested
    @DisplayName("Testes de igualdade e hashCode")
    class IgualdadeHashCodeTests {

        @Test
        @DisplayName("Deve considerar dois objetos iguais quando todos os campos são iguais")
        void deveConsiderarDoisObjetosIguaisQuandoTodosCamposSaoIguais() {
            // Arrange
            LocalDateTime dataCadastro = LocalDateTime.now();
            VeiculoResponse response1 = new VeiculoResponse(
                1L, "Toyota", "Corolla", 2023, "Branco",
                new BigDecimal("75000.00"), StatusVeiculo.DISPONIVEL, dataCadastro
            );
            VeiculoResponse response2 = new VeiculoResponse(
                1L, "Toyota", "Corolla", 2023, "Branco",
                new BigDecimal("75000.00"), StatusVeiculo.DISPONIVEL, dataCadastro
            );

            // Act & Assert
            assertEquals(response1, response2);
            assertEquals(response1.hashCode(), response2.hashCode());
        }

        @Test
        @DisplayName("Deve considerar dois objetos diferentes quando algum campo difere")
        void deveConsiderarDoisObjetosDiferentesQuandoAlgumCampoDifere() {
            // Arrange
            LocalDateTime dataCadastro = LocalDateTime.now();
            VeiculoResponse response1 = new VeiculoResponse(
                1L, "Toyota", "Corolla", 2023, "Branco",
                new BigDecimal("75000.00"), StatusVeiculo.DISPONIVEL, dataCadastro
            );
            VeiculoResponse response2 = new VeiculoResponse(
                2L, "Honda", "Civic", 2023, "Preto",
                new BigDecimal("80000.00"), StatusVeiculo.VENDIDO, dataCadastro
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
            LocalDateTime dataCadastro = LocalDateTime.now();
            VeiculoResponse response = new VeiculoResponse(
                1L, "Toyota", "Corolla", 2023, "Branco",
                new BigDecimal("75000.00"), StatusVeiculo.DISPONIVEL, dataCadastro
            );

            // Act
            String resultado = response.toString();

            // Assert
            assertNotNull(resultado);
            assertTrue(resultado.contains("Toyota"));
            assertTrue(resultado.contains("Corolla"));
            assertTrue(resultado.contains("Branco"));
        }
    }

    @Nested
    @DisplayName("Testes de diferentes status")
    class DiferentesStatusTests {

        @Test
        @DisplayName("Deve aceitar veículo com status DISPONIVEL")
        void deveAceitarVeiculoComStatusDisponivel() {
            // Arrange & Act
            VeiculoResponse response = new VeiculoResponse();
            response.setStatus(StatusVeiculo.DISPONIVEL);

            // Assert
            assertEquals(StatusVeiculo.DISPONIVEL, response.getStatus());
        }

        @Test
        @DisplayName("Deve aceitar veículo com status VENDIDO")
        void deveAceitarVeiculoComStatusVendido() {
            // Arrange & Act
            VeiculoResponse response = new VeiculoResponse();
            response.setStatus(StatusVeiculo.VENDIDO);

            // Assert
            assertEquals(StatusVeiculo.VENDIDO, response.getStatus());
        }

        @Test
        @DisplayName("Deve aceitar veículo com status RESERVADO")
        void deveAceitarVeiculoComStatusReservado() {
            // Arrange & Act
            VeiculoResponse response = new VeiculoResponse();
            response.setStatus(StatusVeiculo.RESERVADO);

            // Assert
            assertEquals(StatusVeiculo.RESERVADO, response.getStatus());
        }
    }
}

