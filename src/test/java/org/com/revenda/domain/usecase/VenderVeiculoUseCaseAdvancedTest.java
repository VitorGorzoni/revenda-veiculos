package org.com.revenda.domain.usecase;

import org.com.revenda.domain.entity.StatusVeiculo;
import org.com.revenda.domain.entity.Veiculo;
import org.com.revenda.domain.entity.Venda;
import org.com.revenda.domain.exception.VeiculoNaoEncontradoException;
import org.com.revenda.domain.repository.VeiculoRepository;
import org.com.revenda.domain.repository.VendaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes abrangentes para VenderVeiculoUseCase")
class VenderVeiculoUseCaseAdvancedTest {

    @Mock
    private VeiculoRepository veiculoRepository;

    @Mock
    private VendaRepository vendaRepository;

    @InjectMocks
    private VenderVeiculoUseCase venderVeiculoUseCase;

    private Veiculo veiculoDisponivel;
    private LocalDateTime dataVenda;

    @BeforeEach
    void setUp() {
        veiculoDisponivel = new Veiculo();
        veiculoDisponivel.setId(1L);
        veiculoDisponivel.setMarca("Toyota");
        veiculoDisponivel.setModelo("Corolla");
        veiculoDisponivel.setAno(2023);
        veiculoDisponivel.setCor("Branco");
        veiculoDisponivel.setPreco(new BigDecimal("75000.00"));
        veiculoDisponivel.setStatus(StatusVeiculo.DISPONIVEL);
        veiculoDisponivel.setDataCadastro(LocalDateTime.now());

        dataVenda = LocalDateTime.of(2025, 1, 15, 14, 30);
    }

    @Nested
    @DisplayName("Testes de validação de CPF")
    class ValidacaoCpfTests {

        @ParameterizedTest
        @ValueSource(strings = {"12345678900", "98765432100", "11111111111", "00000000000"})
        @DisplayName("Deve aceitar CPFs válidos sem formatação")
        void deveAceitarCpfsValidosSemFormatacao(String cpf) {
            // Arrange
            Venda vendaEsperada = new Venda(1L, cpf, dataVenda, "PAG-12345678");
            when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculoDisponivel));
            when(vendaRepository.save(any(Venda.class))).thenReturn(vendaEsperada);

            // Act
            Venda resultado = venderVeiculoUseCase.execute(1L, cpf, dataVenda);

            // Assert
            assertNotNull(resultado);
            assertEquals(cpf, resultado.getCpfComprador());
        }

        @ParameterizedTest
        @ValueSource(strings = {"123.456.789-00", "987.654.321-00", "111.111.111-11"})
        @DisplayName("Deve aceitar CPFs válidos com formatação")
        void deveAceitarCpfsValidosComFormatacao(String cpf) {
            // Arrange
            Venda vendaEsperada = new Venda(1L, cpf, dataVenda, "PAG-12345678");
            when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculoDisponivel));
            when(vendaRepository.save(any(Venda.class))).thenReturn(vendaEsperada);

            // Act
            Venda resultado = venderVeiculoUseCase.execute(1L, cpf, dataVenda);

            // Assert
            assertNotNull(resultado);
            assertEquals(cpf, resultado.getCpfComprador());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"", "   ", "\t", "\n"})
        @DisplayName("Deve lançar exceção para CPFs nulos ou vazios")
        void deveLancarExcecaoParaCpfsNulosOuVazios(String cpf) {
            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> venderVeiculoUseCase.execute(1L, cpf, dataVenda)
            );

            assertEquals("CPF é obrigatório", exception.getMessage());
            verify(veiculoRepository, never()).findById(any());
        }

        @ParameterizedTest
        @ValueSource(strings = {"123", "12345678", "123456789012", "abcdefghijk"})
        @DisplayName("Deve lançar exceção para CPFs com tamanho inválido")
        void deveLancarExcecaoParaCpfsComTamanhoInvalido(String cpf) {
            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> venderVeiculoUseCase.execute(1L, cpf, dataVenda)
            );

            assertEquals("CPF deve ter 11 dígitos", exception.getMessage());
            verify(veiculoRepository, never()).findById(any());
        }

        @ParameterizedTest
        @ValueSource(strings = {"123.456.789", "123-456-78", "123/456/78"})
        @DisplayName("Deve lançar exceção para CPFs com formatação inválida")
        void deveLancarExcecaoParaCpfsComFormatacaoInvalida(String cpf) {
            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> venderVeiculoUseCase.execute(1L, cpf, dataVenda)
            );

            assertEquals("CPF deve ter 11 dígitos", exception.getMessage());
            verify(veiculoRepository, never()).findById(any());
        }

        @Test
        @DisplayName("Deve remover caracteres especiais do CPF")
        void deveRemoverCaracteresEspeciaisDoCpf() {
            // Arrange
            String cpfComCaracteres = "123.456.789-00";
            String cpfLimpo = "12345678900";
            Venda vendaEsperada = new Venda(1L, cpfComCaracteres, dataVenda, "PAG-12345678");

            when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculoDisponivel));
            when(vendaRepository.save(any(Venda.class))).thenReturn(vendaEsperada);

            // Act
            Venda resultado = venderVeiculoUseCase.execute(1L, cpfComCaracteres, dataVenda);

            // Assert
            assertNotNull(resultado);
            assertEquals(cpfComCaracteres, resultado.getCpfComprador());
        }
    }

    @Nested
    @DisplayName("Testes de geração de código de pagamento")
    class GeracaoCodigoPagamentoTests {

        @Test
        @DisplayName("Deve gerar códigos de pagamento únicos")
        void deveGerarCodigosPagamentoUnicos() {
            // Arrange
            when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculoDisponivel));
            when(vendaRepository.save(any(Venda.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            Venda venda1 = venderVeiculoUseCase.execute(1L, "12345678900", dataVenda);
            Venda venda2 = venderVeiculoUseCase.execute(1L, "98765432100", dataVenda);

            // Assert
            assertNotEquals(venda1.getCodigoPagamento(), venda2.getCodigoPagamento());
            assertTrue(venda1.getCodigoPagamento().startsWith("PAG-"));
            assertTrue(venda2.getCodigoPagamento().startsWith("PAG-"));
        }

        @Test
        @DisplayName("Código de pagamento deve ter formato PAG-XXXXXXXX")
        void codigoPagamentoDeveTerFormatoPadrao() {
            // Arrange
            when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculoDisponivel));
            when(vendaRepository.save(any(Venda.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            Venda resultado = venderVeiculoUseCase.execute(1L, "12345678900", dataVenda);

            // Assert
            String codigo = resultado.getCodigoPagamento();
            assertTrue(codigo.startsWith("PAG-"));
            assertEquals(12, codigo.length()); // PAG- + 8 caracteres
            assertTrue(codigo.substring(4).matches("[A-Z0-9]{8}"));
        }

        @Test
        @DisplayName("Deve gerar múltiplos códigos diferentes em sequência")
        void deveGerarMultiplosCodigosDiferentesEmSequencia() {
            // Arrange
            when(veiculoRepository.findById(anyLong())).thenReturn(Optional.of(veiculoDisponivel));
            when(vendaRepository.save(any(Venda.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            String[] codigos = new String[10];
            for (int i = 0; i < 10; i++) {
                Venda venda = venderVeiculoUseCase.execute(1L, "12345678900", dataVenda);
                codigos[i] = venda.getCodigoPagamento();
            }

            // Assert
            for (int i = 0; i < codigos.length; i++) {
                for (int j = i + 1; j < codigos.length; j++) {
                    assertNotEquals(codigos[i], codigos[j],
                        "Códigos " + i + " e " + j + " são iguais: " + codigos[i]);
                }
            }
        }
    }

    @Nested
    @DisplayName("Testes de validação de datas")
    class ValidacaoDataTests {

        @Test
        @DisplayName("Deve aceitar data de venda no passado")
        void deveAceitarDataVendaNoPassado() {
            // Arrange
            LocalDateTime dataPassado = LocalDateTime.of(2024, 1, 1, 10, 0);
            Venda vendaEsperada = new Venda(1L, "12345678900", dataPassado, "PAG-12345678");

            when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculoDisponivel));
            when(vendaRepository.save(any(Venda.class))).thenReturn(vendaEsperada);

            // Act
            Venda resultado = venderVeiculoUseCase.execute(1L, "12345678900", dataPassado);

            // Assert
            assertEquals(dataPassado, resultado.getDataVenda());
        }

        @Test
        @DisplayName("Deve aceitar data de venda no futuro")
        void deveAceitarDataVendaNoFuturo() {
            // Arrange
            LocalDateTime dataFuturo = LocalDateTime.of(2026, 12, 31, 23, 59);
            Venda vendaEsperada = new Venda(1L, "12345678900", dataFuturo, "PAG-12345678");

            when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculoDisponivel));
            when(vendaRepository.save(any(Venda.class))).thenReturn(vendaEsperada);

            // Act
            Venda resultado = venderVeiculoUseCase.execute(1L, "12345678900", dataFuturo);

            // Assert
            assertEquals(dataFuturo, resultado.getDataVenda());
        }

        @Test
        @DisplayName("Deve aceitar data de venda atual")
        void deveAceitarDataVendaAtual() {
            // Arrange
            LocalDateTime agora = LocalDateTime.now();
            Venda vendaEsperada = new Venda(1L, "12345678900", agora, "PAG-12345678");

            when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculoDisponivel));
            when(vendaRepository.save(any(Venda.class))).thenReturn(vendaEsperada);

            // Act
            Venda resultado = venderVeiculoUseCase.execute(1L, "12345678900", agora);

            // Assert
            assertEquals(agora, resultado.getDataVenda());
        }
    }

    @Nested
    @DisplayName("Testes de cenários extremos")
    class CenariosExtremosTests {

        @Test
        @DisplayName("Deve lidar com múltiplas vendas simultâneas")
        void deveLidarComMultiplasVendasSimultaneas() {
            // Arrange
            when(veiculoRepository.findById(anyLong())).thenReturn(Optional.of(veiculoDisponivel));
            when(vendaRepository.save(any(Venda.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            Venda venda1 = venderVeiculoUseCase.execute(1L, "12345678900", dataVenda);
            Venda venda2 = venderVeiculoUseCase.execute(2L, "98765432100", dataVenda);

            // Assert
            assertNotNull(venda1);
            assertNotNull(venda2);
            assertNotEquals(venda1.getCodigoPagamento(), venda2.getCodigoPagamento());
            verify(vendaRepository, times(2)).save(any(Venda.class));
        }

        @Test
        @DisplayName("Deve manter integridade dos dados após erro no repositório")
        void deveManterIntegridadeDadosAposErroRepositorio() {
            // Arrange
            when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculoDisponivel));
            when(vendaRepository.save(any(Venda.class))).thenThrow(new RuntimeException("Erro no banco"));

            // Act & Assert
            assertThrows(RuntimeException.class,
                () -> venderVeiculoUseCase.execute(1L, "12345678900", dataVenda));

            verify(veiculoRepository, times(1)).findById(1L);
            verify(vendaRepository, times(1)).save(any(Venda.class));
        }

        @Test
        @DisplayName("Deve lidar com IDs muito grandes")
        void deveLidarComIdsMuitoGrandes() {
            // Arrange
            Long idMuitoGrande = Long.MAX_VALUE;
            Veiculo veiculoComIdGrande = new Veiculo();
            veiculoComIdGrande.setId(idMuitoGrande);
            veiculoComIdGrande.setStatus(StatusVeiculo.DISPONIVEL);

            Venda vendaEsperada = new Venda(idMuitoGrande, "12345678900", dataVenda, "PAG-12345678");

            when(veiculoRepository.findById(idMuitoGrande)).thenReturn(Optional.of(veiculoComIdGrande));
            when(vendaRepository.save(any(Venda.class))).thenReturn(vendaEsperada);

            // Act
            Venda resultado = venderVeiculoUseCase.execute(idMuitoGrande, "12345678900", dataVenda);

            // Assert
            assertEquals(idMuitoGrande, resultado.getVeiculoId());
        }

        @Test
        @DisplayName("Deve lidar com CPFs com zeros à esquerda")
        void deveLidarComCpfsComZerosAEsquerda() {
            // Arrange
            String cpfComZeros = "00123456789";
            Venda vendaEsperada = new Venda(1L, cpfComZeros, dataVenda, "PAG-12345678");

            when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculoDisponivel));
            when(vendaRepository.save(any(Venda.class))).thenReturn(vendaEsperada);

            // Act
            Venda resultado = venderVeiculoUseCase.execute(1L, cpfComZeros, dataVenda);

            // Assert
            assertEquals(cpfComZeros, resultado.getCpfComprador());
        }

        @Test
        @DisplayName("Deve lidar com datas com precisão de nanossegundos")
        void deveLidarComDatasComPrecisaoNanossegundos() {
            // Arrange
            LocalDateTime dataComNanos = LocalDateTime.of(2025, 6, 15, 14, 30, 45, 123456789);
            Venda vendaEsperada = new Venda(1L, "12345678900", dataComNanos, "PAG-12345678");

            when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculoDisponivel));
            when(vendaRepository.save(any(Venda.class))).thenReturn(vendaEsperada);

            // Act
            Venda resultado = venderVeiculoUseCase.execute(1L, "12345678900", dataComNanos);

            // Assert
            assertEquals(dataComNanos, resultado.getDataVenda());
        }
    }

    @Nested
    @DisplayName("Testes de performance e stress")
    class PerformanceStressTests {

        @Test
        @DisplayName("Deve processar vendas em lote sem degradação")
        void deveProcessarVendasEmLoteSemDegradacao() {
            // Arrange
            when(veiculoRepository.findById(anyLong())).thenReturn(Optional.of(veiculoDisponivel));
            when(vendaRepository.save(any(Venda.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            long inicioTempo = System.currentTimeMillis();
            for (int i = 0; i < 100; i++) {
                venderVeiculoUseCase.execute(1L, "12345678900", dataVenda.plusSeconds(i));
            }
            long fimTempo = System.currentTimeMillis();

            // Assert
            long tempoTotal = fimTempo - inicioTempo;
            assertTrue(tempoTotal < 5000, "Tempo de processamento muito alto: " + tempoTotal + "ms");
            verify(vendaRepository, times(100)).save(any(Venda.class));
        }

        @Test
        @DisplayName("Deve manter qualidade dos códigos de pagamento em alta demanda")
        void deveManterQualidadeCodigosPagamentoAltaDemanda() {
            // Arrange
            when(veiculoRepository.findById(anyLong())).thenReturn(Optional.of(veiculoDisponivel));
            when(vendaRepository.save(any(Venda.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            java.util.Set<String> codigos = new java.util.HashSet<>();
            for (int i = 0; i < 1000; i++) {
                Venda venda = venderVeiculoUseCase.execute(1L, "12345678900", dataVenda);
                codigos.add(venda.getCodigoPagamento());
            }

            // Assert
            assertEquals(1000, codigos.size(), "Códigos duplicados encontrados");
            codigos.forEach(codigo -> {
                assertTrue(codigo.startsWith("PAG-"));
                assertEquals(12, codigo.length());
            });
        }
    }
}
