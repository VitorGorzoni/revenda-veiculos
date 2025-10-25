package org.com.revenda.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.com.revenda.application.gateway.VeiculoPersistenceGateway;
import org.com.revenda.application.usecase.ListarVeiculosDisponiveisUseCase;
import org.com.revenda.domain.entity.Veiculo;
import org.com.revenda.domain.enums.StatusVeiculo;

import java.util.List;

/**
 * Implementação do Use Case de listar veículos disponíveis.
 */
@RequiredArgsConstructor
@Slf4j
public class ListarVeiculosDisponiveisService implements ListarVeiculosDisponiveisUseCase {

    private final VeiculoPersistenceGateway veiculoPersistenceGateway;

    @Override
    public List<Veiculo> execute() {
        log.debug("Listando veículos disponíveis");
        return veiculoPersistenceGateway.findByStatusOrderByPrecoAsc(StatusVeiculo.DISPONIVEL);
    }
}

