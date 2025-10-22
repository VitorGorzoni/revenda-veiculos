package org.com.revenda.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.com.revenda.domain.entity.Veiculo;
import org.com.revenda.application.usecase.BuscarVeiculoPorIdUseCase;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class BuscarVeiculoPorIdApplicationService {

    private final BuscarVeiculoPorIdUseCase buscarVeiculoPorIdUseCase;

    public Veiculo execute(Long id) {
        log.debug("Executando busca de veículo por ID: {}", id);

        Veiculo veiculo = buscarVeiculoPorIdUseCase.execute(id);

        log.debug("Veículo encontrado: {} {}", veiculo.getMarca(), veiculo.getModelo());

        return veiculo;
    }
}
