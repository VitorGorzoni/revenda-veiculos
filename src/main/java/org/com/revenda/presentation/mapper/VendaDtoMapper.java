package org.com.revenda.presentation.mapper;

import org.com.revenda.domain.entity.Venda;
import org.com.revenda.presentation.dto.response.VendaResponse;
import org.springframework.stereotype.Component;

@Component
public class VendaDtoMapper {

    public VendaResponse toResponse(Venda venda) {
        return new VendaResponse(
            venda.getId(),
            venda.getVeiculoId(),
            venda.getCpfComprador(),
            venda.getDataVenda(),
            venda.getCodigoPagamento(),
            venda.getStatusPagamento()
        );
    }
}
