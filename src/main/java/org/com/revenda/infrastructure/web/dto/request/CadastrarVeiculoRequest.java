package org.com.revenda.infrastructure.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CadastrarVeiculoRequest(
    @NotBlank(message = "Marca é obrigatória")
    String marca,

    @NotBlank(message = "Modelo é obrigatório")
    String modelo,

    @NotNull(message = "Ano é obrigatório")
    Integer ano,

    @NotNull(message = "Preço é obrigatório")
    @Positive(message = "Preço deve ser positivo")
    BigDecimal preco,

    String cor,
    String combustivel
) {}
