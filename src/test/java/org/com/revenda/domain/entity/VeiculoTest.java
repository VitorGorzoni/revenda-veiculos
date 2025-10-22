package org.com.revenda.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes para a entidade Veiculo")
class VeiculoTest {

    @Test
    @DisplayName("Deve criar veículo e verificar se está disponível")
    void deveCriarVeiculoEVerificarSeEstaDisponivel() {
        // Arrange
        Veiculo veiculo = new Veiculo();
        veiculo.setStatus(StatusVeiculo.DISPONIVEL);

        // Act
        boolean resultado = veiculo.isDisponivel();

        // Assert
        assertTrue(resultado);
    }

    @Test
    @DisplayName("Deve retornar falso quando veículo está vendido")
    void deveRetornarFalsoQuandoVeiculoVendido() {
        // Arrange
        Veiculo veiculo = new Veiculo();
        veiculo.setStatus(StatusVeiculo.VENDIDO);

        // Act
        boolean resultado = veiculo.isDisponivel();

        // Assert
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Deve marcar veículo como vendido")
    void deveMarcarVeiculoComoVendido() {
        // Arrange
        Veiculo veiculo = new Veiculo();
        veiculo.setStatus(StatusVeiculo.DISPONIVEL);

        // Act
        veiculo.vender();

        // Assert
        assertEquals(StatusVeiculo.VENDIDO, veiculo.getStatus());
        assertFalse(veiculo.isDisponivel());
    }

    @Test
    @DisplayName("Deve definir e obter todos os campos")
    void deveDefinirEObterTodosCampos() {
        // Arrange
        Veiculo veiculo = new Veiculo();

        // Act
        veiculo.setId(1L);
        veiculo.setMarca("Toyota");
        veiculo.setModelo("Corolla");
        veiculo.setAno(2023);
        veiculo.setCor("Prata");
        veiculo.setPreco(new BigDecimal("120000.00"));
        veiculo.setStatus(StatusVeiculo.DISPONIVEL);

        // Assert
        assertEquals(1L, veiculo.getId());
        assertEquals("Toyota", veiculo.getMarca());
        assertEquals("Corolla", veiculo.getModelo());
        assertEquals(2023, veiculo.getAno());
        assertEquals("Prata", veiculo.getCor());
        assertEquals(0, new BigDecimal("120000.00").compareTo(veiculo.getPreco()));
        assertEquals(StatusVeiculo.DISPONIVEL, veiculo.getStatus());
    }
}
