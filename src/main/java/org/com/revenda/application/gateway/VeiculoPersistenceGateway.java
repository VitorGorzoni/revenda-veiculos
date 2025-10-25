package org.com.revenda.application.gateway;

import org.com.revenda.domain.entity.Veiculo;
import org.com.revenda.domain.enums.StatusVeiculo;

import java.util.List;
import java.util.Optional;

/**
 * Gateway de persistência de veículos (Output Boundary).
 * Define o contrato que a camada de aplicação precisa para persistir veículos,
 * sem conhecer detalhes de implementação (JPA, MongoDB, etc).
 */
public interface VeiculoPersistenceGateway {
    Veiculo save(Veiculo veiculo);
    Optional<Veiculo> findById(Long id);
    List<Veiculo> findByStatusOrderByPrecoAsc(StatusVeiculo status);
    void deleteById(Long id);
    List<Veiculo> findAll();
}

