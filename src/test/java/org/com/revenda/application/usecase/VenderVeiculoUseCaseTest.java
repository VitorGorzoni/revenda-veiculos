package org.com.revenda.application.usecase;

import org.com.revenda.application.usecase.VenderVeiculoUseCase;
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

import java.math.BigDecimal;
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

    @BeforeEach
    void setUp() {
        veiculoDisponivel = new Veiculo();
        veiculoDisponivel.setId(1L);
        veiculoDisponivel.setMarca("Toyota");
        veiculoDisponivel.setModelo("Corolla");
        veiculoDisponivel.setStatus(StatusVeiculo.DISPONIVEL);
    }

    @Test
    @DisplayName("Deve vender veículo com sucesso")
    void deveVenderVeiculoComSucesso() {
        // Arrange
        String cpf = "123.456.789-00";
        String nome = "João Silva";
        BigDecimal valor = new BigDecimal("75000.00");

        Venda vendaEsperada = new Venda();
        vendaEsperada.setId(1L);
        vendaEsperada.setVeiculoId(1L);
        vendaEsperada.setCpfCliente(cpf);
        vendaEsperada.setNomeCliente(nome);
        vendaEsperada.setValorVenda(valor);

        when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculoDisponivel));
        when(vendaRepository.save(any(Venda.class))).thenReturn(vendaEsperada);

        // Act
        Venda resultado = venderVeiculoUseCase.execute(1L, cpf, nome, valor);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(cpf, resultado.getCpfCliente());
        assertEquals(nome, resultado.getNomeCliente());
        verify(veiculoRepository, times(1)).findById(1L);
        verify(vendaRepository, times(1)).save(any(Venda.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando veículo não for encontrado")
    void deveLancarExcecaoQuandoVeiculoNaoEncontrado() {
        // Arrange
        when(veiculoRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> venderVeiculoUseCase.execute(999L, "123.456.789-00", "João Silva", new BigDecimal("75000.00"))
        );

        assertTrue(exception.getMessage().contains("Veículo não encontrado"));
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
            () -> venderVeiculoUseCase.execute(1L, "123.456.789-00", "João Silva", new BigDecimal("75000.00"))
        );

        assertTrue(exception.getMessage().contains("não está disponível"));
        verify(veiculoRepository, times(1)).findById(1L);
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
        vendaEsperada.setCpfCliente(cpfSemFormatacao);
        vendaEsperada.setNomeCliente("João Silva");
        vendaEsperada.setValorVenda(new BigDecimal("75000.00"));

        when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculoDisponivel));
        when(vendaRepository.save(any(Venda.class))).thenReturn(vendaEsperada);

        // Act
        Venda resultado = venderVeiculoUseCase.execute(1L, cpfSemFormatacao, "João Silva", new BigDecimal("75000.00"));

        // Assert
        assertNotNull(resultado);
        assertEquals(cpfSemFormatacao, resultado.getCpfCliente());
        verify(veiculoRepository, times(1)).findById(1L);
        verify(vendaRepository, times(1)).save(any(Venda.class));
    }

    @Test
    @DisplayName("Deve aceitar CPF com formatação")
    void deveAceitarCpfComFormatacao() {
        // Arrange
        String cpfComFormatacao = "123.456.789-00";
        Venda vendaEsperada = new Venda();
        vendaEsperada.setId(1L);
        vendaEsperada.setVeiculoId(1L);
        vendaEsperada.setCpfCliente(cpfComFormatacao);
        vendaEsperada.setNomeCliente("João Silva");
        vendaEsperada.setValorVenda(new BigDecimal("75000.00"));

        when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculoDisponivel));
        when(vendaRepository.save(any(Venda.class))).thenReturn(vendaEsperada);

        // Act
        Venda resultado = venderVeiculoUseCase.execute(1L, cpfComFormatacao, "João Silva", new BigDecimal("75000.00"));

        // Assert
        assertNotNull(resultado);
        assertEquals(cpfComFormatacao, resultado.getCpfCliente());
        verify(veiculoRepository, times(1)).findById(1L);
        verify(vendaRepository, times(1)).save(any(Venda.class));
    }
}
