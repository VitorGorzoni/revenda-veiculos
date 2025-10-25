package org.com.revenda.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.com.revenda.application.gateway.VeiculoPersistenceGateway;
import org.com.revenda.application.usecase.ListarVeiculosPorStatusUseCase;
import org.com.revenda.domain.entity.Veiculo;
import org.com.revenda.domain.enums.StatusVeiculo;

import java.util.List;

/**
 * Implementação do Use Case de listar veículos por status.
 */
@RequiredArgsConstructor
@Slf4j
public class ListarVeiculosPorStatusService implements ListarVeiculosPorStatusUseCase {

    private final VeiculoPersistenceGateway veiculoPersistenceGateway;

    @Override
    public List<Veiculo> execute(StatusVeiculo status) {
        log.debug("Listando veículos com status: {}", status);
        return veiculoPersistenceGateway.findByStatusOrderByPrecoAsc(status);
    }
}

