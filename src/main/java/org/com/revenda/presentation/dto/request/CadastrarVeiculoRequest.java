package org.com.revenda.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Dados para cadastro de veículo")
public class CadastrarVeiculoRequest {

    @NotBlank(message = "Marca é obrigatória")
    @Size(max = 50, message = "Marca deve ter no máximo 50 caracteres")
    @Schema(description = "Marca do veículo", example = "Toyota")
    private String marca;

    @NotBlank(message = "Modelo é obrigatório")
    @Size(max = 100, message = "Modelo deve ter no máximo 100 caracteres")
    @Schema(description = "Modelo do veículo", example = "Corolla")
    private String modelo;

    @NotNull(message = "Ano é obrigatório")
    @Min(value = 1900, message = "Ano deve ser maior que 1900")
    @Max(value = 2025, message = "Ano deve ser menor ou igual a 2025")
    @Schema(description = "Ano de fabricação", example = "2023")
    private Integer ano;

    @NotBlank(message = "Cor é obrigatória")
    @Size(max = 30, message = "Cor deve ter no máximo 30 caracteres")
    @Schema(description = "Cor do veículo", example = "Branco")
    private String cor;

    @NotNull(message = "Preço é obrigatório")
    @DecimalMin(value = "0.01", message = "Preço deve ser maior que zero")
    @Digits(integer = 8, fraction = 2, message = "Preço deve ter no máximo 8 dígitos inteiros e 2 decimais")
    @Schema(description = "Preço do veículo", example = "75000.00")
    private BigDecimal preco;
}
