package org.com.revenda.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.com.revenda.domain.entity.StatusVeiculo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dados do veículo")
public class VeiculoResponse {

    @Schema(description = "ID do veículo", example = "1")
    private Long id;

    @Schema(description = "Marca do veículo", example = "Toyota")
    private String marca;

    @Schema(description = "Modelo do veículo", example = "Corolla")
    private String modelo;

    @Schema(description = "Ano de fabricação", example = "2023")
    private Integer ano;

    @Schema(description = "Cor do veículo", example = "Branco")
    private String cor;

    @Schema(description = "Preço do veículo", example = "75000.00")
    private BigDecimal preco;

    @Schema(description = "Status do veículo", example = "DISPONIVEL")
    private StatusVeiculo status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Data de cadastro", example = "2023-10-19 10:30:00")
    private LocalDateTime dataCadastro;
}
