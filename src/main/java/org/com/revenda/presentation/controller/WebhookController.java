package org.com.revenda.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.com.revenda.domain.entity.StatusPagamento;
import org.com.revenda.domain.usecase.ProcessarPagamentoUseCase;
import org.com.revenda.presentation.dto.request.WebhookPagamentoRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/webhook")
@RequiredArgsConstructor
@Tag(name = "Webhook", description = "Endpoints para processamento de pagamentos")
public class WebhookController {

    private final ProcessarPagamentoUseCase processarPagamentoUseCase;

    @PostMapping("/pagamento")
    @Operation(summary = "Processar pagamento",
               description = "Webhook para processar confirmação ou cancelamento de pagamento")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pagamento processado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Código de pagamento não encontrado")
    })
    public ResponseEntity<Void> processarPagamento(@Valid @RequestBody WebhookPagamentoRequest request) {
        StatusPagamento statusPagamento = StatusPagamento.valueOf(request.getStatus().toUpperCase());
        processarPagamentoUseCase.execute(request.getCodigoPagamento(), statusPagamento);
        return ResponseEntity.ok().build();
    }
}
