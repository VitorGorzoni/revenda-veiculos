package org.com.revenda.domain.usecase;

import org.com.revenda.domain.entity.StatusPagamento;
import org.com.revenda.domain.entity.StatusVeiculo;
import org.com.revenda.domain.entity.Veiculo;
import org.com.revenda.domain.entity.Venda;
import org.com.revenda.domain.repository.VeiculoRepository;
import org.com.revenda.domain.repository.VendaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para ProcessarPagamentoUseCase")
class ProcessarPagamentoUseCaseTest {

    @Mock
    private VendaRepository vendaRepository;

    @Mock
    private VeiculoRepository veiculoRepository;

    @InjectMocks
    private ProcessarPagamentoUseCase processarPagamentoUseCase;

    private Venda venda;
    private Veiculo veiculo;

    @BeforeEach
    void setUp() {
        venda = new Venda();
        venda.setId(1L);
        venda.setVeiculoId(1L);
        venda.setCodigoPagamento("PAG-ABC12345");
        venda.setStatusPagamento(StatusPagamento.PENDENTE);

        veiculo = new Veiculo();
        veiculo.setId(1L);
        veiculo.setMarca("Honda");
        veiculo.setModelo("Civic");
        veiculo.setStatus(StatusVeiculo.DISPONIVEL);
    }

    @Test
    @DisplayName("Deve confirmar pagamento com sucesso")
    void deveConfirmarPagamentoComSucesso() {
        // Arrange
        when(vendaRepository.findByCodigoPagamento("PAG-ABC12345"))
            .thenReturn(Optional.of(venda));
        when(veiculoRepository.findById(1L))
            .thenReturn(Optional.of(veiculo));
        when(vendaRepository.save(any(Venda.class)))
            .thenReturn(venda);
        when(veiculoRepository.save(any(Veiculo.class)))
            .thenReturn(veiculo);

        // Act
        processarPagamentoUseCase.execute("PAG-ABC12345", StatusPagamento.CONFIRMADO);

        // Assert
        verify(vendaRepository, times(1)).findByCodigoPagamento("PAG-ABC12345");
        verify(veiculoRepository, times(1)).findById(1L);
        verify(vendaRepository, times(1)).save(venda);
        verify(veiculoRepository, times(1)).save(veiculo);
        assertEquals(StatusPagamento.CONFIRMADO, venda.getStatusPagamento());
        assertEquals(StatusVeiculo.VENDIDO, veiculo.getStatus());
    }

    @Test
    @DisplayName("Deve cancelar pagamento com sucesso")
    void deveCancelarPagamentoComSucesso() {
        // Arrange
        when(vendaRepository.findByCodigoPagamento("PAG-ABC12345"))
            .thenReturn(Optional.of(venda));
        when(vendaRepository.save(any(Venda.class)))
            .thenReturn(venda);

        // Act
        processarPagamentoUseCase.execute("PAG-ABC12345", StatusPagamento.CANCELADO);

        // Assert
        verify(vendaRepository, times(1)).findByCodigoPagamento("PAG-ABC12345");
        verify(vendaRepository, times(1)).save(venda);
        verify(veiculoRepository, never()).save(any(Veiculo.class));
        assertEquals(StatusPagamento.CANCELADO, venda.getStatusPagamento());
    }

    @Test
    @DisplayName("Deve lançar exceção quando código de pagamento não for encontrado")
    void deveLancarExcecaoQuandoCodigoPagamentoNaoEncontrado() {
        // Arrange
        when(vendaRepository.findByCodigoPagamento("PAG-INVALIDO"))
            .thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> processarPagamentoUseCase.execute("PAG-INVALIDO", StatusPagamento.CONFIRMADO)
        );

        assertEquals("Código de pagamento não encontrado: PAG-INVALIDO", exception.getMessage());
        verify(vendaRepository, times(1)).findByCodigoPagamento("PAG-INVALIDO");
        verify(vendaRepository, never()).save(any(Venda.class));
        verify(veiculoRepository, never()).save(any(Veiculo.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando veículo não for encontrado ao confirmar pagamento")
    void deveLancarExcecaoQuandoVeiculoNaoEncontradoAoConfirmar() {
        // Arrange
        when(vendaRepository.findByCodigoPagamento("PAG-ABC12345"))
            .thenReturn(Optional.of(venda));
        when(veiculoRepository.findById(1L))
            .thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> processarPagamentoUseCase.execute("PAG-ABC12345", StatusPagamento.CONFIRMADO)
        );

        assertEquals("Veículo não encontrado com ID: 1", exception.getMessage());
        verify(veiculoRepository, times(1)).findById(1L);
        verify(veiculoRepository, never()).save(any(Veiculo.class));
    }
}

