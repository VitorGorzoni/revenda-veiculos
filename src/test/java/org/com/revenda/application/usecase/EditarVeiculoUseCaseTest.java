package org.com.revenda.application.usecase;

import org.com.revenda.application.usecase.*;

import org.com.revenda.application.usecase.EditarVeiculoUseCase;
import org.com.revenda.domain.entity.StatusVeiculo;
import org.com.revenda.domain.entity.Veiculo;
import org.com.revenda.domain.exception.VeiculoNaoEncontradoException;
import org.com.revenda.domain.repository.VeiculoRepository;
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
@DisplayName("Testes para EditarVeiculoUseCase")
class EditarVeiculoUseCaseTest {

    @Mock
    private VeiculoRepository veiculoRepository;

    @InjectMocks
    private EditarVeiculoUseCase editarVeiculoUseCase;

    private Veiculo veiculoExistente;
    private Veiculo veiculoAtualizado;

    @BeforeEach
    void setUp() {
        veiculoExistente = new Veiculo();
        veiculoExistente.setId(1L);
        veiculoExistente.setMarca("Toyota");
        veiculoExistente.setModelo("Corolla");
        veiculoExistente.setAno(2020);
        veiculoExistente.setCor("Prata");
        veiculoExistente.setPreco(new BigDecimal("100000.00"));
        veiculoExistente.setStatus(StatusVeiculo.DISPONIVEL);

        veiculoAtualizado = new Veiculo();
        veiculoAtualizado.setMarca("Toyota");
        veiculoAtualizado.setModelo("Corolla");
        veiculoAtualizado.setAno(2023);
        veiculoAtualizado.setCor("Preto");
        veiculoAtualizado.setPreco(new BigDecimal("120000.00"));
    }

    @Test
    @DisplayName("Deve editar veículo com sucesso")
    void deveEditarVeiculoComSucesso() {
        // Arrange
        when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculoExistente));
        when(veiculoRepository.save(any(Veiculo.class))).thenReturn(veiculoExistente);

        // Act
        Veiculo resultado = editarVeiculoUseCase.execute(1L, veiculoAtualizado);

        // Assert
        assertNotNull(resultado);
        assertEquals(2023, resultado.getAno());
        assertEquals("Preto", resultado.getCor());
        assertEquals(0, new BigDecimal("120000.00").compareTo(resultado.getPreco()));
        verify(veiculoRepository, times(1)).findById(1L);
        verify(veiculoRepository, times(1)).save(veiculoExistente);
    }

    @Test
    @DisplayName("Deve lançar exceção quando veículo não for encontrado")
    void deveLancarExcecaoQuandoVeiculoNaoEncontrado() {
        // Arrange
        when(veiculoRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        VeiculoNaoEncontradoException exception = assertThrows(
            VeiculoNaoEncontradoException.class,
            () -> editarVeiculoUseCase.execute(999L, veiculoAtualizado)
        );

        assertEquals("Veículo não encontrado com ID: 999", exception.getMessage());
        verify(veiculoRepository, times(1)).findById(999L);
        verify(veiculoRepository, never()).save(any(Veiculo.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando tentar editar veículo vendido")
    void deveLancarExcecaoQuandoTentarEditarVeiculoVendido() {
        // Arrange
        veiculoExistente.setStatus(StatusVeiculo.VENDIDO);
        when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculoExistente));

        // Act & Assert
        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> editarVeiculoUseCase.execute(1L, veiculoAtualizado)
        );

        assertEquals("Não é possível editar um veículo que não está disponível", exception.getMessage());
        verify(veiculoRepository, times(1)).findById(1L);
        verify(veiculoRepository, never()).save(any(Veiculo.class));
    }
}

