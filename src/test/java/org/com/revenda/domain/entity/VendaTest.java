package org.com.revenda.domain.entity;

import org.com.revenda.domain.enums.StatusPagamento;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes para a entidade Venda")
class VendaTest {

    @Test
    @DisplayName("Deve criar venda com construtor completo")
    void deveCriarVendaComConstrutorCompleto() {
        // Arrange & Act
        Venda venda = new Venda(1L, "123.456.789-00", "João Silva", new BigDecimal("75000.00"));

        // Assert
        assertNotNull(venda);
        assertEquals(1L, venda.getVeiculoId());
        assertEquals("123.456.789-00", venda.getCpfCliente());
        assertEquals("João Silva", venda.getNomeCliente());
        assertEquals(0, new BigDecimal("75000.00").compareTo(venda.getValorVenda()));
        assertEquals(StatusPagamento.PENDENTE, venda.getStatusPagamento());
        assertNotNull(venda.getDataVenda());
        assertNotNull(venda.getCodigoPagamento());
        assertTrue(venda.getCodigoPagamento().startsWith("PAG-"));
    }

    @Test
    @DisplayName("Deve criar venda com construtor vazio")
    void deveCriarVendaComConstrutorVazio() {
        // Act
        Venda venda = new Venda();

        // Assert
        assertNotNull(venda);
    }

    @Test
    @DisplayName("Deve confirmar pagamento")
    void deveConfirmarPagamento() {
        // Arrange
        Venda venda = new Venda();
        venda.setStatusPagamento(StatusPagamento.PENDENTE);

        // Act
        venda.confirmarPagamento();

        // Assert
        assertEquals(StatusPagamento.CONFIRMADO, venda.getStatusPagamento());
    }

    @Test
    @DisplayName("Deve cancelar pagamento")
    void deveCancelarPagamento() {
        // Arrange
        Venda venda = new Venda();
        venda.setStatusPagamento(StatusPagamento.PENDENTE);

        // Act
        venda.cancelarPagamento();

        // Assert
        assertEquals(StatusPagamento.CANCELADO, venda.getStatusPagamento());
    }

    @Test
    @DisplayName("Deve verificar se pagamento está confirmado")
    void deveVerificarSePagamentoEstaConfirmado() {
        // Arrange
        Venda venda = new Venda();
        venda.setStatusPagamento(StatusPagamento.CONFIRMADO);

        // Act
        boolean resultado = venda.isPagamentoConfirmado();

        // Assert
        assertTrue(resultado);
    }

    @Test
    @DisplayName("Deve retornar falso quando pagamento não está confirmado")
    void deveRetornarFalsoQuandoPagamentoNaoConfirmado() {
        // Arrange
        Venda venda = new Venda();
        venda.setStatusPagamento(StatusPagamento.PENDENTE);

        // Act
        boolean resultado = venda.isPagamentoConfirmado();

        // Assert
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Deve definir e obter todos os campos")
    void deveDefinirEObterTodosCampos() {
        // Arrange
        Venda venda = new Venda();
        LocalDateTime dataVenda = LocalDateTime.now();

        // Act
        venda.setId(1L);
        venda.setVeiculoId(2L);
        venda.setCpfCliente("987.654.321-00");
        venda.setNomeCliente("Maria Silva");
        venda.setValorVenda(new BigDecimal("85000.00"));
        venda.setDataVenda(dataVenda);
        venda.setCodigoPagamento("PAG-XYZ789");
        venda.setStatusPagamento(StatusPagamento.CONFIRMADO);

        // Assert
        assertEquals(1L, venda.getId());
        assertEquals(2L, venda.getVeiculoId());
        assertEquals("987.654.321-00", venda.getCpfCliente());
        assertEquals("Maria Silva", venda.getNomeCliente());
        assertEquals(0, new BigDecimal("85000.00").compareTo(venda.getValorVenda()));
        assertEquals(dataVenda, venda.getDataVenda());
        assertEquals("PAG-XYZ789", venda.getCodigoPagamento());
        assertEquals(StatusPagamento.CONFIRMADO, venda.getStatusPagamento());
    }
}
