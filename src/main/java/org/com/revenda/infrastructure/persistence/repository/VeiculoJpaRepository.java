package org.com.revenda.infrastructure.persistence.repository;

import org.com.revenda.domain.enums.StatusVeiculo;
import org.com.revenda.infrastructure.persistence.entity.VeiculoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VeiculoJpaRepository extends JpaRepository<VeiculoJpaEntity, Long> {
    List<VeiculoJpaEntity> findByStatusOrderByPrecoAsc(StatusVeiculo status);
}
