package org.com.revenda.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.com.revenda.domain.entity.StatusVeiculo;
import org.com.revenda.domain.entity.Veiculo;
import org.com.revenda.domain.usecase.*;
import org.com.revenda.presentation.dto.request.CadastrarVeiculoRequest;
import org.com.revenda.presentation.dto.request.VenderVeiculoRequest;
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

    @Test
    void deveCadastrarVeiculoComSucesso() throws Exception {
        // Given
        CadastrarVeiculoRequest request = new CadastrarVeiculoRequest();
        request.setMarca("Toyota");
        request.setModelo("Corolla");
        request.setAno(2023);
        request.setCor("Branco");
        request.setPreco(new BigDecimal("75000.00"));

        Veiculo veiculoSalvo = new Veiculo();
        veiculoSalvo.setId(1L);
        veiculoSalvo.setMarca("Toyota");
        veiculoSalvo.setModelo("Corolla");
        veiculoSalvo.setAno(2023);
        veiculoSalvo.setCor("Branco");
        veiculoSalvo.setPreco(new BigDecimal("75000.00"));
        veiculoSalvo.setStatus(StatusVeiculo.DISPONIVEL);
        veiculoSalvo.setDataCadastro(LocalDateTime.now());

        when(cadastrarVeiculoUseCase.execute(any(Veiculo.class))).thenReturn(veiculoSalvo);

        // When & Then
        mockMvc.perform(post("/api/veiculos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.marca").value("Toyota"))
                .andExpect(jsonPath("$.modelo").value("Corolla"))
                .andExpected(jsonPath("$.ano").value(2023))
                .andExpect(jsonPath("$.cor").value("Branco"))
                .andExpect(jsonPath("$.preco").value(75000.00))
                .andExpect(jsonPath("$.status").value("DISPONIVEL"));
    }

    @Test
    void deveListarVeiculosDisponiveisComSucesso() throws Exception {
        // Given
        Veiculo veiculo1 = new Veiculo();
        veiculo1.setId(1L);
        veiculo1.setMarca("Toyota");
        veiculo1.setModelo("Corolla");
        veiculo1.setPreco(new BigDecimal("70000.00"));
        veiculo1.setStatus(StatusVeiculo.DISPONIVEL);

        Veiculo veiculo2 = new Veiculo();
        veiculo2.setId(2L);
        veiculo2.setMarca("Honda");
        veiculo2.setModelo("Civic");
        veiculo2.setPreco(new BigDecimal("80000.00"));
        veiculo2.setStatus(StatusVeiculo.DISPONIVEL);

        List<Veiculo> veiculos = Arrays.asList(veiculo1, veiculo2);

        when(listarVeiculosDisponiveis.execute()).thenReturn(veiculos);

        // When & Then
        mockMvc.perform(get("/api/veiculos/disponiveis"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].marca").value("Toyota"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].marca").value("Honda"));
    }

    @Test
    void deveRetornarBadRequestQuandoDadosInvalidos() throws Exception {
        // Given
        CadastrarVeiculoRequest request = new CadastrarVeiculoRequest();
        // Deixando campos obrigatórios em branco

        // When & Then
        mockMvc.perform(post("/api/veiculos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
