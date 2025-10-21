package org.com.revenda.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Dados para venda de veículo")
public class VenderVeiculoRequest {

    @NotBlank(message = "CPF do comprador é obrigatório")
    @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}|\\d{11}",
             message = "CPF deve estar no formato XXX.XXX.XXX-XX ou apenas números")
    @Schema(description = "CPF do comprador", example = "123.456.789-00")
    private String cpfComprador;

    @NotNull(message = "Data da venda é obrigatória")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Data e hora da venda", example = "2025-01-18T14:30:00")
    private LocalDateTime dataVenda;
}
