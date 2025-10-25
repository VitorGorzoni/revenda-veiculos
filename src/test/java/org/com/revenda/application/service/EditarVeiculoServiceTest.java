package org.com.revenda.application.service;

import org.com.revenda.application.gateway.VeiculoPersistenceGateway;
import org.com.revenda.domain.entity.Veiculo;
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
@DisplayName("Testes do Serviço: Editar Veículo")
class EditarVeiculoServiceTest {

    @Mock
    private VeiculoPersistenceGateway veiculoPersistenceGateway;

    @InjectMocks
    private EditarVeiculoService editarVeiculoService;

    private Veiculo veiculoExistente;
    private Veiculo veiculoAtualizado;

    @BeforeEach
    void setUp() {
        veiculoExistente = new Veiculo("Toyota", "Corolla", 2023, "Branco", new BigDecimal("75000.00"));
        veiculoExistente.setId(1L);

        veiculoAtualizado = new Veiculo("Toyota", "Corolla XEI", 2024, "Preto", new BigDecimal("80000.00"));
    }

    @Test
    @DisplayName("Deve editar veículo com sucesso")
    void deveEditarVeiculoComSucesso() {
        // Given
        when(veiculoPersistenceGateway.findById(1L)).thenReturn(Optional.of(veiculoExistente));
        when(veiculoPersistenceGateway.save(any(Veiculo.class))).thenReturn(veiculoExistente);

        // When
        Veiculo resultado = editarVeiculoService.execute(1L, veiculoAtualizado);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getModelo()).isEqualTo("Corolla XEI");
        assertThat(resultado.getAno()).isEqualTo(2024);
        verify(veiculoPersistenceGateway).findById(1L);
        verify(veiculoPersistenceGateway).save(veiculoExistente);
    }

    @Test
    @DisplayName("Deve lançar exceção ao editar veículo não encontrado")
    void deveLancarExcecaoAoEditarVeiculoNaoEncontrado() {
        // Given
        when(veiculoPersistenceGateway.findById(999L)).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> editarVeiculoService.execute(999L, veiculoAtualizado))
            .isInstanceOf(VeiculoNaoEncontradoException.class)
            .hasMessageContaining("Veículo não encontrado com ID: 999");
    }

    @Test
    @DisplayName("Não deve permitir editar veículo vendido")
    void naoDevePermitirEditarVeiculoVendido() {
        // Given
        veiculoExistente.setStatus(StatusVeiculo.VENDIDO);
        when(veiculoPersistenceGateway.findById(1L)).thenReturn(Optional.of(veiculoExistente));

        // When / Then
        assertThatThrownBy(() -> editarVeiculoService.execute(1L, veiculoAtualizado))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("Não é possível editar um veículo que não está disponível");
    }
}

