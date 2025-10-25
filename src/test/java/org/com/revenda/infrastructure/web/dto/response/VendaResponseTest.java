package org.com.revenda.infrastructure.web.dto.response;

import org.com.revenda.domain.enums.StatusPagamento;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes para VendaResponse")
class VendaResponseTest {

    @Nested
    @DisplayName("Testes de criação")
    class CriacaoTests {

        @Test
        @DisplayName("Deve criar VendaResponse com construtor completo")
        void deveCriarVendaResponseComConstrutorCompleto() {
            // Arrange
            LocalDateTime dataVenda = LocalDateTime.now();

            // Act
            VendaResponse response = new VendaResponse(
                1L, 1L, "12345678900", dataVenda, "PAG-ABC12345", StatusPagamento.PENDENTE
            );

            // Assert
            assertNotNull(response);
            assertEquals(1L, response.getId());
            assertEquals(1L, response.getVeiculoId());
            assertEquals("12345678900", response.getCpfComprador());
            assertEquals(dataVenda, response.getDataVenda());
            assertEquals("PAG-ABC12345", response.getCodigoPagamento());
            assertEquals(StatusPagamento.PENDENTE, response.getStatusPagamento());
        }

        @Test
        @DisplayName("Deve criar VendaResponse vazia com construtor sem argumentos")
        void deveCriarVendaResponseVaziaComConstrutorSemArgumentos() {
            // Act
            VendaResponse response = new VendaResponse();

            // Assert
            assertNotNull(response);
            assertNull(response.getId());
            assertNull(response.getVeiculoId());
            assertNull(response.getCpfComprador());
            assertNull(response.getDataVenda());
            assertNull(response.getCodigoPagamento());
            assertNull(response.getStatusPagamento());
        }
    }

    @Nested
    @DisplayName("Testes de getters e setters")
    class GettersSettersTests {

        @Test
        @DisplayName("Deve permitir modificar todos os campos via setters")
        void devePermitirModificarTodosCamposViaSetters() {
            // Arrange
            VendaResponse response = new VendaResponse();
            LocalDateTime dataVenda = LocalDateTime.now();

            // Act
            response.setId(1L);
            response.setVeiculoId(1L);
            response.setCpfComprador("12345678900");
            response.setDataVenda(dataVenda);
            response.setCodigoPagamento("PAG-ABC12345");
            response.setStatusPagamento(StatusPagamento.PENDENTE);

            // Assert
            assertEquals(1L, response.getId());
            assertEquals(1L, response.getVeiculoId());
            assertEquals("12345678900", response.getCpfComprador());
            assertEquals(dataVenda, response.getDataVenda());
            assertEquals("PAG-ABC12345", response.getCodigoPagamento());
            assertEquals(StatusPagamento.PENDENTE, response.getStatusPagamento());
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
            VendaResponse response1 = new VendaResponse(
                1L, 1L, "12345678900", dataVenda, "PAG-ABC12345", StatusPagamento.PENDENTE
            );
            VendaResponse response2 = new VendaResponse(
                1L, 1L, "12345678900", dataVenda, "PAG-ABC12345", StatusPagamento.PENDENTE
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
            VendaResponse response1 = new VendaResponse(
                1L, 1L, "12345678900", dataVenda, "PAG-ABC12345", StatusPagamento.PENDENTE
            );
            VendaResponse response2 = new VendaResponse(
                2L, 2L, "98765432100", dataVenda, "PAG-XYZ789", StatusPagamento.CONFIRMADO
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
            VendaResponse response = new VendaResponse(
                1L, 1L, "12345678900", dataVenda, "PAG-ABC12345", StatusPagamento.PENDENTE
            );

            // Act
            String resultado = response.toString();

            // Assert
            assertNotNull(resultado);
            assertTrue(resultado.contains("12345678900"));
            assertTrue(resultado.contains("PAG-ABC12345"));
        }
    }

    @Nested
    @DisplayName("Testes de diferentes status de pagamento")
    class DiferentesStatusPagamentoTests {

        @Test
        @DisplayName("Deve aceitar venda com status PENDENTE")
        void deveAceitarVendaComStatusPendente() {
            // Arrange & Act
            VendaResponse response = new VendaResponse();
            response.setStatusPagamento(StatusPagamento.PENDENTE);

            // Assert
            assertEquals(StatusPagamento.PENDENTE, response.getStatusPagamento());
        }

        @Test
        @DisplayName("Deve aceitar venda com status CONFIRMADO")
        void deveAceitarVendaComStatusConfirmado() {
            // Arrange & Act
            VendaResponse response = new VendaResponse();
            response.setStatusPagamento(StatusPagamento.CONFIRMADO);

            // Assert
            assertEquals(StatusPagamento.CONFIRMADO, response.getStatusPagamento());
        }

        @Test
        @DisplayName("Deve aceitar venda com status CANCELADO")
        void deveAceitarVendaComStatusCancelado() {
            // Arrange & Act
            VendaResponse response = new VendaResponse();
            response.setStatusPagamento(StatusPagamento.CANCELADO);

            // Assert
            assertEquals(StatusPagamento.CANCELADO, response.getStatusPagamento());
        }
    }
}

