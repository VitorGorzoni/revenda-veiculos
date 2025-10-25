package org.com.revenda.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.com.revenda.application.gateway.VendaPersistenceGateway;
import org.com.revenda.application.usecase.ListarVeiculosVendidosUseCase;
import org.com.revenda.domain.entity.Venda;

import java.util.List;

/**
 * Implementação do Use Case de listar veículos vendidos.
 */
@RequiredArgsConstructor
@Slf4j
public class ListarVeiculosVendidosService implements ListarVeiculosVendidosUseCase {

    private final VendaPersistenceGateway vendaPersistenceGateway;

    @Override
    public List<Venda> execute() {
        log.debug("Listando veículos vendidos");
        return vendaPersistenceGateway.findAllOrderByValorVendaDesc();
    }
}

