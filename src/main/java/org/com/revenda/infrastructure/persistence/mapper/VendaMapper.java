package org.com.revenda.infrastructure.persistence.mapper;

import org.com.revenda.domain.entity.Venda;
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
        entity.setCpfComprador(venda.getCpfComprador());
        entity.setDataVenda(venda.getDataVenda());
        entity.setCodigoPagamento(venda.getCodigoPagamento());
        entity.setStatusPagamento(venda.getStatusPagamento());

        return entity;
    }

    public Venda toDomainEntity(VendaJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        Venda venda = new Venda();
        venda.setId(entity.getId());
        venda.setVeiculoId(entity.getVeiculoId());
        venda.setCpfComprador(entity.getCpfComprador());
        venda.setDataVenda(entity.getDataVenda());
        venda.setCodigoPagamento(entity.getCodigoPagamento());
        venda.setStatusPagamento(entity.getStatusPagamento());

        return venda;
    }
}
