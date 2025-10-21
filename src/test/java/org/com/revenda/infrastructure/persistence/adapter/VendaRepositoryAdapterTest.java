package org.com.revenda.infrastructure.persistence.adapter;

import org.com.revenda.domain.dto.VendaComVeiculo;
import org.com.revenda.domain.entity.Veiculo;
import org.com.revenda.domain.entity.Venda;
import org.com.revenda.infrastructure.persistence.entity.VeiculoJpaEntity;
import org.com.revenda.infrastructure.persistence.entity.VendaJpaEntity;
import org.com.revenda.infrastructure.persistence.mapper.VeiculoMapper;
import org.com.revenda.infrastructure.persistence.mapper.VendaMapper;
import org.com.revenda.infrastructure.persistence.repository.VendaJpaRepository;
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
@DisplayName("Testes para VendaRepositoryAdapter")
class VendaRepositoryAdapterTest {

    @Mock
    private VendaJpaRepository jpaRepository;

    @Mock
    private VendaMapper vendaMapper;

    @Mock
    private VeiculoMapper veiculoMapper;

    @InjectMocks
    private VendaRepositoryAdapter vendaRepositoryAdapter;

    private Venda venda;
    private VendaJpaEntity vendaEntity;
    private Veiculo veiculo;
    private VeiculoJpaEntity veiculoEntity;

    @BeforeEach
    void setUp() {
        venda = new Venda();
        venda.setId(1L);
        venda.setVeiculoId(1L);
        venda.setCpfComprador("123.456.789-00");

        vendaEntity = new VendaJpaEntity();
        vendaEntity.setId(1L);
        vendaEntity.setVeiculoId(1L);
        vendaEntity.setCpfComprador("123.456.789-00");

        veiculo = new Veiculo();
        veiculo.setId(1L);
        veiculo.setMarca("Toyota");

        veiculoEntity = new VeiculoJpaEntity();
        veiculoEntity.setId(1L);
        veiculoEntity.setMarca("Toyota");

        vendaEntity.setVeiculo(veiculoEntity);
    }

    @Test
    @DisplayName("Deve salvar venda com sucesso")
    void deveSalvarVendaComSucesso() {
        // Arrange
        when(vendaMapper.toJpaEntity(venda)).thenReturn(vendaEntity);
        when(jpaRepository.save(vendaEntity)).thenReturn(vendaEntity);
        when(vendaMapper.toDomainEntity(vendaEntity)).thenReturn(venda);

        // Act
        Venda resultado = vendaRepositoryAdapter.save(venda);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(vendaMapper, times(1)).toJpaEntity(venda);
        verify(jpaRepository, times(1)).save(vendaEntity);
        verify(vendaMapper, times(1)).toDomainEntity(vendaEntity);
    }

    @Test
    @DisplayName("Deve buscar venda por ID")
    void deveBuscarVendaPorId() {
        // Arrange
        when(jpaRepository.findById(1L)).thenReturn(Optional.of(vendaEntity));
        when(vendaMapper.toDomainEntity(vendaEntity)).thenReturn(venda);

        // Act
        Optional<Venda> resultado = vendaRepositoryAdapter.findById(1L);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        verify(jpaRepository, times(1)).findById(1L);
        verify(vendaMapper, times(1)).toDomainEntity(vendaEntity);
    }

    @Test
    @DisplayName("Deve buscar venda por código de pagamento")
    void deveBuscarVendaPorCodigoPagamento() {
        // Arrange
        String codigoPagamento = "PAG-ABC123";
        when(jpaRepository.findByCodigoPagamento(codigoPagamento))
            .thenReturn(Optional.of(vendaEntity));
        when(vendaMapper.toDomainEntity(vendaEntity)).thenReturn(venda);

        // Act
        Optional<Venda> resultado = vendaRepositoryAdapter.findByCodigoPagamento(codigoPagamento);

        // Assert
        assertTrue(resultado.isPresent());
        verify(jpaRepository, times(1)).findByCodigoPagamento(codigoPagamento);
        verify(vendaMapper, times(1)).toDomainEntity(vendaEntity);
    }

    @Test
    @DisplayName("Deve listar todas as vendas")
    void deveListarTodasAsVendas() {
        // Arrange
        List<VendaJpaEntity> entities = Arrays.asList(vendaEntity);
        when(jpaRepository.findAll()).thenReturn(entities);
        when(vendaMapper.toDomainEntity(vendaEntity)).thenReturn(venda);

        // Act
        List<Venda> resultado = vendaRepositoryAdapter.findAll();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(jpaRepository, times(1)).findAll();
        verify(vendaMapper, times(1)).toDomainEntity(vendaEntity);
    }

    @Test
    @DisplayName("Deve buscar vendas com veículos ordenadas por preço")
    void deveBuscarVendasComVeiculosOrderByPreco() {
        // Arrange
        List<VendaJpaEntity> entities = Arrays.asList(vendaEntity);
        when(jpaRepository.findVendasComVeiculosOrderByPreco()).thenReturn(entities);
        when(vendaMapper.toDomainEntity(vendaEntity)).thenReturn(venda);
        when(veiculoMapper.toDomainEntity(veiculoEntity)).thenReturn(veiculo);

        // Act
        List<VendaComVeiculo> resultado = vendaRepositoryAdapter.findVendasComVeiculosOrderByPreco();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(jpaRepository, times(1)).findVendasComVeiculosOrderByPreco();
        verify(vendaMapper, times(1)).toDomainEntity(vendaEntity);
        verify(veiculoMapper, times(1)).toDomainEntity(veiculoEntity);
    }
}

