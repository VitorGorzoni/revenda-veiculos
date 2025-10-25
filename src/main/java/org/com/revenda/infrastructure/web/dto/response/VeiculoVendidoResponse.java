package org.com.revenda.infrastructure.web.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dados do veículo vendido")
public class VeiculoVendidoResponse {

    @Schema(description = "ID da venda", example = "1")
    private Long vendaId;

    @Schema(description = "CPF do comprador", example = "123.456.789-00")
    private String cpfComprador;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Data da venda", example = "2023-10-19 14:30:00")
    private LocalDateTime dataVenda;

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
}
