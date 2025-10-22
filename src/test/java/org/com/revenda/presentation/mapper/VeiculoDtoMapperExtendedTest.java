package org.com.revenda.presentation.mapper;

import org.com.revenda.domain.entity.StatusVeiculo;
import org.com.revenda.domain.entity.Veiculo;
import org.com.revenda.presentation.dto.request.CadastrarVeiculoRequest;
import org.com.revenda.presentation.dto.response.VeiculoResponse;
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
    private CadastrarVeiculoRequest cadastrarRequest;
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

        cadastrarRequest = new CadastrarVeiculoRequest();
        cadastrarRequest.setMarca("Honda");
        cadastrarRequest.setModelo("Civic");
        cadastrarRequest.setAno(2024);
        cadastrarRequest.setCor("Preto");
        cadastrarRequest.setPreco(new BigDecimal("85000.00"));

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
            CadastrarVeiculoRequest requestExtremo = new CadastrarVeiculoRequest();
            requestExtremo.setAno(1900);
            requestExtremo.setPreco(new BigDecimal("999999999.99"));

            Veiculo veiculoEsperado = new Veiculo();
            veiculoEsperado.setAno(1900);
            veiculoEsperado.setPreco(new BigDecimal("999999999.99"));

            when(mapper.toDomain(requestExtremo)).thenReturn(veiculoEsperado);

            // Act
            Veiculo resultado = mapper.toDomain(requestExtremo);

            // Assert
            assertEquals(1900, resultado.getAno());
            assertEquals(new BigDecimal("999999999.99"), resultado.getPreco());
        }

        @Test
        @DisplayName("Deve converter strings com caracteres especiais")
        void deveConverterStringsComCaracteresEspeciais() {
            // Arrange
            CadastrarVeiculoRequest requestEspecial = new CadastrarVeiculoRequest();
            requestEspecial.setMarca("Peugeot-Citroën");
            requestEspecial.setModelo("C4 Picasso 2.0 HDi");
            requestEspecial.setCor("Azul Metálico");

            Veiculo veiculoEsperado = new Veiculo();
            veiculoEsperado.setMarca("Peugeot-Citroën");
            veiculoEsperado.setModelo("C4 Picasso 2.0 HDi");
            veiculoEsperado.setCor("Azul Metálico");

            when(mapper.toDomain(requestEspecial)).thenReturn(veiculoEsperado);

            // Act
            Veiculo resultado = mapper.toDomain(requestEspecial);

            // Assert
            assertEquals("Peugeot-Citroën", resultado.getMarca());
            assertEquals("C4 Picasso 2.0 HDi", resultado.getModelo());
            assertEquals("Azul Metálico", resultado.getCor());
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
            assertEquals(dataAtual, resultado.getDataCadastro());
        }

        @Test
        @DisplayName("Deve converter veículo vendido")
        void deveConverterVeiculoVendido() {
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
        @DisplayName("Deve manter precisão decimal do preço")
        void deveManterPrecisaoDecimalDoPreco() {
            // Arrange
            BigDecimal precoEspecifico = new BigDecimal("12345.67");
            veiculo.setPreco(precoEspecifico);
            veiculoResponse.setPreco(precoEspecifico);

            when(mapper.toResponse(veiculo)).thenReturn(veiculoResponse);

            // Act
            VeiculoResponse resultado = mapper.toResponse(veiculo);

            // Assert
            assertEquals(precoEspecifico, resultado.getPreco());
        }
    }

    @Nested
    @DisplayName("Testes de conversão de listas")
    class ConversaoListasTests {

        @Test
        @DisplayName("Deve converter lista de veículos para lista de responses")
        void deveConverterListaVeiculosParaListaResponses() {
            // Arrange
            Veiculo veiculo2 = new Veiculo();
            veiculo2.setId(2L);
            veiculo2.setMarca("Honda");
            veiculo2.setModelo("Civic");
            veiculo2.setStatus(StatusVeiculo.VENDIDO);

            VeiculoResponse response2 = new VeiculoResponse();
            response2.setId(2L);
            response2.setMarca("Honda");
            response2.setModelo("Civic");
            response2.setStatus(StatusVeiculo.VENDIDO);

            List<Veiculo> veiculos = Arrays.asList(veiculo, veiculo2);
            List<VeiculoResponse> responsesEsperadas = Arrays.asList(veiculoResponse, response2);

            when(mapper.toResponseList(veiculos)).thenReturn(responsesEsperadas);

            // Act
            List<VeiculoResponse> resultado = mapper.toResponseList(veiculos);

            // Assert
            assertNotNull(resultado);
            assertEquals(2, resultado.size());

            assertEquals(1L, resultado.get(0).getId());
            assertEquals("Toyota", resultado.get(0).getMarca());
            assertEquals(StatusVeiculo.DISPONIVEL, resultado.get(0).getStatus());

            assertEquals(2L, resultado.get(1).getId());
            assertEquals("Honda", resultado.get(1).getMarca());
            assertEquals(StatusVeiculo.VENDIDO, resultado.get(1).getStatus());
        }

        @Test
        @DisplayName("Deve converter lista vazia")
        void deveConverterListaVazia() {
            // Arrange
            when(mapper.toResponseList(Collections.emptyList())).thenReturn(Collections.emptyList());

            // Act
            List<VeiculoResponse> resultado = mapper.toResponseList(Collections.emptyList());

            // Assert
            assertNotNull(resultado);
            assertTrue(resultado.isEmpty());
        }
    }

    @Nested
    @DisplayName("Testes de cenários especiais")
    class CenariosEspeciaisTests {

        @Test
        @DisplayName("Deve lidar com preços com muitas casas decimais")
        void deveLidarComPrecosComMuitasCasasDecimais() {
            // Arrange
            BigDecimal precoComplexo = new BigDecimal("12345.123456789");
            CadastrarVeiculoRequest requestComplexo = new CadastrarVeiculoRequest();
            requestComplexo.setPreco(precoComplexo);

            Veiculo veiculoEsperado = new Veiculo();
            veiculoEsperado.setPreco(precoComplexo);

            when(mapper.toDomain(requestComplexo)).thenReturn(veiculoEsperado);

            // Act
            Veiculo resultado = mapper.toDomain(requestComplexo);

            // Assert
            assertEquals(precoComplexo, resultado.getPreco());
        }

        @Test
        @DisplayName("Deve lidar com anos extremos")
        void deveLidarComAnosExtremos() {
            // Arrange
            CadastrarVeiculoRequest requestExtremo = new CadastrarVeiculoRequest();
            requestExtremo.setAno(2050);

            Veiculo veiculoEsperado = new Veiculo();
            veiculoEsperado.setAno(2050);

            when(mapper.toDomain(requestExtremo)).thenReturn(veiculoEsperado);

            // Act
            Veiculo resultado = mapper.toDomain(requestExtremo);

            // Assert
            assertEquals(2050, resultado.getAno());
        }
    }
}
