ackage org.com.revenda.infrastructure.persistence.mapper;

import org.com.revenda.domain.entity.StatusVeiculo;
import org.com.revenda.domain.entity.Veiculo;
import org.com.revenda.infrastructure.persistence.entity.VeiculoJpaEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes para VeiculoMapper")
class VeiculoMapperTest {

    private VeiculoMapper veiculoMapper;

    @BeforeEach
    void setUp() {
        veiculoMapper = new VeiculoMapper();
    }

    @Test
    @DisplayName("Deve converter Veiculo para VeiculoJpaEntity")
    void deveConverterVeiculoParaVeiculoJpaEntity() {
        // Arrange
        Veiculo veiculo = new Veiculo();
        veiculo.setId(1L);
        veiculo.setMarca("Toyota");
        veiculo.setModelo("Corolla");
        veiculo.setAno(2023);
        veiculo.setCor("Prata");
        veiculo.setPreco(120000.0);
        veiculo.setStatus(StatusVeiculo.DISPONIVEL);
        veiculo.setDataCadastro(LocalDateTime.now());

        // Act
        VeiculoJpaEntity entity = veiculoMapper.toJpaEntity(veiculo);

        // Assert
        assertNotNull(entity);
        assertEquals(1L, entity.getId());
        assertEquals("Toyota", entity.getMarca());
        assertEquals("Corolla", entity.getModelo());
        assertEquals(2023, entity.getAno());
        assertEquals("Prata", entity.getCor());
        assertEquals(120000.0, entity.getPreco());
        assertEquals(StatusVeiculo.DISPONIVEL, entity.getStatus());
    }

    @Test
    @DisplayName("Deve retornar null quando Veiculo for null")
    void deveRetornarNullQuandoVeiculoForNull() {
        // Act
        VeiculoJpaEntity entity = veiculoMapper.toJpaEntity(null);

        // Assert
        assertNull(entity);
    }

    @Test
    @DisplayName("Deve converter VeiculoJpaEntity para Veiculo")
    void deveConverterVeiculoJpaEntityParaVeiculo() {
        // Arrange
        VeiculoJpaEntity entity = new VeiculoJpaEntity();
        entity.setId(2L);
        entity.setMarca("Honda");
        entity.setModelo("Civic");
        entity.setAno(2022);
        entity.setCor("Preto");
        entity.setPreco(100000.0);
        entity.setStatus(StatusVeiculo.VENDIDO);
        entity.setDataCadastro(LocalDateTime.now());

        // Act
        Veiculo veiculo = veiculoMapper.toDomainEntity(entity);

        // Assert
        assertNotNull(veiculo);
        assertEquals(2L, veiculo.getId());
        assertEquals("Honda", veiculo.getMarca());
        assertEquals("Civic", veiculo.getModelo());
        assertEquals(2022, veiculo.getAno());
        assertEquals("Preto", veiculo.getCor());
        assertEquals(100000.0, veiculo.getPreco());
        assertEquals(StatusVeiculo.VENDIDO, veiculo.getStatus());
    }

    @Test
    @DisplayName("Deve retornar null quando VeiculoJpaEntity for null")
    void deveRetornarNullQuandoVeiculoJpaEntityForNull() {
        // Act
        Veiculo veiculo = veiculoMapper.toDomainEntity(null);

        // Assert
        assertNull(veiculo);
    }
}

