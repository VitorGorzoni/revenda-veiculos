package org.com.revenda.domain.usecase;

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

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para VenderVeiculoUseCase")
class VenderVeiculoUseCaseTest {

    @Mock
    private VeiculoRepository veiculoRepository;

    @Mock
    private VendaRepository vendaRepository;

    @InjectMocks
    private VenderVeiculoUseCase venderVeiculoUseCase;

    private Veiculo veiculoDisponivel;
    private LocalDateTime dataVenda;

    @BeforeEach
    void setUp() {
        veiculoDisponivel = new Veiculo();
        veiculoDisponivel.setId(1L);
        veiculoDisponivel.setMarca("Toyota");
        veiculoDisponivel.setModelo("Corolla");
        veiculoDisponivel.setStatus(StatusVeiculo.DISPONIVEL);

        dataVenda = LocalDateTime.of(2025, 1, 18, 14, 30);
    }

    @Test
    @DisplayName("Deve vender veículo com sucesso")
    void deveVenderVeiculoComSucesso() {
        // Arrange
        String cpf = "123.456.789-00";
        Venda vendaEsperada = new Venda();
        vendaEsperada.setId(1L);
        vendaEsperada.setVeiculoId(1L);
        vendaEsperada.setCpfComprador(cpf);
        vendaEsperada.setDataVenda(dataVenda);

        when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculoDisponivel));
        when(vendaRepository.save(any(Venda.class))).thenReturn(vendaEsperada);

        // Act
        Venda resultado = venderVeiculoUseCase.execute(1L, cpf, dataVenda);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(cpf, resultado.getCpfComprador());
        verify(veiculoRepository, times(1)).findById(1L);
        verify(vendaRepository, times(1)).save(any(Venda.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando veículo não for encontrado")
    void deveLancarExcecaoQuandoVeiculoNaoEncontrado() {
        // Arrange
        when(veiculoRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        VeiculoNaoEncontradoException exception = assertThrows(
            VeiculoNaoEncontradoException.class,
            () -> venderVeiculoUseCase.execute(999L, "123.456.789-00", dataVenda)
        );

        assertEquals("Veículo não encontrado com ID: 999", exception.getMessage());
        verify(veiculoRepository, times(1)).findById(999L);
        verify(vendaRepository, never()).save(any(Venda.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando veículo não estiver disponível")
    void deveLancarExcecaoQuandoVeiculoNaoDisponivelParaVenda() {
        // Arrange
        Veiculo veiculoVendido = new Veiculo();
        veiculoVendido.setId(1L);
        veiculoVendido.setStatus(StatusVeiculo.VENDIDO);

        when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculoVendido));

        // Act & Assert
        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> venderVeiculoUseCase.execute(1L, "123.456.789-00", dataVenda)
        );

        assertEquals("Veículo não está disponível para venda", exception.getMessage());
        verify(veiculoRepository, times(1)).findById(1L);
        verify(vendaRepository, never()).save(any(Venda.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando CPF for nulo")
    void deveLancarExcecaoQuandoCpfForNulo() {
        // Arrange
        when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculoDisponivel));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> venderVeiculoUseCase.execute(1L, null, dataVenda)
        );

        assertEquals("CPF é obrigatório", exception.getMessage());
        verify(vendaRepository, never()).save(any(Venda.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando CPF for vazio")
    void deveLancarExcecaoQuandoCpfForVazio() {
        // Arrange
        when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculoDisponivel));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> venderVeiculoUseCase.execute(1L, "   ", dataVenda)
        );

        assertEquals("CPF é obrigatório", exception.getMessage());
        verify(vendaRepository, never()).save(any(Venda.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando CPF for inválido")
    void deveLancarExcecaoQuandoCpfForInvalido() {
        // Arrange
        when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculoDisponivel));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> venderVeiculoUseCase.execute(1L, "123", dataVenda)
        );

        assertEquals("CPF deve ter 11 dígitos", exception.getMessage());
        verify(vendaRepository, never()).save(any(Venda.class));
    }

    @Test
    @DisplayName("Deve aceitar CPF sem formatação")
    void deveAceitarCpfSemFormatacao() {
        // Arrange
        String cpfSemFormatacao = "12345678900";
        Venda vendaEsperada = new Venda();
        vendaEsperada.setId(1L);
        vendaEsperada.setVeiculoId(1L);
        vendaEsperada.setCpfComprador(cpfSemFormatacao);

        when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculoDisponivel));
        when(vendaRepository.save(any(Venda.class))).thenReturn(vendaEsperada);

        // Act
        Venda resultado = venderVeiculoUseCase.execute(1L, cpfSemFormatacao, dataVenda);

        // Assert
        assertNotNull(resultado);
        assertEquals(cpfSemFormatacao, resultado.getCpfComprador());
        verify(vendaRepository, times(1)).save(any(Venda.class));
    }
}

