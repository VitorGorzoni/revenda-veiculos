package org.com.revenda.application.usecase;

import org.com.revenda.domain.entity.Venda;

import java.math.BigDecimal;

/**
 * Use Case para vender um veículo (Input Boundary).
 */
public interface VenderVeiculoUseCase {
    Venda execute(Long veiculoId, String cpfCliente, String nomeCliente, BigDecimal valorVenda);
}

