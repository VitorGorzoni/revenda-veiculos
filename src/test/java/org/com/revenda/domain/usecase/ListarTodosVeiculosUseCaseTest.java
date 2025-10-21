package org.com.revenda.domain.usecase;

import org.com.revenda.domain.entity.Veiculo;
import org.com.revenda.domain.repository.VeiculoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para ListarTodosVeiculosUseCase")
class ListarTodosVeiculosUseCaseTest {

    @Mock
    private VeiculoRepository veiculoRepository;

    @InjectMocks
    private ListarTodosVeiculosUseCase listarTodosVeiculosUseCase;

    @Test
    @DisplayName("Deve listar todos os veículos com sucesso")
    void deveListarTodosVeiculosComSucesso() {
        // Arrange
        Veiculo veiculo1 = new Veiculo();
        veiculo1.setId(1L);
        veiculo1.setMarca("Ford");
        veiculo1.setModelo("Focus");

        Veiculo veiculo2 = new Veiculo();
        veiculo2.setId(2L);
        veiculo2.setMarca("Chevrolet");
        veiculo2.setModelo("Onix");

        List<Veiculo> veiculos = Arrays.asList(veiculo1, veiculo2);
        when(veiculoRepository.findAll()).thenReturn(veiculos);

        // Act
        List<Veiculo> resultado = listarTodosVeiculosUseCase.execute();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Ford", resultado.get(0).getMarca());
        assertEquals("Chevrolet", resultado.get(1).getMarca());
        verify(veiculoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não houver veículos")
    void deveRetornarListaVaziaQuandoNaoHouverVeiculos() {
        // Arrange
        when(veiculoRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<Veiculo> resultado = listarTodosVeiculosUseCase.execute();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(veiculoRepository, times(1)).findAll();
    }
}

