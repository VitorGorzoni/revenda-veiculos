package org.com.revenda.application.usecase;

import org.com.revenda.domain.entity.Venda;
import org.com.revenda.domain.enums.StatusPagamento;

import java.util.List;

/**
 * Use Case para listar vendas por status de pagamento (Input Boundary).
 */
public interface ListarVendasPorStatusPagamentoUseCase {
    List<Venda> execute(StatusPagamento statusPagamento);
}

