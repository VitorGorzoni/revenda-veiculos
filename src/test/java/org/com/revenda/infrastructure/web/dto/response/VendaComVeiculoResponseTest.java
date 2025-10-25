package org.com.revenda.infrastructure.web.dto.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes para VendaComVeiculoResponse")
class VendaComVeiculoResponseTest {

    @Nested
    @DisplayName("Testes de criação")
    class CriacaoTests {

        @Test
        @DisplayName("Deve criar VendaComVeiculoResponse com construtor completo")
        void deveCriarVendaComVeiculoResponseComConstrutorCompleto() {
            // Arrange
            LocalDateTime dataVenda = LocalDateTime.now();

            // Act
            VendaComVeiculoResponse response = new VendaComVeiculoResponse(
                1L, "PAG-ABC12345", "12345678900", "João Silva",
                new BigDecimal("75000.00"), "PENDENTE", dataVenda,
                1L, "Toyota", "Corolla", 2023, "Branco", new BigDecimal("75000.00")
            );

            // Assert
            assertNotNull(response);
            assertEquals(1L, response.getVendaId());
            assertEquals("PAG-ABC12345", response.getCodigoPagamento());
            assertEquals("12345678900", response.getCpfCliente());
            assertEquals("João Silva", response.getNomeCliente());
            assertEquals(new BigDecimal("75000.00"), response.getValorVenda());
            assertEquals("PENDENTE", response.getStatusPagamento());
            assertEquals(dataVenda, response.getDataVenda());
            assertEquals(1L, response.getVeiculoId());
            assertEquals("Toyota", response.getMarca());
            assertEquals("Corolla", response.getModelo());
            assertEquals(2023, response.getAno());
            assertEquals("Branco", response.getCor());
            assertEquals(new BigDecimal("75000.00"), response.getPrecoOriginal());
        }

        @Test
        @DisplayName("Deve criar VendaComVeiculoResponse vazia com construtor sem argumentos")
        void deveCriarVendaComVeiculoResponseVaziaComConstrutorSemArgumentos() {
            // Act
            VendaComVeiculoResponse response = new VendaComVeiculoResponse();

            // Assert
            assertNotNull(response);
            assertNull(response.getVendaId());
            assertNull(response.getCodigoPagamento());
            assertNull(response.getCpfCliente());
            assertNull(response.getNomeCliente());
            assertNull(response.getValorVenda());
            assertNull(response.getStatusPagamento());
            assertNull(response.getDataVenda());
            assertNull(response.getVeiculoId());
            assertNull(response.getMarca());
            assertNull(response.getModelo());
            assertNull(response.getAno());
            assertNull(response.getCor());
            assertNull(response.getPrecoOriginal());
        }
    }

    @Nested
    @DisplayName("Testes de getters e setters")
    class GettersSettersTests {

        @Test
        @DisplayName("Deve permitir modificar campos de venda via setters")
        void devePermitirModificarCamposVendaViaSetters() {
            // Arrange
            VendaComVeiculoResponse response = new VendaComVeiculoResponse();
            LocalDateTime dataVenda = LocalDateTime.now();

            // Act
            response.setVendaId(1L);
            response.setCodigoPagamento("PAG-ABC12345");
            response.setCpfCliente("12345678900");
            response.setNomeCliente("João Silva");
            response.setValorVenda(new BigDecimal("75000.00"));
            response.setStatusPagamento("PENDENTE");
            response.setDataVenda(dataVenda);

            // Assert
            assertEquals(1L, response.getVendaId());
            assertEquals("PAG-ABC12345", response.getCodigoPagamento());
            assertEquals("12345678900", response.getCpfCliente());
            assertEquals("João Silva", response.getNomeCliente());
            assertEquals(new BigDecimal("75000.00"), response.getValorVenda());
            assertEquals("PENDENTE", response.getStatusPagamento());
            assertEquals(dataVenda, response.getDataVenda());
        }

        @Test
        @DisplayName("Deve permitir modificar campos de veículo via setters")
        void devePermitirModificarCamposVeiculoViaSetters() {
            // Arrange
            VendaComVeiculoResponse response = new VendaComVeiculoResponse();

            // Act
            response.setVeiculoId(1L);
            response.setMarca("Toyota");
            response.setModelo("Corolla");
            response.setAno(2023);
            response.setCor("Branco");
            response.setPrecoOriginal(new BigDecimal("75000.00"));

            // Assert
            assertEquals(1L, response.getVeiculoId());
            assertEquals("Toyota", response.getMarca());
            assertEquals("Corolla", response.getModelo());
            assertEquals(2023, response.getAno());
            assertEquals("Branco", response.getCor());
            assertEquals(new BigDecimal("75000.00"), response.getPrecoOriginal());
        }
    }

    @Nested
    @DisplayName("Testes de dados completos")
    class DadosCompletosTests {

        @Test
        @DisplayName("Deve conter todos os dados de venda e veículo")
        void deveConterTodosDadosVendaEVeiculo() {
            // Arrange
            LocalDateTime dataVenda = LocalDateTime.now();
            VendaComVeiculoResponse response = new VendaComVeiculoResponse();

            // Act
            response.setVendaId(1L);
            response.setCodigoPagamento("PAG-ABC12345");
            response.setCpfCliente("12345678900");
            response.setNomeCliente("João Silva");
            response.setValorVenda(new BigDecimal("75000.00"));
            response.setStatusPagamento("CONFIRMADO");
            response.setDataVenda(dataVenda);
            response.setVeiculoId(1L);
            response.setMarca("Toyota");
            response.setModelo("Corolla");
            response.setAno(2023);
            response.setCor("Branco");
            response.setPrecoOriginal(new BigDecimal("75000.00"));

            // Assert
            assertNotNull(response.getVendaId());
            assertNotNull(response.getCodigoPagamento());
            assertNotNull(response.getCpfCliente());
            assertNotNull(response.getNomeCliente());
            assertNotNull(response.getValorVenda());
            assertNotNull(response.getStatusPagamento());
            assertNotNull(response.getDataVenda());
            assertNotNull(response.getVeiculoId());
            assertNotNull(response.getMarca());
            assertNotNull(response.getModelo());
            assertNotNull(response.getAno());
            assertNotNull(response.getCor());
            assertNotNull(response.getPrecoOriginal());
        }
    }

    @Nested
    @DisplayName("Testes de diferentes status de pagamento")
    class DiferentesStatusPagamentoTests {

        @Test
        @DisplayName("Deve aceitar venda com status PENDENTE")
        void deveAceitarVendaComStatusPendente() {
            // Arrange & Act
            VendaComVeiculoResponse response = new VendaComVeiculoResponse();
            response.setStatusPagamento("PENDENTE");

            // Assert
            assertEquals("PENDENTE", response.getStatusPagamento());
        }

        @Test
        @DisplayName("Deve aceitar venda com status CONFIRMADO")
        void deveAceitarVendaComStatusConfirmado() {
            // Arrange & Act
            VendaComVeiculoResponse response = new VendaComVeiculoResponse();
            response.setStatusPagamento("CONFIRMADO");

            // Assert
            assertEquals("CONFIRMADO", response.getStatusPagamento());
        }

        @Test
        @DisplayName("Deve aceitar venda com status CANCELADO")
        void deveAceitarVendaComStatusCancelado() {
            // Arrange & Act
            VendaComVeiculoResponse response = new VendaComVeiculoResponse();
            response.setStatusPagamento("CANCELADO");

            // Assert
            assertEquals("CANCELADO", response.getStatusPagamento());
        }
    }
}

