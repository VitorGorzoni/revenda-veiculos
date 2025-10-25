package org.com.revenda.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.com.revenda.application.gateway.VeiculoPersistenceGateway;
import org.com.revenda.application.usecase.CadastrarVeiculoUseCase;
import org.com.revenda.domain.entity.Veiculo;

/**
 * Implementação do Use Case de cadastrar veículo.
 */
@RequiredArgsConstructor
@Slf4j
public class CadastrarVeiculoService implements CadastrarVeiculoUseCase {

    private final VeiculoPersistenceGateway veiculoPersistenceGateway;

    @Override
    public Veiculo execute(Veiculo veiculo) {
        log.info("Cadastrando veículo: {} {}", veiculo.getMarca(), veiculo.getModelo());

        Veiculo veiculoSalvo = veiculoPersistenceGateway.save(veiculo);

        log.info("Veículo cadastrado com sucesso. ID: {}", veiculoSalvo.getId());
        return veiculoSalvo;
    }
}

