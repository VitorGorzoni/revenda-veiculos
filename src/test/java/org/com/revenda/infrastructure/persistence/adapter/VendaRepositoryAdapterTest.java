package org.com.revenda.infrastructure.persistence.adapter;

import org.com.revenda.domain.entity.Venda;
import org.com.revenda.infrastructure.persistence.entity.VendaJpaEntity;
import org.com.revenda.infrastructure.persistence.mapper.VendaMapper;
import org.com.revenda.infrastructure.persistence.repository.VendaJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
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

    @InjectMocks
    private VendaPersistenceAdapter vendaPersistenceAdapter;

    private Venda venda;
    private VendaJpaEntity vendaEntity;

    @BeforeEach
    void setUp() {
        venda = new Venda();
        venda.setId(1L);
        venda.setVeiculoId(1L);
        venda.setCpfCliente("123.456.789-00");
        venda.setNomeCliente("João Silva");
        venda.setValorVenda(new BigDecimal("75000.00"));

        vendaEntity = new VendaJpaEntity();
        vendaEntity.setId(1L);
        vendaEntity.setVeiculoId(1L);
        vendaEntity.setCpfCliente("123.456.789-00");
        vendaEntity.setNomeCliente("João Silva");
        vendaEntity.setValorVenda(new BigDecimal("75000.00"));
    }

    @Test
    @DisplayName("Deve salvar venda com sucesso")
    void deveSalvarVendaComSucesso() {
        // Arrange
        when(vendaMapper.toJpaEntity(venda)).thenReturn(vendaEntity);
        when(jpaRepository.save(vendaEntity)).thenReturn(vendaEntity);
        when(vendaMapper.toDomainEntity(vendaEntity)).thenReturn(venda);

        // Act
        Venda resultado = vendaPersistenceAdapter.save(venda);

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
        Optional<Venda> resultado = vendaPersistenceAdapter.findById(1L);

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
        Optional<Venda> resultado = vendaPersistenceAdapter.findByCodigoPagamento(codigoPagamento);

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
        List<Venda> resultado = vendaPersistenceAdapter.findAll();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(jpaRepository, times(1)).findAll();
        verify(vendaMapper, times(1)).toDomainEntity(vendaEntity);
    }
}
