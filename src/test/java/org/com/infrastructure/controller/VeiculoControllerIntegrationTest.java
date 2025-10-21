package org.com.infrastructure.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.com.application.dto.VeiculoRequestDto;
import org.com.application.dto.VendaRequestDto;
import org.com.application.dto.WebhookPagamentoDto;
import org.com.domain.entity.StatusVeiculo;
import org.com.domain.entity.Veiculo;
import org.com.infrastructure.persistence.entity.VeiculoEntity;
import org.com.infrastructure.persistence.repository.VeiculoJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
@Transactional
class VeiculoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private VeiculoJpaRepository veiculoJpaRepository;

    private VeiculoRequestDto veiculoRequestDto;
    private VeiculoEntity veiculoEntity;

    @BeforeEach
    void setUp() {
        veiculoJpaRepository.deleteAll();

        veiculoRequestDto = new VeiculoRequestDto();
        veiculoRequestDto.setMarca("Toyota");
        veiculoRequestDto.setModelo("Corolla");
        veiculoRequestDto.setAno(2023);
        veiculoRequestDto.setCor("Branco");
        veiculoRequestDto.setPreco(new BigDecimal("85000.00"));

        veiculoEntity = new VeiculoEntity();
        veiculoEntity.setMarca("Honda");
        veiculoEntity.setModelo("Civic");
        veiculoEntity.setAno(2022);
        veiculoEntity.setCor("Preto");
        veiculoEntity.setPreco(new BigDecimal("75000.00"));
        veiculoEntity.setStatus(StatusVeiculo.DISPONIVEL);
        veiculoEntity.setDataCadastro(LocalDateTime.now());
    }

    @Test
    void deveCadastrarVeiculoComSucesso() throws Exception {
        mockMvc.perform(post("/api/veiculos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(veiculoRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.marca", is("Toyota")))
                .andExpect(jsonPath("$.modelo", is("Corolla")))
                .andExpect(jsonPath("$.ano", is(2023)))
                .andExpect(jsonPath("$.cor", is("Branco")))
                .andExpect(jsonPath("$.preco", is(85000.00)))
                .andExpect(jsonPath("$.status", is("DISPONIVEL")));
    }

    @Test
    void deveRetornarBadRequestParaDadosInvalidos() throws Exception {
        VeiculoRequestDto invalidDto = new VeiculoRequestDto();
        invalidDto.setMarca("");
        invalidDto.setModelo("");
        invalidDto.setAno(null);
        invalidDto.setCor("");
        invalidDto.setPreco(null);

        mockMvc.perform(post("/api/veiculos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveEditarVeiculoComSucesso() throws Exception {
        VeiculoEntity savedEntity = veiculoJpaRepository.save(veiculoEntity);

        VeiculoRequestDto updateDto = new VeiculoRequestDto();
        updateDto.setMarca("Honda");
        updateDto.setModelo("Civic");
        updateDto.setAno(2023);
        updateDto.setCor("Azul");
        updateDto.setPreco(new BigDecimal("80000.00"));

        mockMvc.perform(put("/api/veiculos/{id}", savedEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cor", is("Azul")))
                .andExpect(jsonPath("$.preco", is(80000.00)));
    }

    @Test
    void deveEfetuarVendaComSucesso() throws Exception {
        VeiculoEntity savedEntity = veiculoJpaRepository.save(veiculoEntity);

        VendaRequestDto vendaDto = new VendaRequestDto();
        vendaDto.setCpfComprador("12345678901");

        mockMvc.perform(post("/api/veiculos/{id}/venda", savedEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vendaDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("VENDIDO")))
                .andExpect(jsonPath("$.venda.cpfComprador", is("12345678901")));
    }

    @Test
    void deveListarVeiculosVendaComSucesso() throws Exception {
        veiculoJpaRepository.save(veiculoEntity);

        mockMvc.perform(get("/api/veiculos/venda"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].status", is("DISPONIVEL")));
    }

    @Test
    void deveProcessarWebhookPagamentoComSucesso() throws Exception {
        // Primeiro, criar um veículo vendido
        VeiculoEntity savedEntity = veiculoJpaRepository.save(veiculoEntity);

        VendaRequestDto vendaDto = new VendaRequestDto();
        vendaDto.setCpfComprador("12345678901");

        // Efetuar venda
        mockMvc.perform(post("/api/veiculos/{id}/venda", savedEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vendaDto)))
                .andExpect(status().isOk());

        // Simular webhook de pagamento
        WebhookPagamentoDto webhookDto = new WebhookPagamentoDto();
        webhookDto.setCodigoPagamento("codigo-pagamento-exemplo");
        webhookDto.setPagamentoEfetuado(true);

        mockMvc.perform(post("/api/webhook/pagamento")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(webhookDto)))
                .andExpect(status().isOk());
    }
}
