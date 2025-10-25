package org.com.revenda.application.usecase;

import org.com.revenda.domain.entity.Veiculo;

import java.util.List;

/**
 * Use Case para listar veículos disponíveis (Input Boundary).
 */
public interface ListarVeiculosDisponiveisUseCase {
    List<Veiculo> execute();
}

