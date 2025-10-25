package org.com.revenda.application.usecase;

import org.com.revenda.domain.entity.Venda;

import java.util.List;

/**
 * Use Case para listar veículos vendidos (Input Boundary).
 */
public interface ListarVeiculosVendidosUseCase {
    List<Venda> execute();
}

