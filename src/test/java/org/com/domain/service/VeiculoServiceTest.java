package org.com.domain.service;

import org.com.domain.entity.StatusPagamento;
import org.com.domain.entity.StatusVeiculo;
import org.com.domain.entity.Veiculo;
import org.com.domain.exception.PagamentoNotFoundException;
import org.com.domain.exception.VeiculoJaVendidoException;
import org.com.domain.exception.VeiculoNotFoundException;
import org.com.domain.repository.VeiculoRepository;
import org.com.domain.usecase.CadastrarVeiculoUseCase;
import org.com.domain.usecase.EditarVeiculoUseCase;
import org.com.domain.usecase.EfetuarVendaVeiculoUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VeiculoServiceTest {

    @Mock
    private VeiculoRepository veiculoRepository;

    private CadastrarVeiculoUseCase cadastrarVeiculoUseCase;
    private EditarVeiculoUseCase editarVeiculoUseCase;
    private EfetuarVendaVeiculoUseCase efetuarVendaVeiculoUseCase;

    private VeiculoRequestDTO veiculoRequestDTO;
    private Veiculo veiculo;
    private VendaRequestDTO vendaRequestDTO;

    @BeforeEach
    void setUp() {
        veiculoRequestDTO = VeiculoRequestDTO.builder()
                .marca("Toyota")
                .modelo("Corolla")
                .ano(2020)
                .cor("Branco")
                .preco(new BigDecimal("85000.00"))
                .build();

        veiculo = Veiculo.builder()
                .id(1L)
                .marca("Toyota")
                .modelo("Corolla")
                .ano(2020)
                .cor("Branco")
                .preco(new BigDecimal("85000.00"))
                .status(StatusVeiculo.DISPONIVEL)
                .createdAt(LocalDateTime.now())
                .build();

        vendaRequestDTO = VendaRequestDTO.builder()
                .cpfComprador("12345678901")
                .build();

        cadastrarVeiculoUseCase = new CadastrarVeiculoUseCase(veiculoRepository);
        editarVeiculoUseCase = new EditarVeiculoUseCase(veiculoRepository);
        efetuarVendaVeiculoUseCase = new EfetuarVendaVeiculoUseCase(veiculoRepository);
    }

    @Test
    void cadastrarVeiculo_DeveRetornarVeiculoCadastrado() {
        // Arrange
        when(veiculoRepository.save(any(Veiculo.class))).thenReturn(veiculo);

        // Act
        Veiculo resultado = veiculoService.cadastrarVeiculo(veiculoRequestDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals("Toyota", resultado.getMarca());
        assertEquals("Corolla", resultado.getModelo());
        assertEquals(StatusVeiculo.DISPONIVEL, resultado.getStatus());
        verify(veiculoRepository, times(1)).save(any(Veiculo.class));
    }

    @Test
    void buscarVeiculoPorId_QuandoExiste_DeveRetornarVeiculo() {
        // Arrange
        when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculo));

        // Act
        Veiculo resultado = veiculoService.buscarVeiculoPorId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Toyota", resultado.getMarca());
    }

    @Test
    void buscarVeiculoPorId_QuandoNaoExiste_DeveLancarExcecao() {
        // Arrange
        when(veiculoRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(VeiculoNotFoundException.class,
                () -> veiculoService.buscarVeiculoPorId(1L));
    }

    @Test
    void venderVeiculo_QuandoDisponivel_DeveRealizarVenda() {
        // Arrange
        when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculo));
        when(veiculoRepository.save(any(Veiculo.class))).thenReturn(veiculo);

        // Act
        Veiculo resultado = veiculoService.venderVeiculo(1L, vendaRequestDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals("12345678901", resultado.getCpfComprador());
        assertEquals(StatusVeiculo.RESERVADO, resultado.getStatus());
        assertEquals(StatusPagamento.PENDENTE, resultado.getStatusPagamento());
        assertNotNull(resultado.getDataVenda());
        assertNotNull(resultado.getCodigoPagamento());
    }

    @Test
    void venderVeiculo_QuandoJaVendido_DeveLancarExcecao() {
        // Arrange
        veiculo.setStatus(StatusVeiculo.VENDIDO);
        when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculo));

        // Act & Assert
        assertThrows(VeiculoJaVendidoException.class,
                () -> veiculoService.venderVeiculo(1L, vendaRequestDTO));
    }

    @Test
    void processarWebhookPagamento_QuandoPagamentoAprovado_DeveFinalizarVenda() {
        // Arrange
        veiculo.setStatus(StatusVeiculo.RESERVADO);
        veiculo.setCodigoPagamento("codigo-123");

        PagamentoWebhookDTO webhookDTO = PagamentoWebhookDTO.builder()
                .codigoPagamento("codigo-123")
                .statusPagamento(StatusPagamento.APROVADO)
                .build();

        when(veiculoRepository.findByCodigoPagamento("codigo-123")).thenReturn(Optional.of(veiculo));
        when(veiculoRepository.save(any(Veiculo.class))).thenReturn(veiculo);

        // Act
        veiculoService.processarWebhookPagamento(webhookDTO);

        // Assert
        assertEquals(StatusVeiculo.VENDIDO, veiculo.getStatus());
        assertEquals(StatusPagamento.APROVADO, veiculo.getStatusPagamento());
    }

    @Test
    void processarWebhookPagamento_QuandoPagamentoCancelado_DeveVoltarParaDisponivel() {
        // Arrange
        veiculo.setStatus(StatusVeiculo.RESERVADO);
        veiculo.setCodigoPagamento("codigo-123");
        veiculo.setCpfComprador("12345678901");

        PagamentoWebhookDTO webhookDTO = PagamentoWebhookDTO.builder()
                .codigoPagamento("codigo-123")
                .statusPagamento(StatusPagamento.CANCELADO)
                .build();

        when(veiculoRepository.findByCodigoPagamento("codigo-123")).thenReturn(Optional.of(veiculo));
        when(veiculoRepository.save(any(Veiculo.class))).thenReturn(veiculo);

        // Act
        veiculoService.processarWebhookPagamento(webhookDTO);

        // Assert
        assertEquals(StatusVeiculo.DISPONIVEL, veiculo.getStatus());
        assertEquals(StatusPagamento.CANCELADO, veiculo.getStatusPagamento());
        assertNull(veiculo.getCpfComprador());
        assertNull(veiculo.getDataVenda());
        assertNull(veiculo.getCodigoPagamento());
    }

    @Test
    void listarVeiculosDisponiveis_DeveRetornarListaOrdenadaPorPreco() {
        // Arrange
        List<Veiculo> veiculos = List.of(veiculo);
        when(veiculoRepository.findByStatusOrderByPrecoAsc(StatusVeiculo.DISPONIVEL)).thenReturn(veiculos);

        // Act
        List<Veiculo> resultado = veiculoService.listarVeiculosDisponiveis();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(StatusVeiculo.DISPONIVEL, resultado.get(0).getStatus());
    }

    @Test
    void deveCadastrarVeiculoComSucesso() {
        // Arrange
        Veiculo veiculo = new Veiculo("Toyota", "Corolla", 2023, "Branco", new BigDecimal("80000.00"));
        veiculo.setId(1L);

        when(veiculoRepository.save(any(Veiculo.class))).thenReturn(veiculo);

        // Act
        Veiculo resultado = cadastrarVeiculoUseCase.execute(veiculo);

        // Assert
        assertNotNull(resultado);
        assertEquals("Toyota", resultado.getMarca());
        assertEquals("Corolla", resultado.getModelo());
        assertEquals(2023, resultado.getAno());
        assertEquals("Branco", resultado.getCor());
        assertEquals(new BigDecimal("80000.00"), resultado.getPreco());
        assertEquals(StatusVeiculo.DISPONIVEL, resultado.getStatus());
    }

    @Test
    void deveRejeitarVeiculoComDadosInvalidos() {
        // Arrange
        Veiculo veiculo = new Veiculo("", "", null, "", null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            cadastrarVeiculoUseCase.execute(veiculo);
        });
    }

    @Test
    void deveEditarVeiculoComSucesso() {
        // Arrange
        Long veiculoId = 1L;
        Veiculo veiculoExistente = new Veiculo("Toyota", "Corolla", 2022, "Branco", new BigDecimal("75000.00"));
        veiculoExistente.setId(veiculoId);

        Veiculo veiculoAtualizado = new Veiculo("Toyota", "Corolla", 2023, "Preto", new BigDecimal("80000.00"));

        when(veiculoRepository.findById(veiculoId)).thenReturn(Optional.of(veiculoExistente));
        when(veiculoRepository.save(any(Veiculo.class))).thenReturn(veiculoExistente);

        // Act
        Veiculo resultado = editarVeiculoUseCase.execute(veiculoId, veiculoAtualizado);

        // Assert
        assertNotNull(resultado);
        assertEquals("Preto", resultado.getCor());
        assertEquals(new BigDecimal("80000.00"), resultado.getPreco());
    }

    @Test
    void deveEfetuarVendaComSucesso() {
        // Arrange
        Long veiculoId = 1L;
        Veiculo veiculo = new Veiculo("Toyota", "Corolla", 2023, "Branco", new BigDecimal("80000.00"));
        veiculo.setId(veiculoId);
        veiculo.setStatus(StatusVeiculo.DISPONIVEL);

        when(veiculoRepository.findById(veiculoId)).thenReturn(Optional.of(veiculo));
        when(veiculoRepository.save(any(Veiculo.class))).thenReturn(veiculo);

        // Act
        Veiculo resultado = efetuarVendaVeiculoUseCase.execute(veiculoId, "12345678901", LocalDateTime.now());

        // Assert
        assertNotNull(resultado);
        assertEquals(StatusVeiculo.VENDIDO, resultado.getStatus());
        assertNotNull(resultado.getVenda());
        assertEquals("12345678901", resultado.getVenda().getCpfComprador());
    }

    @Test
    void deveRejeitarVendaDeVeiculoJaVendido() {
        // Arrange
        Long veiculoId = 1L;
        Veiculo veiculo = new Veiculo("Toyota", "Corolla", 2023, "Branco", new BigDecimal("80000.00"));
        veiculo.setId(veiculoId);
        veiculo.setStatus(StatusVeiculo.VENDIDO);

        when(veiculoRepository.findById(veiculoId)).thenReturn(Optional.of(veiculo));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> {
            efetuarVendaVeiculoUseCase.execute(veiculoId, "12345678901", LocalDateTime.now());
        });
    }
}
