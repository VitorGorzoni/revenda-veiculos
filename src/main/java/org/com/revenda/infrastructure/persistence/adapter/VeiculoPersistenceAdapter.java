package org.com.revenda.infrastructure.persistence.adapter;

import lombok.RequiredArgsConstructor;
import org.com.revenda.application.gateway.VeiculoPersistenceGateway;
import org.com.revenda.domain.entity.Veiculo;
import org.com.revenda.domain.enums.StatusVeiculo;
import org.com.revenda.infrastructure.persistence.mapper.VeiculoMapper;
import org.com.revenda.infrastructure.persistence.repository.VeiculoJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class VeiculoPersistenceAdapter implements VeiculoPersistenceGateway {

    private final VeiculoJpaRepository jpaRepository;
    private final VeiculoMapper mapper;

    @Override
    public Veiculo save(Veiculo veiculo) {
        var entity = mapper.toJpaEntity(veiculo);
        var savedEntity = jpaRepository.save(entity);
        return mapper.toDomainEntity(savedEntity);
    }

    @Override
    public Optional<Veiculo> findById(Long id) {
        return jpaRepository.findById(id)
            .map(mapper::toDomainEntity);
    }

    @Override
    public List<Veiculo> findByStatusOrderByPrecoAsc(StatusVeiculo status) {
        return jpaRepository.findByStatusOrderByPrecoAsc(status)
            .stream()
            .map(mapper::toDomainEntity)
            .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public List<Veiculo> findAll() {
        return jpaRepository.findAll()
            .stream()
            .map(mapper::toDomainEntity)
            .collect(Collectors.toList());
    }
}
