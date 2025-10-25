package org.com.revenda.application.usecase;

import org.com.revenda.domain.entity.Veiculo;
import org.com.revenda.domain.enums.StatusVeiculo;

import java.util.List;

/**
 * Use Case para listar veículos por status (Input Boundary).
 */
public interface ListarVeiculosPorStatusUseCase {
    List<Veiculo> execute(StatusVeiculo status);
}

