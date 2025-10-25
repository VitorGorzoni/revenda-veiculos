package org.com.revenda.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.com.revenda.application.gateway.VendaPersistenceGateway;
import org.com.revenda.application.usecase.ListarVendasPorStatusPagamentoUseCase;
import org.com.revenda.domain.entity.Venda;
import org.com.revenda.domain.enums.StatusPagamento;

import java.util.List;

/**
 * Implementação do Use Case de listar vendas por status de pagamento.
 */
@RequiredArgsConstructor
@Slf4j
public class ListarVendasPorStatusPagamentoService implements ListarVendasPorStatusPagamentoUseCase {

    private final VendaPersistenceGateway vendaPersistenceGateway;

    @Override
    public List<Venda> execute(StatusPagamento statusPagamento) {
        log.info("Listando vendas com status de pagamento: {}", statusPagamento);
        return vendaPersistenceGateway.findByStatusPagamento(statusPagamento);
    }
}

