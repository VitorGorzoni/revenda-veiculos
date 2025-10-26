package org.com.revenda.infrastructure.persistence.adapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.com.revenda.application.gateway.VendaPersistenceGateway;
import org.com.revenda.domain.entity.Venda;
import org.com.revenda.domain.enums.StatusPagamento;
import org.com.revenda.infrastructure.persistence.mapper.VendaMapper;
import org.com.revenda.infrastructure.persistence.repository.VendaJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class VendaPersistenceAdapter implements VendaPersistenceGateway {

    private final VendaJpaRepository jpaRepository;
    private final VendaMapper mapper;

    @Override
    public Venda save(Venda venda) {
        log.info("Salvando venda para veículo ID: {} - Cliente: {}",
                venda.getVeiculoId(), venda.getCpfCliente());

        var entity = mapper.toJpaEntity(venda);
        var savedEntity = jpaRepository.save(entity);
        var result = mapper.toDomainEntity(savedEntity);

        log.info("Venda salva com sucesso. ID: {} - Código pagamento: {}",
                result.getId(), result.getCodigoPagamento());
        return result;
    }

    @Override
    public Optional<Venda> findByCodigoPagamento(String codigoPagamento) {
        log.debug("Buscando venda por código de pagamento: {}", codigoPagamento);

        var result = jpaRepository.findByCodigoPagamento(codigoPagamento)
            .map(mapper::toDomainEntity);

        if (result.isPresent()) {
            log.debug("Venda encontrada para código: {}", codigoPagamento);
        } else {
            log.debug("Venda não encontrada para código: {}", codigoPagamento);
        }

        return result;
    }

    @Override
    public Optional<Venda> findById(Long id) {
        log.debug("Buscando venda por ID: {}", id);

        var result = jpaRepository.findById(id)
            .map(mapper::toDomainEntity);

        if (result.isPresent()) {
            log.debug("Venda encontrada: ID {}", id);
        } else {
            log.debug("Venda não encontrada para ID: {}", id);
        }

        return result;
    }

    @Override
    public List<Venda> findAll() {
        log.debug("Buscando todas as vendas");

        var result = jpaRepository.findAll()
            .stream()
            .map(mapper::toDomainEntity)
            .collect(Collectors.toList());

        log.info("Encontradas {} vendas no total", result.size());
        return result;
    }

    @Override
    public List<Venda> findAllOrderByValorVendaDesc() {
        log.debug("Buscando todas as vendas ordenadas por valor");

        var result = jpaRepository.findAllByOrderByValorVendaDesc()
            .stream()
            .map(mapper::toDomainEntity)
            .collect(Collectors.toList());

        log.info("Encontradas {} vendas ordenadas por valor", result.size());
        return result;
    }

    @Override
    public List<Venda> findAllOrderByValorVendaAsc() {
        log.debug("Buscando todas as vendas ordenadas por valor (menor para maior)");

        var result = jpaRepository.findAllByOrderByValorVendaAsc()
            .stream()
            .map(mapper::toDomainEntity)
            .collect(Collectors.toList());

        log.info("Encontradas {} vendas ordenadas por valor (menor para maior)", result.size());
        return result;
    }

    @Override
    public List<Venda> findByStatusPagamento(StatusPagamento statusPagamento) {
        log.debug("Buscando vendas com status de pagamento: {}", statusPagamento);

        var result = jpaRepository.findByStatusPagamento(statusPagamento)
            .stream()
            .map(mapper::toDomainEntity)
            .collect(Collectors.toList());

        log.info("Encontradas {} vendas com status {}", result.size(), statusPagamento);
        return result;
    }
}
