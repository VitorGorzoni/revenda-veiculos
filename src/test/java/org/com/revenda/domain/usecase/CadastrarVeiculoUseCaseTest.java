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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para CadastrarVeiculoUseCase")
class CadastrarVeiculoUseCaseTest {

    @Mock
    private VeiculoRepository veiculoRepository;

    @InjectMocks
    private CadastrarVeiculoUseCase cadastrarVeiculoUseCase;

    private Veiculo veiculo;

    @BeforeEach
    void setUp() {
        veiculo = new Veiculo();
        veiculo.setMarca("Toyota");
        veiculo.setModelo("Corolla");
        veiculo.setAno(2023);
        veiculo.setCor("Prata");
        veiculo.setPreco(120000.0);
    }

    @Test
    @DisplayName("Deve cadastrar um veículo com sucesso")
    void deveCadastrarVeiculoComSucesso() {
        // Arrange
        Veiculo veiculoSalvo = new Veiculo();
        veiculoSalvo.setId(1L);
        veiculoSalvo.setMarca(veiculo.getMarca());
        veiculoSalvo.setModelo(veiculo.getModelo());
        veiculoSalvo.setAno(veiculo.getAno());
        veiculoSalvo.setCor(veiculo.getCor());
        veiculoSalvo.setPreco(veiculo.getPreco());

        when(veiculoRepository.save(any(Veiculo.class))).thenReturn(veiculoSalvo);

        // Act
        Veiculo resultado = cadastrarVeiculoUseCase.execute(veiculo);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Toyota", resultado.getMarca());
        assertEquals("Corolla", resultado.getModelo());
        assertEquals(2023, resultado.getAno());
        assertEquals("Prata", resultado.getCor());
        assertEquals(120000.0, resultado.getPreco());
        verify(veiculoRepository, times(1)).save(veiculo);
    }

    @Test
    @DisplayName("Deve lançar exceção quando veículo for nulo")
    void deveLancarExcecaoQuandoVeiculoForNulo() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            cadastrarVeiculoUseCase.execute(null);
        });
    }

    @Test
    @DisplayName("Deve propagar exceção do repository")
    void devePropagarExcecaoDoRepository() {
        // Arrange
        when(veiculoRepository.save(any(Veiculo.class)))
            .thenThrow(new RuntimeException("Erro ao salvar no banco"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cadastrarVeiculoUseCase.execute(veiculo);
        });

        assertEquals("Erro ao salvar no banco", exception.getMessage());
        verify(veiculoRepository, times(1)).save(veiculo);
    }
}

