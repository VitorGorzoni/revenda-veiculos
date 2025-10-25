package org.com.revenda.application.service;

import org.com.revenda.application.gateway.VeiculoPersistenceGateway;
import org.com.revenda.domain.entity.Veiculo;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do Serviço: Buscar Veículo por ID")
class BuscarVeiculoPorIdServiceTest {

    @Mock
    private VeiculoPersistenceGateway veiculoPersistenceGateway;

    @InjectMocks
    private BuscarVeiculoPorIdService buscarVeiculoPorIdService;

    private Veiculo veiculo;

    @BeforeEach
    void setUp() {
        veiculo = new Veiculo("Toyota", "Corolla", 2023, "Branco", new BigDecimal("75000.00"));
        veiculo.setId(1L);
    }

    @Test
    @DisplayName("Deve buscar veículo por ID com sucesso")
    void deveBuscarVeiculoPorIdComSucesso() {
        // Given
        when(veiculoPersistenceGateway.findById(1L)).thenReturn(Optional.of(veiculo));

        // When
        Veiculo resultado = buscarVeiculoPorIdService.execute(1L);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getMarca()).isEqualTo("Toyota");
        verify(veiculoPersistenceGateway).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção quando veículo não for encontrado")
    void deveLancarExcecaoQuandoVeiculoNaoEncontrado() {
        // Given
        when(veiculoPersistenceGateway.findById(999L)).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> buscarVeiculoPorIdService.execute(999L))
            .isInstanceOf(VeiculoNaoEncontradoException.class)
            .hasMessageContaining("Veículo não encontrado com ID: 999");

        verify(veiculoPersistenceGateway).findById(999L);
    }
}

