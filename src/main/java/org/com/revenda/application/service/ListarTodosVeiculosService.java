package org.com.revenda.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.com.revenda.application.gateway.VeiculoPersistenceGateway;
import org.com.revenda.application.usecase.ListarTodosVeiculosUseCase;
import org.com.revenda.domain.entity.Veiculo;

import java.util.List;

/**
 * Implementação do Use Case de listar todos os veículos.
 */
@RequiredArgsConstructor
@Slf4j
public class ListarTodosVeiculosService implements ListarTodosVeiculosUseCase {

    private final VeiculoPersistenceGateway veiculoPersistenceGateway;

    @Override
    public List<Veiculo> execute() {
        log.debug("Listando todos os veículos");
        return veiculoPersistenceGateway.findAll();
    }
}

