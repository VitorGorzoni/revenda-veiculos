package org.com.revenda.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record VenderVeiculoRequest(
    @NotBlank(message = "CPF do cliente é obrigatório")
    String cpfCliente,

    @NotBlank(message = "Nome do cliente é obrigatório")
    String nomeCliente,

    @NotNull(message = "Valor da venda é obrigatório")
    @Positive(message = "Valor da venda deve ser positivo")
    BigDecimal valorVenda
) {}
