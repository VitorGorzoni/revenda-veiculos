package org.com.revenda.application.usecase;

import org.com.revenda.domain.entity.Veiculo;

/**
 * Use Case para buscar um veículo por ID (Input Boundary).
 */
public interface BuscarVeiculoPorIdUseCase {
    Veiculo execute(Long id);
}

