package org.com.revenda.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.com.revenda.domain.entity.StatusVeiculo;
import org.com.revenda.domain.entity.Veiculo;
import org.com.revenda.domain.usecase.*;
import org.com.revenda.presentation.dto.request.CadastrarVeiculoRequest;
import org.com.revenda.presentation.dto.request.VenderVeiculoRequest;
import org.com.revenda.presentation.dto.response.VeiculoResponse;
import org.com.revenda.presentation.mapper.VeiculoDtoMapper;
import org.com.revenda.presentation.mapper.VendaDtoMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VeiculoController.class)
class VeiculoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CadastrarVeiculoUseCase cadastrarVeiculoUseCase;

    @MockBean
    private EditarVeiculoUseCase editarVeiculoUseCase;

    @MockBean
    private VenderVeiculoUseCase venderVeiculoUseCase;

    @MockBean
    private ListarVeiculosDisponiveis listarVeiculosDisponiveis;

    @MockBean
    private ListarVeiculosVendidosUseCase listarVeiculosVendidosUseCase;

    @MockBean
    private ListarVeiculosPorStatusUseCase listarVeiculosPorStatusUseCase;

    @MockBean
    private ListarTodosVeiculosUseCase listarTodosVeiculosUseCase;

    @MockBean
    private BuscarVeiculoPorIdUseCase buscarVeiculoPorIdUseCase;

    @MockBean
    private VeiculoDtoMapper veiculoDtoMapper;

    @MockBean
    private VendaDtoMapper vendaDtoMapper;

    @Test
    void deveCadastrarVeiculoComSucesso() throws Exception {
        // Given
        CadastrarVeiculoRequest request = new CadastrarVeiculoRequest();
        request.setMarca("Toyota");
        request.setModelo("Corolla");
        request.setAno(2023);
        request.setCor("Branco");
        request.setPreco(new BigDecimal("75000.00"));

        Veiculo veiculoInput = new Veiculo();
        veiculoInput.setMarca("Toyota");
        veiculoInput.setModelo("Corolla");
        veiculoInput.setAno(2023);
        veiculoInput.setCor("Branco");
        veiculoInput.setPreco(new BigDecimal("75000.00"));

        Veiculo veiculoSalvo = new Veiculo();
        veiculoSalvo.setId(1L);
        veiculoSalvo.setMarca("Toyota");
        veiculoSalvo.setModelo("Corolla");
        veiculoSalvo.setAno(2023);
        veiculoSalvo.setCor("Branco");
        veiculoSalvo.setPreco(new BigDecimal("75000.00"));
        veiculoSalvo.setStatus(StatusVeiculo.DISPONIVEL);
        veiculoSalvo.setDataCadastro(LocalDateTime.now());

        VeiculoResponse veiculoResponse = new VeiculoResponse();
        veiculoResponse.setId(1L);
        veiculoResponse.setMarca("Toyota");
        veiculoResponse.setModelo("Corolla");
        veiculoResponse.setAno(2023);
        veiculoResponse.setCor("Branco");
        veiculoResponse.setPreco(new BigDecimal("75000.00"));
        veiculoResponse.setStatus(StatusVeiculo.DISPONIVEL);
        veiculoResponse.setDataCadastro(veiculoSalvo.getDataCadastro());

        // Mock the mappers
        when(veiculoDtoMapper.toDomain(any(CadastrarVeiculoRequest.class))).thenReturn(veiculoInput);
        when(cadastrarVeiculoUseCase.execute(any(Veiculo.class))).thenReturn(veiculoSalvo);
        when(veiculoDtoMapper.toResponse(any(Veiculo.class))).thenReturn(veiculoResponse);

        // When & Then
        mockMvc.perform(post("/api/veiculos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.marca").value("Toyota"))
                .andExpect(jsonPath("$.modelo").value("Corolla"))
                .andExpect(jsonPath("$.ano").value(2023))
                .andExpect(jsonPath("$.cor").value("Branco"))
                .andExpect(jsonPath("$.status").value("DISPONIVEL"));
    }

    @Test
    void deveListarVeiculosDisponiveisComSucesso() throws Exception {
        // Given
        Veiculo veiculo1 = new Veiculo();
        veiculo1.setId(1L);
        veiculo1.setMarca("Honda");
        veiculo1.setModelo("Civic");
        veiculo1.setStatus(StatusVeiculo.DISPONIVEL);

        Veiculo veiculo2 = new Veiculo();
        veiculo2.setId(2L);
        veiculo2.setMarca("Toyota");
        veiculo2.setModelo("Corolla");
        veiculo2.setStatus(StatusVeiculo.DISPONIVEL);

        List<Veiculo> veiculos = Arrays.asList(veiculo1, veiculo2);

        VeiculoResponse response1 = new VeiculoResponse();
        response1.setId(1L);
        response1.setMarca("Honda");
        response1.setModelo("Civic");
        response1.setStatus(StatusVeiculo.DISPONIVEL);

        VeiculoResponse response2 = new VeiculoResponse();
        response2.setId(2L);
        response2.setMarca("Toyota");
        response2.setModelo("Corolla");
        response2.setStatus(StatusVeiculo.DISPONIVEL);

        List<VeiculoResponse> responses = Arrays.asList(response1, response2);

        // Mock the use case and mapper
        when(listarVeiculosPorStatusUseCase.execute(StatusVeiculo.DISPONIVEL)).thenReturn(veiculos);
        when(veiculoDtoMapper.toResponseList(veiculos)).thenReturn(responses);

        // When & Then
        mockMvc.perform(get("/api/veiculos")
                .param("status", "DISPONIVEL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].marca").value("Honda"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].marca").value("Toyota"));
    }

    @Test
    void deveRetornarBadRequestQuandoDadosInvalidos() throws Exception {
        // Given
        CadastrarVeiculoRequest request = new CadastrarVeiculoRequest();
        // Dados inválidos: sem marca e modelo

        // When & Then
        mockMvc.perform(post("/api/veiculos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
