package org.com.revenda.application.usecase;

import org.com.revenda.domain.enums.StatusPagamento;

/**
 * Use Case para processar pagamento (Input Boundary).
 */
public interface ProcessarPagamentoUseCase {
    void execute(String codigoPagamento, StatusPagamento novoStatus);
}

