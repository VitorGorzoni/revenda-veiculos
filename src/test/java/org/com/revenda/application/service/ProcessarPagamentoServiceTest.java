package org.com.revenda.application.service;

import org.com.revenda.application.gateway.VeiculoPersistenceGateway;
import org.com.revenda.application.gateway.VendaPersistenceGateway;
import org.com.revenda.domain.entity.Veiculo;
import org.com.revenda.domain.entity.Venda;
import org.com.revenda.domain.enums.StatusPagamento;
import org.com.revenda.domain.enums.StatusVeiculo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do Serviço: Processar Pagamento")
class ProcessarPagamentoServiceTest {

    @Mock
    private VendaPersistenceGateway vendaPersistenceGateway;

    @Mock
    private VeiculoPersistenceGateway veiculoPersistenceGateway;

    @InjectMocks
    private ProcessarPagamentoService processarPagamentoService;

    private Venda venda;
    private Veiculo veiculo;

    @BeforeEach
    void setUp() {
        veiculo = new Veiculo("Toyota", "Corolla", 2023, "Branco", new BigDecimal("75000.00"));
        veiculo.setId(1L);
        veiculo.setStatus(StatusVeiculo.DISPONIVEL);

        venda = new Venda(1L, "123.456.789-00", "João Silva", new BigDecimal("75000.00"));
        venda.setId(1L);
        venda.setCodigoPagamento("PAG-ABC123");
        venda.setStatusPagamento(StatusPagamento.PENDENTE);
    }

    @Test
    @DisplayName("Deve confirmar pagamento com sucesso")
    void deveConfirmarPagamentoComSucesso() {
        // Given
        when(vendaPersistenceGateway.findByCodigoPagamento("PAG-ABC123")).thenReturn(Optional.of(venda));
        when(veiculoPersistenceGateway.findById(1L)).thenReturn(Optional.of(veiculo));
        when(vendaPersistenceGateway.save(any(Venda.class))).thenReturn(venda);
        when(veiculoPersistenceGateway.save(any(Veiculo.class))).thenReturn(veiculo);

        // When
        processarPagamentoService.execute("PAG-ABC123", StatusPagamento.CONFIRMADO);

        // Then
        assertThat(venda.getStatusPagamento()).isEqualTo(StatusPagamento.CONFIRMADO);
        verify(vendaPersistenceGateway).findByCodigoPagamento("PAG-ABC123");
        verify(vendaPersistenceGateway).save(venda);
        verify(veiculoPersistenceGateway).save(veiculo);
    }

    @Test
    @DisplayName("Deve cancelar pagamento com sucesso")
    void deveCancelarPagamentoComSucesso() {
        // Given
        when(vendaPersistenceGateway.findByCodigoPagamento("PAG-ABC123")).thenReturn(Optional.of(venda));
        when(veiculoPersistenceGateway.findById(1L)).thenReturn(Optional.of(veiculo));
        when(vendaPersistenceGateway.save(any(Venda.class))).thenReturn(venda);
        when(veiculoPersistenceGateway.save(any(Veiculo.class))).thenReturn(veiculo);

        // When
        processarPagamentoService.execute("PAG-ABC123", StatusPagamento.CANCELADO);

        // Then
        assertThat(venda.getStatusPagamento()).isEqualTo(StatusPagamento.CANCELADO);
        verify(vendaPersistenceGateway).findByCodigoPagamento("PAG-ABC123");
        verify(vendaPersistenceGateway).save(venda);
        verify(veiculoPersistenceGateway).findById(1L);
        verify(veiculoPersistenceGateway).save(veiculo);
    }

    @Test
    @DisplayName("Deve lançar exceção quando código de pagamento não for encontrado")
    void deveLancarExcecaoQuandoCodigoPagamentoNaoEncontrado() {
        // Given
        when(vendaPersistenceGateway.findByCodigoPagamento("PAG-INVALIDO")).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> processarPagamentoService.execute("PAG-INVALIDO", StatusPagamento.CONFIRMADO))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Código de pagamento não encontrado");
    }
}
