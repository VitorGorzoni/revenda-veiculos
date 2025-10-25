package org.com.revenda.application.gateway;

import org.com.revenda.domain.entity.Venda;

import java.util.List;
import java.util.Optional;

/**
 * Gateway de persistência de vendas (Output Boundary).
 * Define o contrato que a camada de aplicação precisa para persistir vendas,
 * sem conhecer detalhes de implementação (JPA, MongoDB, etc).
 */
public interface VendaPersistenceGateway {
    Venda save(Venda venda);
    Optional<Venda> findByCodigoPagamento(String codigoPagamento);
    Optional<Venda> findById(Long id);
    List<Venda> findAll();
    List<Venda> findAllOrderByValorVendaDesc();
}
