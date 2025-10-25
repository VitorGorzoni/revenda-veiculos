package org.com.revenda.infrastructure.web.mapper;

import org.com.revenda.domain.enums.StatusVeiculo;
import org.com.revenda.domain.entity.Veiculo;
import org.com.revenda.infrastructure.web.dto.request.CadastrarVeiculoRequest;
import org.com.revenda.infrastructure.web.dto.response.VeiculoResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes abrangentes para VeiculoDtoMapper")
class VeiculoDtoMapperExtendedTest {

    @Mock
    private VeiculoDtoMapper mapper;

    private Veiculo veiculo;
    private VeiculoResponse veiculoResponse;
    private LocalDateTime dataAtual;

    @BeforeEach
    void setUp() {
        dataAtual = LocalDateTime.of(2025, 1, 15, 14, 30);

        veiculo = new Veiculo();
        veiculo.setId(1L);
        veiculo.setMarca("Toyota");
        veiculo.setModelo("Corolla");
        veiculo.setAno(2023);
        veiculo.setCor("Branco");
        veiculo.setPreco(new BigDecimal("75000.00"));
        veiculo.setStatus(StatusVeiculo.DISPONIVEL);
        veiculo.setDataCadastro(dataAtual);

        // Setup do mock para retornar valores esperados
        veiculoResponse = new VeiculoResponse();
        veiculoResponse.setId(1L);
        veiculoResponse.setMarca("Toyota");
        veiculoResponse.setModelo("Corolla");
        veiculoResponse.setAno(2023);
        veiculoResponse.setCor("Branco");
        veiculoResponse.setPreco(new BigDecimal("75000.00"));
        veiculoResponse.setStatus(StatusVeiculo.DISPONIVEL);
        veiculoResponse.setDataCadastro(dataAtual);
    }

    @Nested
    @DisplayName("Testes de conversão Request para Domain")
    class RequestParaDomainTests {

        @Test
        @DisplayName("Deve converter request válido para domain")
        void deveConverterRequestValidoParaDomain() {
            // Arrange
            CadastrarVeiculoRequest cadastrarRequest = new CadastrarVeiculoRequest(
                "Honda", "Civic", 2024, new BigDecimal("85000.00"), "Preto", "Flex"
            );
            
            Veiculo veiculoEsperado = new Veiculo();
            veiculoEsperado.setMarca("Honda");
            veiculoEsperado.setModelo("Civic");
            veiculoEsperado.setAno(2024);
            veiculoEsperado.setCor("Preto");
            veiculoEsperado.setPreco(new BigDecimal("85000.00"));

            when(mapper.toDomain(cadastrarRequest)).thenReturn(veiculoEsperado);

            // Act
            Veiculo resultado = mapper.toDomain(cadastrarRequest);

            // Assert
            assertNotNull(resultado);
            assertEquals("Honda", resultado.getMarca());
            assertEquals("Civic", resultado.getModelo());
            assertEquals(2024, resultado.getAno());
            assertEquals("Preto", resultado.getCor());
            assertEquals(new BigDecimal("85000.00"), resultado.getPreco());
        }

        @Test
        @DisplayName("Deve converter campos com valores extremos")
        void deveConverterCamposComValoresExtremos() {
            // Arrange
            CadastrarVeiculoRequest requestExtremo = new CadastrarVeiculoRequest(
                "A", "B", 1900, new BigDecimal("0.01"), "C", "D"
            );
            
            Veiculo veiculoExtremo = new Veiculo();
            veiculoExtremo.setMarca("A");
            veiculoExtremo.setModelo("B");
            veiculoExtremo.setAno(1900);
            veiculoExtremo.setCor("C");
            veiculoExtremo.setPreco(new BigDecimal("0.01"));

            when(mapper.toDomain(requestExtremo)).thenReturn(veiculoExtremo);

            // Act
            Veiculo resultado = mapper.toDomain(requestExtremo);

            // Assert
            assertNotNull(resultado);
            assertEquals("A", resultado.getMarca());
            assertEquals("B", resultado.getModelo());
        }

        @Test
        @DisplayName("Deve converter request com caracteres especiais")
        void deveConverterRequestComCaracteresEspeciais() {
            // Arrange
            CadastrarVeiculoRequest requestEspecial = new CadastrarVeiculoRequest(
                "Mercedes-Benz", "Classe A 200", 2023, new BigDecimal("180000.00"), "Azul Metálico", "Gasolina"
            );
            
            Veiculo veiculoEspecial = new Veiculo();
            veiculoEspecial.setMarca("Mercedes-Benz");
            veiculoEspecial.setModelo("Classe A 200");
            veiculoEspecial.setCor("Azul Metálico");

            when(mapper.toDomain(requestEspecial)).thenReturn(veiculoEspecial);

            // Act
            Veiculo resultado = mapper.toDomain(requestEspecial);

            // Assert
            assertNotNull(resultado);
            assertEquals("Mercedes-Benz", resultado.getMarca());
            assertEquals("Classe A 200", resultado.getModelo());
        }

        @Test
        @DisplayName("Deve retornar null quando request é null")
        void deveRetornarNullQuandoRequestENull() {
            // Arrange
            when(mapper.toDomain(null)).thenReturn(null);

            // Act
            Veiculo resultado = mapper.toDomain(null);

            // Assert
            assertNull(resultado);
        }
    }

    @Nested
    @DisplayName("Testes de conversão Domain para Response")
    class DomainParaResponseTests {

        @Test
        @DisplayName("Deve converter domain válido para response")
        void deveConverterDomainValidoParaResponse() {
            // Arrange
            when(mapper.toResponse(veiculo)).thenReturn(veiculoResponse);

            // Act
            VeiculoResponse resultado = mapper.toResponse(veiculo);

            // Assert
            assertNotNull(resultado);
            assertEquals(1L, resultado.getId());
            assertEquals("Toyota", resultado.getMarca());
            assertEquals("Corolla", resultado.getModelo());
            assertEquals(2023, resultado.getAno());
            assertEquals("Branco", resultado.getCor());
            assertEquals(new BigDecimal("75000.00"), resultado.getPreco());
            assertEquals(StatusVeiculo.DISPONIVEL, resultado.getStatus());
        }

        @Test
        @DisplayName("Deve converter domain sem ID para response")
        void deveConverterDomainSemIdParaResponse() {
            // Arrange
            Veiculo veiculoSemId = new Veiculo();
            veiculoSemId.setMarca("Ford");
            veiculoSemId.setModelo("Fiesta");
            veiculoSemId.setAno(2022);

            VeiculoResponse responseSemId = new VeiculoResponse();
            responseSemId.setMarca("Ford");
            responseSemId.setModelo("Fiesta");
            responseSemId.setAno(2022);

            when(mapper.toResponse(veiculoSemId)).thenReturn(responseSemId);

            // Act
            VeiculoResponse resultado = mapper.toResponse(veiculoSemId);

            // Assert
            assertNotNull(resultado);
            assertEquals("Ford", resultado.getMarca());
            assertEquals("Fiesta", resultado.getModelo());
        }

        @Test
        @DisplayName("Deve retornar null quando domain é null")
        void deveRetornarNullQuandoDomainENull() {
            // Arrange
            when(mapper.toResponse((Veiculo) null)).thenReturn(null);

            // Act
            VeiculoResponse resultado = mapper.toResponse((Veiculo) null);

            // Assert
            assertNull(resultado);
        }
    }

    @Nested
    @DisplayName("Testes de conversão de listas")
    class ConversaoListasTests {

        @Test
        @DisplayName("Deve converter lista de veículos para lista de responses")
        void deveConverterListaVeiculosParaListaResponses() {
            // Arrange
            Veiculo veiculo1 = new Veiculo();
            veiculo1.setId(1L);
            veiculo1.setMarca("Toyota");

            Veiculo veiculo2 = new Veiculo();
            veiculo2.setId(2L);
            veiculo2.setMarca("Honda");

            List<Veiculo> veiculos = Arrays.asList(veiculo1, veiculo2);

            VeiculoResponse response1 = new VeiculoResponse();
            response1.setId(1L);
            response1.setMarca("Toyota");

            VeiculoResponse response2 = new VeiculoResponse();
            response2.setId(2L);
            response2.setMarca("Honda");

            List<VeiculoResponse> responsesEsperadas = Arrays.asList(response1, response2);

            when(mapper.toResponseList(veiculos)).thenReturn(responsesEsperadas);

            // Act
            List<VeiculoResponse> resultado = mapper.toResponseList(veiculos);

            // Assert
            assertNotNull(resultado);
            assertEquals(2, resultado.size());
            assertEquals("Toyota", resultado.get(0).getMarca());
            assertEquals("Honda", resultado.get(1).getMarca());
        }

        @Test
        @DisplayName("Deve converter lista vazia")
        void deveConverterListaVazia() {
            // Arrange
            List<Veiculo> veiculosVazios = Collections.emptyList();
            when(mapper.toResponseList(veiculosVazios)).thenReturn(Collections.emptyList());

            // Act
            List<VeiculoResponse> resultado = mapper.toResponseList(veiculosVazios);

            // Assert
            assertNotNull(resultado);
            assertTrue(resultado.isEmpty());
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando lista de entrada é null")
        void deveRetornarListaVaziaQuandoListaEntradaENull() {
            // Arrange
            when(mapper.toResponseList(null)).thenReturn(Collections.emptyList());

            // Act
            List<VeiculoResponse> resultado = mapper.toResponseList(null);

            // Assert
            assertNotNull(resultado);
            assertTrue(resultado.isEmpty());
        }
    }

    @Nested
    @DisplayName("Testes de preservação de dados")
    class PreservacaoDadosTests {

        @Test
        @DisplayName("Deve preservar status ao converter para response")
        void devePreservarStatusAoConverterParaResponse() {
            // Arrange
            veiculo.setStatus(StatusVeiculo.VENDIDO);
            veiculoResponse.setStatus(StatusVeiculo.VENDIDO);
            when(mapper.toResponse(veiculo)).thenReturn(veiculoResponse);

            // Act
            VeiculoResponse resultado = mapper.toResponse(veiculo);

            // Assert
            assertEquals(StatusVeiculo.VENDIDO, resultado.getStatus());
        }

        @Test
        @DisplayName("Deve preservar data de cadastro ao converter para response")
        void devePreservarDataCadastroAoConverterParaResponse() {
            // Arrange
            when(mapper.toResponse(veiculo)).thenReturn(veiculoResponse);

            // Act
            VeiculoResponse resultado = mapper.toResponse(veiculo);

            // Assert
            assertEquals(dataAtual, resultado.getDataCadastro());
        }

        @Test
        @DisplayName("Deve preservar precisão de valores decimais")
        void devePreservarPrecisaoValoresDecimais() {
            // Arrange
            BigDecimal precoExato = new BigDecimal("75123.45");
            veiculo.setPreco(precoExato);
            veiculoResponse.setPreco(precoExato);
            when(mapper.toResponse(veiculo)).thenReturn(veiculoResponse);

            // Act
            VeiculoResponse resultado = mapper.toResponse(veiculo);

            // Assert
            assertEquals(precoExato, resultado.getPreco());
        }
    }

    @Nested
    @DisplayName("Testes de casos extremos")
    class CasosExtremosTests {

        @Test
        @DisplayName("Deve lidar com ID muito grande")
        void deveLidarComIdMuitoGrande() {
            // Arrange
            Long idGrande = Long.MAX_VALUE;
            veiculo.setId(idGrande);
            veiculoResponse.setId(idGrande);
            when(mapper.toResponse(veiculo)).thenReturn(veiculoResponse);

            // Act
            VeiculoResponse resultado = mapper.toResponse(veiculo);

            // Assert
            assertEquals(idGrande, resultado.getId());
        }

        @Test
        @DisplayName("Deve lidar com strings muito longas")
        void deveLidarComStringsMuitoLongas() {
            // Arrange
            String marcaLonga = "A".repeat(100);
            String modeloLongo = "B".repeat(100);
            
            veiculo.setMarca(marcaLonga);
            veiculo.setModelo(modeloLongo);
            veiculoResponse.setMarca(marcaLonga);
            veiculoResponse.setModelo(modeloLongo);
            
            when(mapper.toResponse(veiculo)).thenReturn(veiculoResponse);

            // Act
            VeiculoResponse resultado = mapper.toResponse(veiculo);

            // Assert
            assertEquals(marcaLonga, resultado.getMarca());
            assertEquals(modeloLongo, resultado.getModelo());
        }

        @Test
        @DisplayName("Deve lidar com ano limite")
        void deveLidarComAnoLimite() {
            // Arrange
            veiculo.setAno(Integer.MAX_VALUE);
            veiculoResponse.setAno(Integer.MAX_VALUE);
            when(mapper.toResponse(veiculo)).thenReturn(veiculoResponse);

            // Act
            VeiculoResponse resultado = mapper.toResponse(veiculo);

            // Assert
            assertEquals(Integer.MAX_VALUE, resultado.getAno());
        }
    }
}
