package org.com.revenda.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.com.revenda.application.service.*;
import org.com.revenda.domain.entity.StatusVeiculo;
import org.com.revenda.domain.entity.Veiculo;
import org.com.revenda.presentation.dto.request.CadastrarVeiculoRequest;
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
    private CadastrarVeiculoApplicationService cadastrarVeiculoApplicationService;

    @MockBean
    private EditarVeiculoApplicationService editarVeiculoApplicationService;

    @MockBean
    private VenderVeiculoApplicationService venderVeiculoApplicationService;

    @MockBean
    private ListarVeiculosPorStatusApplicationService listarVeiculosPorStatusApplicationService;

    @MockBean
    private ListarTodosVeiculosApplicationService listarTodosVeiculosApplicationService;

    @MockBean
    private BuscarVeiculoPorIdApplicationService buscarVeiculoPorIdApplicationService;

    @MockBean
    private ListarVeiculosVendidosApplicationService listarVeiculosVendidosApplicationService;

    @MockBean
    private VeiculoDtoMapper veiculoDtoMapper;

    @MockBean
    private VendaDtoMapper vendaDtoMapper;

    @Test
    void deveCadastrarVeiculoComSucesso() throws Exception {
        // Given
        CadastrarVeiculoRequest request = new CadastrarVeiculoRequest(
            "Toyota", "Corolla", 2023, new BigDecimal("75000.00"), "Branco", "ABC1234"
        );

        Veiculo veiculo = new Veiculo();
        veiculo.setMarca("Toyota");
        veiculo.setModelo("Corolla");
        veiculo.setAno(2023);
        veiculo.setCor("Branco");
        veiculo.setPreco(new BigDecimal("75000.00"));

        Veiculo veiculoSalvo = new Veiculo();
        veiculoSalvo.setId(1L);
        veiculoSalvo.setMarca("Toyota");
        veiculoSalvo.setModelo("Corolla");
        veiculoSalvo.setAno(2023);
        veiculoSalvo.setCor("Branco");
        veiculoSalvo.setPreco(new BigDecimal("75000.00"));
        veiculoSalvo.setStatus(StatusVeiculo.DISPONIVEL);
        veiculoSalvo.setDataCadastro(LocalDateTime.now());

        VeiculoResponse response = new VeiculoResponse(
            1L, "Toyota", "Corolla", 2023, "Branco",
            new BigDecimal("75000.00"), StatusVeiculo.DISPONIVEL, LocalDateTime.now()
        );

        when(veiculoDtoMapper.toDomain(any(CadastrarVeiculoRequest.class))).thenReturn(veiculo);
        when(cadastrarVeiculoApplicationService.execute(any(Veiculo.class))).thenReturn(veiculoSalvo);
        when(veiculoDtoMapper.toResponse(any(Veiculo.class))).thenReturn(response);

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
    void deveListarVeiculosComSucesso() throws Exception {
        // Given
        Veiculo veiculo1 = new Veiculo();
        veiculo1.setId(1L);
        veiculo1.setMarca("Toyota");
        veiculo1.setModelo("Corolla");

        Veiculo veiculo2 = new Veiculo();
        veiculo2.setId(2L);
        veiculo2.setMarca("Honda");
        veiculo2.setModelo("Civic");

        List<Veiculo> veiculos = Arrays.asList(veiculo1, veiculo2);

        VeiculoResponse response1 = new VeiculoResponse(
            1L, "Toyota", "Corolla", 2023, "Branco",
            new BigDecimal("75000.00"), StatusVeiculo.DISPONIVEL, LocalDateTime.now()
        );

        VeiculoResponse response2 = new VeiculoResponse(
            2L, "Honda", "Civic", 2022, "Preto",
            new BigDecimal("85000.00"), StatusVeiculo.DISPONIVEL, LocalDateTime.now()
        );

        when(listarTodosVeiculosApplicationService.execute()).thenReturn(veiculos);
        when(veiculoDtoMapper.toResponseList(veiculos)).thenReturn(Arrays.asList(response1, response2));

        // When & Then
        mockMvc.perform(get("/api/veiculos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].marca").value("Toyota"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].marca").value("Honda"));
    }
}
