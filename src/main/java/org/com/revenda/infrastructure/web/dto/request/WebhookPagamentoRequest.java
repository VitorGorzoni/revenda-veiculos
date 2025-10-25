package org.com.revenda.infrastructure.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Dados do webhook de pagamento")
public class WebhookPagamentoRequest {

    @NotBlank(message = "Código de pagamento é obrigatório")
    @Schema(description = "Código de pagamento", example = "PAG-ABC12345")
    private String codigoPagamento;

    @NotBlank(message = "Status do pagamento é obrigatório")
    @Schema(description = "Status do pagamento (CONFIRMADO ou CANCELADO)", example = "CONFIRMADO", allowableValues = {"CONFIRMADO", "CANCELADO"})
    private String status;
}
