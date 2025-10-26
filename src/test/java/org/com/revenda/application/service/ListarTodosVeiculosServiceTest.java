package org.com.revenda.application.service;

import org.com.revenda.application.gateway.VeiculoPersistenceGateway;
import org.com.revenda.domain.entity.Veiculo;
import org.com.revenda.domain.enums.StatusVeiculo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do Serviço: Listar Todos os Veículos")
class ListarTodosVeiculosServiceTest {

    @Mock
    private VeiculoPersistenceGateway veiculoPersistenceGateway;

    @InjectMocks
    private ListarTodosVeiculosService listarTodosVeiculosService;

    private List<Veiculo> veiculos;

    @BeforeEach
    void setUp() {
        Veiculo veiculo1 = new Veiculo("Toyota", "Corolla", 2023, "Branco", new BigDecimal("75000.00"));
        veiculo1.setId(1L);

        Veiculo veiculo2 = new Veiculo("Honda", "Civic", 2022, "Preto", new BigDecimal("80000.00"));
        veiculo2.setId(2L);

        veiculos = Arrays.asList(veiculo1, veiculo2);
    }

    @Test
    @DisplayName("Deve listar todos os veículos disponíveis ordenados por preço")
    void deveListarTodosVeiculos() {
        // Given
        when(veiculoPersistenceGateway.findByStatusOrderByPrecoAsc(StatusVeiculo.DISPONIVEL)).thenReturn(veiculos);

        // When
        List<Veiculo> resultado = listarTodosVeiculosService.execute();

        // Then
        assertThat(resultado).isNotEmpty();
        assertThat(resultado).hasSize(2);
        verify(veiculoPersistenceGateway).findByStatusOrderByPrecoAsc(StatusVeiculo.DISPONIVEL);
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não houver veículos disponíveis")
    void deveRetornarListaVaziaQuandoNaoHouverVeiculos() {
        // Given
        when(veiculoPersistenceGateway.findByStatusOrderByPrecoAsc(StatusVeiculo.DISPONIVEL)).thenReturn(Collections.emptyList());

        // When
        List<Veiculo> resultado = listarTodosVeiculosService.execute();

        // Then
        assertThat(resultado).isEmpty();
        verify(veiculoPersistenceGateway).findByStatusOrderByPrecoAsc(StatusVeiculo.DISPONIVEL);
    }
}
