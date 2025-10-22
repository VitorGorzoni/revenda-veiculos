package org.com.revenda.domain.usecase;

import org.com.revenda.domain.entity.StatusPagamento;
import org.com.revenda.domain.entity.StatusVeiculo;
import org.com.revenda.domain.entity.Veiculo;
import org.com.revenda.domain.entity.Venda;
import org.com.revenda.domain.exception.VeiculoNaoEncontradoException;
import org.com.revenda.domain.repository.VeiculoRepository;
import org.com.revenda.domain.repository.VendaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes abrangentes para ProcessarPagamentoUseCase")
class ProcessarPagamentoUseCaseExtendedTest {

    @Mock
    private VendaRepository vendaRepository;

    @Mock
    private VeiculoRepository veiculoRepository;

    @InjectMocks
    private ProcessarPagamentoUseCase processarPagamentoUseCase;

    private Venda vendaPendente;
    private Veiculo veiculo;
    private String codigoPagamento;

    @BeforeEach
    void setUp() {
        codigoPagamento = "PAG-12345678";

        veiculo = new Veiculo();
        veiculo.setId(1L);
        veiculo.setMarca("Toyota");
        veiculo.setModelo("Corolla");
        veiculo.setAno(2023);
        veiculo.setCor("Branco");
        veiculo.setPreco(new BigDecimal("75000.00"));
        veiculo.setStatus(StatusVeiculo.DISPONIVEL);
        veiculo.setDataCadastro(LocalDateTime.now());

        vendaPendente = new Venda();
        vendaPendente.setId(1L);
        vendaPendente.setVeiculoId(1L);
        vendaPendente.setCpfComprador("12345678900");
        vendaPendente.setDataVenda(LocalDateTime.now());
        vendaPendente.setCodigoPagamento(codigoPagamento);
        vendaPendente.setStatusPagamento(StatusPagamento.PENDENTE);
    }

    @Test
    @DisplayName("Deve confirmar pagamento e marcar veículo como vendido")
    void deveConfirmarPagamentoComSucesso() {
        // Arrange
        when(vendaRepository.findByCodigoPagamento(codigoPagamento)).thenReturn(Optional.of(vendaPendente));
        when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculo));

        // Act
        processarPagamentoUseCase.execute(codigoPagamento, StatusPagamento.CONFIRMADO);

        // Assert
        assertEquals(StatusPagamento.CONFIRMADO, vendaPendente.getStatusPagamento());
        assertEquals(StatusVeiculo.VENDIDO, veiculo.getStatus());
        verify(vendaRepository, times(1)).save(vendaPendente);
        verify(veiculoRepository, times(1)).save(veiculo);
    }

    @Test
    @DisplayName("Deve cancelar pagamento sem alterar status do veículo")
    void deveCancelarPagamentoComSucesso() {
        // Arrange
        when(vendaRepository.findByCodigoPagamento(codigoPagamento)).thenReturn(Optional.of(vendaPendente));

        // Act
        processarPagamentoUseCase.execute(codigoPagamento, StatusPagamento.CANCELADO);

        // Assert
        assertEquals(StatusPagamento.CANCELADO, vendaPendente.getStatusPagamento());
        assertEquals(StatusVeiculo.DISPONIVEL, veiculo.getStatus()); // Deve permanecer disponível
        verify(vendaRepository, times(1)).save(vendaPendente);
        verify(veiculoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando código de pagamento não existir")
    void deveLancarExcecaoQuandoCodigoPagamentoNaoExistir() {
        // Arrange
        String codigoInexistente = "PAG-INEXISTENTE";
        when(vendaRepository.findByCodigoPagamento(codigoInexistente)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> processarPagamentoUseCase.execute(codigoInexistente, StatusPagamento.CONFIRMADO)
        );

        assertEquals("Código de pagamento não encontrado: PAG-INEXISTENTE", exception.getMessage());
        verify(veiculoRepository, never()).findById(any());
        verify(vendaRepository, never()).save(any());
        verify(veiculoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando veículo não for encontrado ao confirmar pagamento")
    void deveLancarExcecaoQuandoVeiculoNaoEncontradoAoConfirmar() {
        // Arrange
        when(vendaRepository.findByCodigoPagamento(codigoPagamento)).thenReturn(Optional.of(vendaPendente));
        when(veiculoRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> processarPagamentoUseCase.execute(codigoPagamento, StatusPagamento.CONFIRMADO)
        );

        assertEquals("Veículo não encontrado com ID: 1", exception.getMessage());
        verify(vendaRepository, never()).save(any());
        verify(veiculoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve processar pagamento já confirmado sem erro")
    void deveProcessarPagamentoJaConfirmado() {
        // Arrange
        vendaPendente.setStatusPagamento(StatusPagamento.CONFIRMADO);
        when(vendaRepository.findByCodigoPagamento(codigoPagamento)).thenReturn(Optional.of(vendaPendente));
        when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculo));

        // Act
        processarPagamentoUseCase.execute(codigoPagamento, StatusPagamento.CONFIRMADO);

        // Assert
        assertEquals(StatusPagamento.CONFIRMADO, vendaPendente.getStatusPagamento());
        verify(vendaRepository, times(1)).save(vendaPendente);
        verify(veiculoRepository, times(1)).save(veiculo);
    }

    @Test
    @DisplayName("Deve processar pagamento já cancelado sem erro")
    void deveProcessarPagamentoJaCancelado() {
        // Arrange
        vendaPendente.setStatusPagamento(StatusPagamento.CANCELADO);
        when(vendaRepository.findByCodigoPagamento(codigoPagamento)).thenReturn(Optional.of(vendaPendente));

        // Act
        processarPagamentoUseCase.execute(codigoPagamento, StatusPagamento.CANCELADO);

        // Assert
        assertEquals(StatusPagamento.CANCELADO, vendaPendente.getStatusPagamento());
        verify(vendaRepository, times(1)).save(vendaPendente);
        verify(veiculoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve processar múltiplas confirmações de pagamento")
    void deveProcessarMultiplasConfirmacoes() {
        // Arrange
        when(vendaRepository.findByCodigoPagamento(codigoPagamento)).thenReturn(Optional.of(vendaPendente));
        when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculo));

        // Act
        processarPagamentoUseCase.execute(codigoPagamento, StatusPagamento.CONFIRMADO);
        processarPagamentoUseCase.execute(codigoPagamento, StatusPagamento.CONFIRMADO);

        // Assert
        assertEquals(StatusPagamento.CONFIRMADO, vendaPendente.getStatusPagamento());
        assertEquals(StatusVeiculo.VENDIDO, veiculo.getStatus());
        verify(vendaRepository, times(2)).save(vendaPendente);
        verify(veiculoRepository, times(2)).save(veiculo);
    }

    @Test
    @DisplayName("Deve processar cancelamento após confirmação")
    void deveProcessarCancelamentoAposConfirmacao() {
        // Arrange
        vendaPendente.setStatusPagamento(StatusPagamento.CONFIRMADO);
        veiculo.setStatus(StatusVeiculo.VENDIDO);
        when(vendaRepository.findByCodigoPagamento(codigoPagamento)).thenReturn(Optional.of(vendaPendente));

        // Act
        processarPagamentoUseCase.execute(codigoPagamento, StatusPagamento.CANCELADO);

        // Assert
        assertEquals(StatusPagamento.CANCELADO, vendaPendente.getStatusPagamento());
        // Veículo mantém status VENDIDO (não é revertido automaticamente)
        assertEquals(StatusVeiculo.VENDIDO, veiculo.getStatus());
        verify(vendaRepository, times(1)).save(vendaPendente);
        verify(veiculoRepository, never()).save(any());
    }
}
