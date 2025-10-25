package org.com.revenda.application.usecase;

import org.com.revenda.domain.entity.Venda;

import java.util.List;

/**
 * Use Case para listar todas as vendas (Input Boundary).
 */
public interface ListarTodasVendasUseCase {
    List<Venda> execute();
}
