package org.com.revenda.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.com.revenda.domain.entity.StatusPagamento;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dados da venda")
public class VendaResponse {

    @Schema(description = "ID da venda", example = "1")
    private Long id;

    @Schema(description = "ID do veículo vendido", example = "1")
    private Long veiculoId;

    @Schema(description = "CPF do comprador", example = "123.456.789-00")
    private String cpfComprador;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Data da venda", example = "2023-10-19 14:30:00")
    private LocalDateTime dataVenda;

    @Schema(description = "Código de pagamento", example = "PAG-ABC12345")
    private String codigoPagamento;

    @Schema(description = "Status do pagamento", example = "PENDENTE")
    private StatusPagamento statusPagamento;
}
