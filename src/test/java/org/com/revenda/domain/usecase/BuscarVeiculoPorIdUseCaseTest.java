package org.com.revenda.domain.usecase;

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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para BuscarVeiculoPorIdUseCase")
class BuscarVeiculoPorIdUseCaseTest {

    @Mock
    private VeiculoRepository veiculoRepository;

    @InjectMocks
    private BuscarVeiculoPorIdUseCase buscarVeiculoPorIdUseCase;

    private Veiculo veiculo;

    @BeforeEach
    void setUp() {
        veiculo = new Veiculo();
        veiculo.setId(1L);
        veiculo.setMarca("Honda");
        veiculo.setModelo("Civic");
        veiculo.setAno(2022);
        veiculo.setCor("Preto");
        veiculo.setPreco(100000.0);
    }

    @Test
    @DisplayName("Deve buscar veículo por ID com sucesso")
    void deveBuscarVeiculoPorIdComSucesso() {
        // Arrange
        when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculo));

        // Act
        Veiculo resultado = buscarVeiculoPorIdUseCase.execute(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Honda", resultado.getMarca());
        assertEquals("Civic", resultado.getModelo());
        verify(veiculoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção quando veículo não for encontrado")
    void deveLancarExcecaoQuandoVeiculoNaoEncontrado() {
        // Arrange
        when(veiculoRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        VeiculoNaoEncontradoException exception = assertThrows(
            VeiculoNaoEncontradoException.class,
            () -> buscarVeiculoPorIdUseCase.execute(999L)
        );

        assertEquals("Veículo não encontrado com ID: 999", exception.getMessage());
        verify(veiculoRepository, times(1)).findById(999L);
    }
}

