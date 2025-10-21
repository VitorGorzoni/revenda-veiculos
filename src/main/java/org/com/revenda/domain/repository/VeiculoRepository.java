package org.com.revenda.domain.repository;

import org.com.revenda.domain.entity.StatusVeiculo;
import org.com.revenda.domain.entity.Veiculo;

import java.util.List;
import java.util.Optional;

public interface VeiculoRepository {
    Veiculo save(Veiculo veiculo);
    Optional<Veiculo> findById(Long id);
    List<Veiculo> findByStatusOrderByPrecoAsc(StatusVeiculo status);
    void deleteById(Long id);
    List<Veiculo> findAll();
}
