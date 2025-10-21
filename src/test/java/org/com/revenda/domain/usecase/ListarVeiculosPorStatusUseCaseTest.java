package org.com.revenda.domain.usecase;

import org.com.revenda.domain.entity.StatusVeiculo;
import org.com.revenda.domain.entity.Veiculo;
import org.com.revenda.domain.repository.VeiculoRepository;
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
@DisplayName("Testes para ListarVeiculosPorStatusUseCase")
class ListarVeiculosPorStatusUseCaseTest {

    @Mock
    private VeiculoRepository veiculoRepository;

    @InjectMocks
    private ListarVeiculosPorStatusUseCase listarVeiculosPorStatusUseCase;

    @Test
    @DisplayName("Deve listar veículos disponíveis com sucesso")
    void deveListarVeiculosDisponiveisComSucesso() {
        // Arrange
        Veiculo veiculo1 = new Veiculo();
        veiculo1.setId(1L);
        veiculo1.setMarca("Volkswagen");
        veiculo1.setStatus(StatusVeiculo.DISPONIVEL);

        Veiculo veiculo2 = new Veiculo();
        veiculo2.setId(2L);
        veiculo2.setMarca("Fiat");
        veiculo2.setStatus(StatusVeiculo.DISPONIVEL);

        List<Veiculo> veiculos = Arrays.asList(veiculo1, veiculo2);
        when(veiculoRepository.findByStatusOrderByPrecoAsc(StatusVeiculo.DISPONIVEL))
            .thenReturn(veiculos);

        // Act
        List<Veiculo> resultado = listarVeiculosPorStatusUseCase.execute(StatusVeiculo.DISPONIVEL);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(StatusVeiculo.DISPONIVEL, resultado.get(0).getStatus());
        verify(veiculoRepository, times(1)).findByStatusOrderByPrecoAsc(StatusVeiculo.DISPONIVEL);
    }

    @Test
    @DisplayName("Deve listar veículos vendidos com sucesso")
    void deveListarVeiculosVendidosComSucesso() {
        // Arrange
        Veiculo veiculo1 = new Veiculo();
        veiculo1.setId(1L);
        veiculo1.setMarca("Nissan");
        veiculo1.setStatus(StatusVeiculo.VENDIDO);

        List<Veiculo> veiculos = Arrays.asList(veiculo1);
        when(veiculoRepository.findByStatusOrderByPrecoAsc(StatusVeiculo.VENDIDO))
            .thenReturn(veiculos);

        // Act
        List<Veiculo> resultado = listarVeiculosPorStatusUseCase.execute(StatusVeiculo.VENDIDO);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(StatusVeiculo.VENDIDO, resultado.get(0).getStatus());
        verify(veiculoRepository, times(1)).findByStatusOrderByPrecoAsc(StatusVeiculo.VENDIDO);
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não houver veículos com o status")
    void deveRetornarListaVaziaQuandoNaoHouverVeiculosComStatus() {
        // Arrange
        when(veiculoRepository.findByStatusOrderByPrecoAsc(StatusVeiculo.DISPONIVEL))
            .thenReturn(Arrays.asList());

        // Act
        List<Veiculo> resultado = listarVeiculosPorStatusUseCase.execute(StatusVeiculo.DISPONIVEL);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(veiculoRepository, times(1)).findByStatusOrderByPrecoAsc(StatusVeiculo.DISPONIVEL);
    }
}

