package org.com.revenda.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.com.revenda.application.gateway.VendaPersistenceGateway;
import org.com.revenda.application.usecase.ListarTodasVendasUseCase;
import org.com.revenda.domain.entity.Venda;

import java.util.List;

/**
 * Implementação do Use Case de listar todas as vendas.
 */
@RequiredArgsConstructor
@Slf4j
public class ListarTodasVendasService implements ListarTodasVendasUseCase {

    private final VendaPersistenceGateway vendaPersistenceGateway;

    @Override
    public List<Venda> execute() {
        log.info("Listando todas as vendas");
        return vendaPersistenceGateway.findAll();
    }
}
