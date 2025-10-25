package org.com.revenda.infrastructure.persistence.mapper;

import org.com.revenda.domain.entity.Venda;
import org.com.revenda.domain.enums.StatusPagamento;
import org.com.revenda.infrastructure.persistence.entity.VendaJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class VendaMapper {

    public VendaJpaEntity toJpaEntity(Venda venda) {
        if (venda == null) {
            return null;
        }

        VendaJpaEntity entity = new VendaJpaEntity();
        entity.setId(venda.getId());
        entity.setVeiculoId(venda.getVeiculoId());
        entity.setCpfCliente(venda.getCpfCliente());
        entity.setNomeCliente(venda.getNomeCliente());
        entity.setValorVenda(venda.getValorVenda());
        entity.setCodigoPagamento(venda.getCodigoPagamento());
        entity.setStatusPagamento(venda.getStatusPagamento());
        entity.setDataVenda(venda.getDataVenda());

        return entity;
    }

    public Venda toDomainEntity(VendaJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        Venda venda = new Venda();
        venda.setId(entity.getId());
        venda.setVeiculoId(entity.getVeiculoId());
        venda.setCpfCliente(entity.getCpfCliente());
        venda.setNomeCliente(entity.getNomeCliente());
        venda.setValorVenda(entity.getValorVenda());
        venda.setCodigoPagamento(entity.getCodigoPagamento());
        venda.setStatusPagamento(entity.getStatusPagamento());
        venda.setDataVenda(entity.getDataVenda());

        return venda;
    }
}
