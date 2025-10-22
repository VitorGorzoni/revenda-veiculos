package org.com.revenda.domain.repository;

import org.com.revenda.domain.entity.Venda;

import java.util.List;
import java.util.Optional;

public interface VendaRepository {
    Venda save(Venda venda);
    Optional<Venda> findByCodigoPagamento(String codigoPagamento);
    Optional<Venda> findById(Long id);
    List<Venda> findAll();
    List<Venda> findAllOrderByValorVendaDesc();
}
