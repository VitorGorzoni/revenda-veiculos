package org.com.revenda.domain.usecase;

import org.com.revenda.domain.dto.VendaComVeiculo;
import org.com.revenda.domain.repository.VendaRepository;
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
@DisplayName("Testes para ListarVeiculosVendidosUseCase")
class ListarVeiculosVendidosUseCaseTest {

    @Mock
    private VendaRepository vendaRepository;

    @InjectMocks
    private ListarVeiculosVendidosUseCase listarVeiculosVendidosUseCase;

    @Test
    @DisplayName("Deve listar veículos vendidos com sucesso")
    void deveListarVeiculosVendidosComSucesso() {
        // Arrange
        VendaComVeiculo venda1 = mock(VendaComVeiculo.class);
        VendaComVeiculo venda2 = mock(VendaComVeiculo.class);
        List<VendaComVeiculo> vendasEsperadas = Arrays.asList(venda1, venda2);

        when(vendaRepository.findVendasComVeiculosOrderByPreco()).thenReturn(vendasEsperadas);

        // Act
        List<VendaComVeiculo> resultado = listarVeiculosVendidosUseCase.execute();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(vendaRepository, times(1)).findVendasComVeiculosOrderByPreco();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não houver vendas")
    void deveRetornarListaVaziaQuandoNaoHouverVendas() {
        // Arrange
        when(vendaRepository.findVendasComVeiculosOrderByPreco()).thenReturn(Arrays.asList());

        // Act
        List<VendaComVeiculo> resultado = listarVeiculosVendidosUseCase.execute();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(vendaRepository, times(1)).findVendasComVeiculosOrderByPreco();
    }
}

