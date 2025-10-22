package org.com.revenda.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.com.revenda.domain.entity.Veiculo;
import org.com.revenda.application.usecase.BuscarVeiculoPorIdUseCase;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BuscarVeiculoPorIdApplicationService {

    private final BuscarVeiculoPorIdUseCase buscarVeiculoPorIdUseCase;

    public Veiculo execute(Long id) {
        log.debug("Executando busca de veículo por ID: {}", id);

        Veiculo veiculo = buscarVeiculoPorIdUseCase.execute(id);

        log.debug("Veículo encontrado: {} {}", veiculo.getMarca(), veiculo.getModelo());

        return veiculo;
    }
}
