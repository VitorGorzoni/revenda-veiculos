package org.com.revenda.application.usecase;

import org.com.revenda.domain.entity.Veiculo;

/**
 * Use Case para editar um veículo (Input Boundary).
 */
public interface EditarVeiculoUseCase {
    Veiculo execute(Long id, Veiculo veiculoAtualizado);
}

