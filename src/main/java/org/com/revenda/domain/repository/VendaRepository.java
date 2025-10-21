package org.com.revenda.domain.repository;

import org.com.revenda.domain.entity.Venda;
import org.com.revenda.domain.dto.VendaComVeiculo;

import java.util.List;
import java.util.Optional;

public interface VendaRepository {
    Venda save(Venda venda);
    Optional<Venda> findById(Long id);
    Optional<Venda> findByCodigoPagamento(String codigoPagamento);
    List<Venda> findAll();
    List<VendaComVeiculo> findVendasComVeiculosOrderByPreco();
}
