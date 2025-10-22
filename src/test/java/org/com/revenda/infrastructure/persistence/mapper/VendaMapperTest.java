package org.com.revenda.infrastructure.persistence.mapper;

import org.com.revenda.domain.entity.StatusPagamento;
import org.com.revenda.domain.entity.Venda;
import org.com.revenda.infrastructure.persistence.entity.VendaJpaEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes para VendaMapper")
class VendaMapperTest {

    private VendaMapper vendaMapper;

    @BeforeEach
    void setUp() {
        vendaMapper = new VendaMapper();
    }

    @Test
    @DisplayName("Deve converter Venda para VendaJpaEntity")
    void deveConverterVendaParaVendaJpaEntity() {
        // Arrange
        Venda venda = new Venda();
        venda.setId(1L);
        venda.setVeiculoId(2L);
        venda.setCpfCliente("123.456.789-00");
        venda.setNomeCliente("João Silva");
        venda.setValorVenda(new BigDecimal("75000.00"));
        venda.setDataVenda(LocalDateTime.of(2025, 1, 18, 14, 30));
        venda.setCodigoPagamento("PAG-ABC123");
        venda.setStatusPagamento(StatusPagamento.CONFIRMADO);

        // Act
        VendaJpaEntity entity = vendaMapper.toJpaEntity(venda);

        // Assert
        assertNotNull(entity);
        assertEquals(1L, entity.getId());
        assertEquals(2L, entity.getVeiculoId());
        assertEquals("123.456.789-00", entity.getCpfCliente());
        assertEquals("João Silva", entity.getNomeCliente());
        assertEquals("PAG-ABC123", entity.getCodigoPagamento());
        assertEquals(StatusPagamento.CONFIRMADO, entity.getStatusPagamento());
    }

    @Test
    @DisplayName("Deve retornar null quando Venda for null")
    void deveRetornarNullQuandoVendaForNull() {
        // Act
        VendaJpaEntity entity = vendaMapper.toJpaEntity(null);

        // Assert
        assertNull(entity);
    }

    @Test
    @DisplayName("Deve converter VendaJpaEntity para Venda")
    void deveConverterVendaJpaEntityParaVenda() {
        // Arrange
        VendaJpaEntity entity = new VendaJpaEntity();
        entity.setId(1L);
        entity.setVeiculoId(2L);
        entity.setCpfCliente("987.654.321-00");
        entity.setNomeCliente("Maria Silva");
        entity.setValorVenda(new BigDecimal("85000.00"));
        entity.setDataVenda(LocalDateTime.of(2025, 1, 18, 14, 30));
        entity.setCodigoPagamento("PAG-XYZ789");
        entity.setStatusPagamento(StatusPagamento.PENDENTE);

        // Act
        Venda venda = vendaMapper.toDomainEntity(entity);

        // Assert
        assertNotNull(venda);
        assertEquals(1L, venda.getId());
        assertEquals(2L, venda.getVeiculoId());
        assertEquals("987.654.321-00", venda.getCpfCliente());
        assertEquals("Maria Silva", venda.getNomeCliente());
        assertEquals("PAG-XYZ789", venda.getCodigoPagamento());
        assertEquals(StatusPagamento.PENDENTE, venda.getStatusPagamento());
    }

    @Test
    @DisplayName("Deve retornar null quando VendaJpaEntity for null")
    void deveRetornarNullQuandoVendaJpaEntityForNull() {
        // Act
        Venda venda = vendaMapper.toDomainEntity(null);

        // Assert
        assertNull(venda);
    }
}
