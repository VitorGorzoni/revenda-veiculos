package org.com.revenda.infrastructure.persistence.adapter;

import lombok.RequiredArgsConstructor;
import org.com.revenda.domain.entity.Venda;
import org.com.revenda.domain.repository.VendaRepository;
import org.com.revenda.domain.dto.VendaComVeiculo;
import org.com.revenda.infrastructure.persistence.mapper.VendaMapper;
import org.com.revenda.infrastructure.persistence.mapper.VeiculoMapper;
import org.com.revenda.infrastructure.persistence.repository.VendaJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class VendaRepositoryAdapter implements VendaRepository {

    private final VendaJpaRepository jpaRepository;
    private final VendaMapper vendaMapper;
    private final VeiculoMapper veiculoMapper;

    @Override
    public Venda save(Venda venda) {
        var entity = vendaMapper.toJpaEntity(venda);
        var savedEntity = jpaRepository.save(entity);
        return vendaMapper.toDomainEntity(savedEntity);
    }

    @Override
    public Optional<Venda> findById(Long id) {
        return jpaRepository.findById(id)
            .map(vendaMapper::toDomainEntity);
    }

    @Override
    public Optional<Venda> findByCodigoPagamento(String codigoPagamento) {
        return jpaRepository.findByCodigoPagamento(codigoPagamento)
            .map(vendaMapper::toDomainEntity);
    }

    @Override
    public List<Venda> findAll() {
        return jpaRepository.findAll()
            .stream()
            .map(vendaMapper::toDomainEntity)
            .toList();
    }

    @Override
    public List<VendaComVeiculo> findVendasComVeiculosOrderByPreco() {
        return jpaRepository.findVendasComVeiculosOrderByPreco()
            .stream()
            .map(vendaJpaEntity -> new VendaComVeiculo(
                vendaMapper.toDomainEntity(vendaJpaEntity),
                veiculoMapper.toDomainEntity(vendaJpaEntity.getVeiculo())
            ))
            .toList();
    }
}
