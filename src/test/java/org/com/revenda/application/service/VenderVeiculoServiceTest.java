package org.com.revenda.application.service;

import org.com.revenda.application.gateway.VeiculoPersistenceGateway;
import org.com.revenda.application.gateway.VendaPersistenceGateway;
import org.com.revenda.domain.entity.Veiculo;
import org.com.revenda.domain.entity.Venda;
import org.com.revenda.domain.enums.StatusVeiculo;
import org.com.revenda.domain.exception.VeiculoNaoEncontradoException;
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
@DisplayName("Testes do Serviço: Vender Veículo")
class VenderVeiculoServiceTest {

    @Mock
    private VeiculoPersistenceGateway veiculoPersistenceGateway;

    @Mock
    private VendaPersistenceGateway vendaPersistenceGateway;

    @InjectMocks
    private VenderVeiculoService venderVeiculoService;

    private Veiculo veiculo;
    private Venda venda;

    @BeforeEach
    void setUp() {
        veiculo = new Veiculo("Toyota", "Corolla", 2023, "Branco", new BigDecimal("75000.00"));
        veiculo.setId(1L);

        venda = new Venda(1L, "123.456.789-00", "João Silva", new BigDecimal("75000.00"));
        venda.setId(1L);
    }

    @Test
    @DisplayName("Deve vender veículo com sucesso")
    void deveVenderVeiculoComSucesso() {
        // Given
        when(veiculoPersistenceGateway.findById(1L)).thenReturn(Optional.of(veiculo));
        when(vendaPersistenceGateway.save(any(Venda.class))).thenReturn(venda);
        when(veiculoPersistenceGateway.save(any(Veiculo.class))).thenReturn(veiculo);

        // When
        Venda resultado = venderVeiculoService.execute(1L, "123.456.789-00", "João Silva", new BigDecimal("75000.00"));

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getCpfCliente()).isEqualTo("123.456.789-00");
        assertThat(veiculo.getStatus()).isEqualTo(StatusVeiculo.RESERVADO);
        verify(veiculoPersistenceGateway).findById(1L);
        verify(vendaPersistenceGateway).save(any(Venda.class));
        verify(veiculoPersistenceGateway).save(veiculo);
    }

    @Test
    @DisplayName("Não deve vender veículo não encontrado")
    void naoDeveVenderVeiculoNaoEncontrado() {
        // Given
        when(veiculoPersistenceGateway.findById(999L)).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> venderVeiculoService.execute(999L, "123.456.789-00", "João Silva", new BigDecimal("75000.00")))
            .isInstanceOf(VeiculoNaoEncontradoException.class)
            .hasMessageContaining("Veículo não encontrado com ID: 999");
    }

    @Test
    @DisplayName("Não deve vender veículo já vendido")
    void naoDeveVenderVeiculoJaVendido() {
        // Given
        veiculo.setStatus(StatusVeiculo.VENDIDO);
        when(veiculoPersistenceGateway.findById(1L)).thenReturn(Optional.of(veiculo));

        // When / Then
        assertThatThrownBy(() -> venderVeiculoService.execute(1L, "123.456.789-00", "João Silva", new BigDecimal("75000.00")))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("Veículo não está disponível para venda");
    }
}
