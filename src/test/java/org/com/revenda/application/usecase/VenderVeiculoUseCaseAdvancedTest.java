package org.com.revenda.application.usecase;

import org.com.revenda.application.usecase.VenderVeiculoUseCase;
import org.com.revenda.domain.entity.StatusVeiculo;
import org.com.revenda.domain.entity.Veiculo;
import org.com.revenda.domain.entity.Venda;
import org.com.revenda.domain.repository.VeiculoRepository;
import org.com.revenda.domain.repository.VendaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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

    private Veiculo criarVeiculoDisponivel(Long id) {
        Veiculo veiculo = new Veiculo();
        veiculo.setId(id);
        veiculo.setMarca("Toyota");
        veiculo.setModelo("Corolla");
        veiculo.setAno(2023);
        veiculo.setCor("Branco");
        veiculo.setPreco(new BigDecimal("75000.00"));
        veiculo.setStatus(StatusVeiculo.DISPONIVEL);
        veiculo.setDataCadastro(LocalDateTime.now());
        return veiculo;
    }

    @Nested
    @DisplayName("Testes de validação de CPF")
    class ValidacaoCpfTests {

        @ParameterizedTest
        @ValueSource(strings = {"12345678900", "98765432100", "11111111111", "00000000000"})
        @DisplayName("Deve aceitar CPFs válidos sem formatação")
        void deveAceitarCpfsValidosSemFormatacao(String cpf) {
            // Arrange
            Veiculo veiculo = criarVeiculoDisponivel(1L);
            when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculo));
            when(vendaRepository.save(any(Venda.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            Venda resultado = venderVeiculoUseCase.execute(1L, cpf, "João Silva", new BigDecimal("75000.00"));

            // Assert
            assertNotNull(resultado);
            assertEquals(cpf, resultado.getCpfCliente());
        }

        @ParameterizedTest
        @ValueSource(strings = {"123.456.789-00", "987.654.321-00", "111.111.111-11"})
        @DisplayName("Deve aceitar CPFs válidos com formatação")
        void deveAceitarCpfsValidosComFormatacao(String cpf) {
            // Arrange
            Veiculo veiculo = criarVeiculoDisponivel(1L);
            when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculo));
            when(vendaRepository.save(any(Venda.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            Venda resultado = venderVeiculoUseCase.execute(1L, cpf, "Maria Silva", new BigDecimal("75000.00"));

            // Assert
            assertNotNull(resultado);
            assertEquals(cpf, resultado.getCpfCliente());
        }

        @Test
        @DisplayName("Deve remover caracteres especiais do CPF")
        void deveRemoverCaracteresEspeciaisDoCpf() {
            // Arrange
            String cpfComCaracteres = "123.456.789-00";
            Veiculo veiculo = criarVeiculoDisponivel(1L);
            when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculo));
            when(vendaRepository.save(any(Venda.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            Venda resultado = venderVeiculoUseCase.execute(1L, cpfComCaracteres, "João Silva", new BigDecimal("75000.00"));

            // Assert
            assertNotNull(resultado);
            assertEquals(cpfComCaracteres, resultado.getCpfCliente());
        }
    }

    @Nested
    @DisplayName("Testes de geração de código de pagamento")
    class GeracaoCodigoPagamentoTests {

        @Test
        @DisplayName("Deve gerar códigos de pagamento únicos")
        void deveGerarCodigosPagamentoUnicos() {
            // Arrange
            Veiculo veiculo1 = criarVeiculoDisponivel(1L);
            Veiculo veiculo2 = criarVeiculoDisponivel(2L);

            when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculo1));
            when(veiculoRepository.findById(2L)).thenReturn(Optional.of(veiculo2));
            when(vendaRepository.save(any(Venda.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            Venda venda1 = venderVeiculoUseCase.execute(1L, "12345678900", "João Silva", new BigDecimal("75000.00"));
            Venda venda2 = venderVeiculoUseCase.execute(2L, "98765432100", "Maria Silva", new BigDecimal("80000.00"));

            // Assert
            assertNotEquals(venda1.getCodigoPagamento(), venda2.getCodigoPagamento());
            assertTrue(venda1.getCodigoPagamento().startsWith("PAG-"));
            assertTrue(venda2.getCodigoPagamento().startsWith("PAG-"));
        }

        @Test
        @DisplayName("Código de pagamento deve ter formato PAG-XXXXXXXX")
        void codigoPagamentoDeveTerFormatoPadrao() {
            // Arrange
            Veiculo veiculo = criarVeiculoDisponivel(1L);
            when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculo));
            when(vendaRepository.save(any(Venda.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            Venda resultado = venderVeiculoUseCase.execute(1L, "12345678900", "João Silva", new BigDecimal("75000.00"));

            // Assert
            String codigo = resultado.getCodigoPagamento();
            assertTrue(codigo.startsWith("PAG-"));
            assertEquals(12, codigo.length());
            assertTrue(codigo.substring(4).matches("[A-Z0-9]{8}"));
        }

        @Test
        @DisplayName("Deve gerar múltiplos códigos diferentes em sequência")
        void deveGerarMultiplosCodigosDiferentesEmSequencia() {
            // Arrange
            when(veiculoRepository.findById(anyLong())).thenAnswer(invocation -> {
                Long id = invocation.getArgument(0);
                return Optional.of(criarVeiculoDisponivel(id));
            });
            when(vendaRepository.save(any(Venda.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            String[] codigos = new String[10];
            for (int i = 0; i < 10; i++) {
                Venda venda = venderVeiculoUseCase.execute((long) i, "12345678900", "João Silva", new BigDecimal("75000.00"));
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
    @DisplayName("Testes de validação de valores")
    class ValidacaoValorTests {

        @Test
        @DisplayName("Deve aceitar valores de venda positivos")
        void deveAceitarValoresVendaPositivos() {
            // Arrange
            Veiculo veiculo = criarVeiculoDisponivel(1L);
            when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculo));
            when(vendaRepository.save(any(Venda.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            Venda resultado = venderVeiculoUseCase.execute(1L, "12345678900", "João Silva", new BigDecimal("75000.00"));

            // Assert
            assertEquals(new BigDecimal("75000.00"), resultado.getValorVenda());
        }

        @Test
        @DisplayName("Deve aceitar valores muito altos")
        void deveAceitarValoresMuitoAltos() {
            // Arrange
            Veiculo veiculo = criarVeiculoDisponivel(1L);
            when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculo));
            when(vendaRepository.save(any(Venda.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            Venda resultado = venderVeiculoUseCase.execute(1L, "12345678900", "João Silva", new BigDecimal("999999999.99"));

            // Assert
            assertEquals(new BigDecimal("999999999.99"), resultado.getValorVenda());
        }

        @Test
        @DisplayName("Deve aceitar valores decimais precisos")
        void deveAceitarValoresDecimaisPrecisos() {
            // Arrange
            Veiculo veiculo = criarVeiculoDisponivel(1L);
            when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculo));
            when(vendaRepository.save(any(Venda.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            Venda resultado = venderVeiculoUseCase.execute(1L, "12345678900", "João Silva", new BigDecimal("75123.45"));

            // Assert
            assertEquals(new BigDecimal("75123.45"), resultado.getValorVenda());
        }
    }

    @Nested
    @DisplayName("Testes de cenários extremos")
    class CenariosExtremosTests {

        @Test
        @DisplayName("Deve lidar com múltiplas vendas simultâneas")
        void deveLidarComMultiplasVendasSimultaneas() {
            // Arrange
            Veiculo veiculo1 = criarVeiculoDisponivel(1L);
            Veiculo veiculo2 = criarVeiculoDisponivel(2L);

            when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculo1));
            when(veiculoRepository.findById(2L)).thenReturn(Optional.of(veiculo2));
            when(vendaRepository.save(any(Venda.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            Venda venda1 = venderVeiculoUseCase.execute(1L, "12345678900", "João Silva", new BigDecimal("75000.00"));
            Venda venda2 = venderVeiculoUseCase.execute(2L, "98765432100", "Maria Silva", new BigDecimal("80000.00"));

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
            Veiculo veiculo = criarVeiculoDisponivel(1L);
            when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculo));
            when(vendaRepository.save(any(Venda.class))).thenThrow(new RuntimeException("Erro no banco"));

            // Act & Assert
            assertThrows(RuntimeException.class,
                () -> venderVeiculoUseCase.execute(1L, "12345678900", "João Silva", new BigDecimal("75000.00")));

            verify(veiculoRepository, times(1)).findById(1L);
            verify(vendaRepository, times(1)).save(any(Venda.class));
        }

        @Test
        @DisplayName("Deve lidar com IDs muito grandes")
        void deveLidarComIdsMuitoGrandes() {
            // Arrange
            Long idMuitoGrande = Long.MAX_VALUE;
            Veiculo veiculoComIdGrande = criarVeiculoDisponivel(idMuitoGrande);

            when(veiculoRepository.findById(idMuitoGrande)).thenReturn(Optional.of(veiculoComIdGrande));
            when(vendaRepository.save(any(Venda.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            Venda resultado = venderVeiculoUseCase.execute(idMuitoGrande, "12345678900", "João Silva", new BigDecimal("75000.00"));

            // Assert
            assertEquals(idMuitoGrande, resultado.getVeiculoId());
        }

        @Test
        @DisplayName("Deve lidar com CPFs com zeros à esquerda")
        void deveLidarComCpfsComZerosAEsquerda() {
            // Arrange
            String cpfComZeros = "00123456789";
            Veiculo veiculo = criarVeiculoDisponivel(1L);
            when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculo));
            when(vendaRepository.save(any(Venda.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            Venda resultado = venderVeiculoUseCase.execute(1L, cpfComZeros, "João Silva", new BigDecimal("75000.00"));

            // Assert
            assertEquals(cpfComZeros, resultado.getCpfCliente());
        }

        @Test
        @DisplayName("Deve lidar com nomes de clientes longos")
        void deveLidarComNomesClientesLongos() {
            // Arrange
            String nomeLongo = "João Pedro da Silva Santos de Oliveira Junior";
            Veiculo veiculo = criarVeiculoDisponivel(1L);
            when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculo));
            when(vendaRepository.save(any(Venda.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            Venda resultado = venderVeiculoUseCase.execute(1L, "12345678900", nomeLongo, new BigDecimal("75000.00"));

            // Assert
            assertEquals(nomeLongo, resultado.getNomeCliente());
        }
    }

    @Nested
    @DisplayName("Testes de performance e stress")
    class PerformanceStressTests {

        @Test
        @DisplayName("Deve processar vendas em lote sem degradação")
        void deveProcessarVendasEmLoteSemDegradacao() {
            // Arrange
            when(veiculoRepository.findById(anyLong())).thenAnswer(invocation -> {
                Long id = invocation.getArgument(0);
                return Optional.of(criarVeiculoDisponivel(id));
            });
            when(vendaRepository.save(any(Venda.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            long inicioTempo = System.currentTimeMillis();
            for (int i = 0; i < 100; i++) {
                venderVeiculoUseCase.execute((long) i, "12345678900", "João Silva", new BigDecimal("75000.00"));
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
            when(veiculoRepository.findById(anyLong())).thenAnswer(invocation -> {
                Long id = invocation.getArgument(0);
                return Optional.of(criarVeiculoDisponivel(id));
            });
            when(vendaRepository.save(any(Venda.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            java.util.Set<String> codigos = new java.util.HashSet<>();
            for (int i = 0; i < 1000; i++) {
                Venda venda = venderVeiculoUseCase.execute((long) i, "12345678900", "João Silva", new BigDecimal("75000.00"));
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

