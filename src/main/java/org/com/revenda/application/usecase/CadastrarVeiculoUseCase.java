package org.com.revenda.application.usecase;

import org.com.revenda.domain.entity.Veiculo;

/**
 * Use Case para cadastrar um veículo (Input Boundary).
 */
public interface CadastrarVeiculoUseCase {
    Veiculo execute(Veiculo veiculo);
}

