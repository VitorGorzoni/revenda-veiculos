package org.com.revenda.infrastructure.persistence.adapter;

import org.com.revenda.domain.entity.StatusVeiculo;
import org.com.revenda.domain.entity.Veiculo;
import org.com.revenda.infrastructure.persistence.entity.VeiculoJpaEntity;
import org.com.revenda.infrastructure.persistence.mapper.VeiculoMapper;
import org.com.revenda.infrastructure.persistence.repository.VeiculoJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para VeiculoRepositoryAdapter")
class VeiculoRepositoryAdapterTest {

    @Mock
    private VeiculoJpaRepository jpaRepository;

    @Mock
    private VeiculoMapper mapper;

    @InjectMocks
    private VeiculoRepositoryAdapter veiculoRepositoryAdapter;

    private Veiculo veiculo;
    private VeiculoJpaEntity veiculoEntity;

    @BeforeEach
    void setUp() {
        veiculo = new Veiculo();
        veiculo.setId(1L);
        veiculo.setMarca("Toyota");
        veiculo.setModelo("Corolla");

        veiculoEntity = new VeiculoJpaEntity();
        veiculoEntity.setId(1L);
        veiculoEntity.setMarca("Toyota");
        veiculoEntity.setModelo("Corolla");
    }

    @Test
    @DisplayName("Deve salvar veículo com sucesso")
    void deveSalvarVeiculoComSucesso() {
        // Arrange
        when(mapper.toJpaEntity(veiculo)).thenReturn(veiculoEntity);
        when(jpaRepository.save(veiculoEntity)).thenReturn(veiculoEntity);
        when(mapper.toDomainEntity(veiculoEntity)).thenReturn(veiculo);

        // Act
        Veiculo resultado = veiculoRepositoryAdapter.save(veiculo);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(mapper, times(1)).toJpaEntity(veiculo);
        verify(jpaRepository, times(1)).save(veiculoEntity);
        verify(mapper, times(1)).toDomainEntity(veiculoEntity);
    }

    @Test
    @DisplayName("Deve buscar veículo por ID")
    void deveBuscarVeiculoPorId() {
        // Arrange
        when(jpaRepository.findById(1L)).thenReturn(Optional.of(veiculoEntity));
        when(mapper.toDomainEntity(veiculoEntity)).thenReturn(veiculo);

        // Act
        Optional<Veiculo> resultado = veiculoRepositoryAdapter.findById(1L);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        verify(jpaRepository, times(1)).findById(1L);
        verify(mapper, times(1)).toDomainEntity(veiculoEntity);
    }

    @Test
    @DisplayName("Deve retornar Optional vazio quando veículo não encontrado")
    void deveRetornarOptionalVazioQuandoVeiculoNaoEncontrado() {
        // Arrange
        when(jpaRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Veiculo> resultado = veiculoRepositoryAdapter.findById(999L);

        // Assert
        assertFalse(resultado.isPresent());
        verify(jpaRepository, times(1)).findById(999L);
        verify(mapper, never()).toDomainEntity(any());
    }

    @Test
    @DisplayName("Deve listar todos os veículos")
    void deveListarTodosOsVeiculos() {
        // Arrange
        List<VeiculoJpaEntity> entities = Arrays.asList(veiculoEntity);
        when(jpaRepository.findAll()).thenReturn(entities);
        when(mapper.toDomainEntity(veiculoEntity)).thenReturn(veiculo);

        // Act
        List<Veiculo> resultado = veiculoRepositoryAdapter.findAll();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(jpaRepository, times(1)).findAll();
        verify(mapper, times(1)).toDomainEntity(veiculoEntity);
    }

    @Test
    @DisplayName("Deve buscar veículos por status")
    void deveBuscarVeiculosPorStatus() {
        // Arrange
        List<VeiculoJpaEntity> entities = Arrays.asList(veiculoEntity);
        when(jpaRepository.findByStatusOrderByPrecoAsc(StatusVeiculo.DISPONIVEL))
            .thenReturn(entities);
        when(mapper.toDomainEntity(veiculoEntity)).thenReturn(veiculo);

        // Act
        List<Veiculo> resultado = veiculoRepositoryAdapter
            .findByStatusOrderByPrecoAsc(StatusVeiculo.DISPONIVEL);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(jpaRepository, times(1))
            .findByStatusOrderByPrecoAsc(StatusVeiculo.DISPONIVEL);
        verify(mapper, times(1)).toDomainEntity(veiculoEntity);
    }
}

