package org.com.revenda.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.com.revenda.application.gateway.VeiculoPersistenceGateway;
import org.com.revenda.application.usecase.BuscarVeiculoPorIdUseCase;
import org.com.revenda.domain.entity.Veiculo;
import org.com.revenda.domain.exception.VeiculoNaoEncontradoException;

/**
 * Implementação do Use Case de buscar veículo por ID.
 */
@RequiredArgsConstructor
@Slf4j
public class BuscarVeiculoPorIdService implements BuscarVeiculoPorIdUseCase {

    private final VeiculoPersistenceGateway veiculoPersistenceGateway;

    @Override
    public Veiculo execute(Long id) {
        log.debug("Buscando veículo por ID: {}", id);

        return veiculoPersistenceGateway.findById(id)
            .orElseThrow(() -> new VeiculoNaoEncontradoException("Veículo não encontrado com ID: " + id));
    }
}

