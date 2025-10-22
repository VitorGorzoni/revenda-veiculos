package org.com.revenda.application.usecase;

import org.com.revenda.application.usecase.*;

import org.com.revenda.application.usecase.ListarVeiculosVendidosUseCase;
import org.com.revenda.domain.entity.Venda;
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
    @DisplayName("Deve listar vendas com sucesso")
    void deveListarVendasComSucesso() {
        // Arrange
        Venda venda1 = new Venda();
        venda1.setId(1L);

        Venda venda2 = new Venda();
        venda2.setId(2L);

        List<Venda> vendasEsperadas = Arrays.asList(venda1, venda2);

        when(vendaRepository.findAllOrderByValorVendaDesc()).thenReturn(vendasEsperadas);

        // Act
        List<Venda> resultado = listarVeiculosVendidosUseCase.execute();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(vendaRepository, times(1)).findAllOrderByValorVendaDesc();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não houver vendas")
    void deveRetornarListaVaziaQuandoNaoHouverVendas() {
        // Arrange
        when(vendaRepository.findAllOrderByValorVendaDesc()).thenReturn(Arrays.asList());

        // Act
        List<Venda> resultado = listarVeiculosVendidosUseCase.execute();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(vendaRepository, times(1)).findAllOrderByValorVendaDesc();
    }
}

