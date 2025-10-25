package org.com.revenda.infrastructure.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.com.revenda.application.usecase.ListarTodasVendasUseCase;
import org.com.revenda.application.usecase.ListarVendasPorStatusPagamentoUseCase;
import org.com.revenda.domain.entity.Venda;
import org.com.revenda.domain.enums.StatusPagamento;
import org.com.revenda.infrastructure.web.dto.response.VendaResponse;
import org.com.revenda.infrastructure.web.mapper.VendaDtoMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendas")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Vendas", description = "Operações relacionadas às vendas")
public class VendaController {

    private final ListarTodasVendasUseCase listarTodasVendasUseCase;
    private final ListarVendasPorStatusPagamentoUseCase listarVendasPorStatusPagamentoUseCase;
    private final VendaDtoMapper vendaMapper;

    @GetMapping
    @Operation(summary = "Listar vendas",
               description = "Lista todas as vendas ou filtra por status de pagamento (PENDENTE, CONFIRMADO ou CANCELADO)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de vendas"),
        @ApiResponse(responseCode = "400", description = "Status de pagamento inválido")
    })
    public ResponseEntity<List<VendaResponse>> listarVendas(
            @Parameter(description = "Status do pagamento (opcional)",
                       schema = @io.swagger.v3.oas.annotations.media.Schema(
                           allowableValues = {"PENDENTE", "CONFIRMADO", "CANCELADO"}))
            @RequestParam(value = "status", required = false) String statusParam) {

        log.info("Listando vendas - Status: {}",
                statusParam != null ? statusParam : "todos");

        List<Venda> vendas = (statusParam == null || statusParam.trim().isEmpty())
                ? listarTodasVendasUseCase.execute()
                : listarVendasPorStatusPagamentoUseCase.execute(parseStatusPagamento(statusParam));

        List<VendaResponse> response = vendaMapper.toResponseList(vendas);

        log.info("Total de vendas encontradas: {}", response.size());

        return ResponseEntity.ok(response);
    }

    private StatusPagamento parseStatusPagamento(String statusParam) {
        try {
            return StatusPagamento.valueOf(statusParam.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.error("Status de pagamento inválido: {}", statusParam);
            throw new IllegalArgumentException("Status de pagamento deve ser PENDENTE, CONFIRMADO ou CANCELADO");
        }
    }
}
