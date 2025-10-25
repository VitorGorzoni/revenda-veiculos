package org.com.revenda.application.service;

import org.com.revenda.application.gateway.VendaPersistenceGateway;
import org.com.revenda.domain.entity.Venda;
import org.com.revenda.domain.enums.StatusPagamento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do Serviço: Listar Veículos Vendidos")
class ListarVeiculosVendidosServiceTest {

    @Mock
    private VendaPersistenceGateway vendaPersistenceGateway;

    @InjectMocks
    private ListarVeiculosVendidosService listarVeiculosVendidosService;

    private List<Venda> vendas;

    @BeforeEach
    void setUp() {
        Venda venda1 = new Venda(1L, "123.456.789-00", "João Silva", new BigDecimal("75000.00"));
        venda1.setId(1L);
        venda1.setStatusPagamento(StatusPagamento.CONFIRMADO);

        Venda venda2 = new Venda(2L, "987.654.321-00", "Maria Santos", new BigDecimal("80000.00"));
        venda2.setId(2L);
        venda2.setStatusPagamento(StatusPagamento.PENDENTE);

        vendas = Arrays.asList(venda1, venda2);
    }

    @Test
    @DisplayName("Deve listar veículos vendidos ordenados por valor")
    void deveListarVeiculosVendidosOrdenadosPorValor() {
        // Given
        when(vendaPersistenceGateway.findAllOrderByValorVendaDesc()).thenReturn(vendas);

        // When
        List<Venda> resultado = listarVeiculosVendidosService.execute();

        // Then
        assertThat(resultado).isNotEmpty();
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getCpfCliente()).isEqualTo("123.456.789-00");
        verify(vendaPersistenceGateway).findAllOrderByValorVendaDesc();
    }
}

