package org.com.revenda.domain.dto;

import org.com.revenda.domain.entity.Veiculo;
import org.com.revenda.domain.entity.Venda;

public class VendaComVeiculo {
    private final Venda venda;
    private final Veiculo veiculo;

    public VendaComVeiculo(Venda venda, Veiculo veiculo) {
        this.venda = venda;
        this.veiculo = veiculo;
    }

    public Venda getVenda() {
        return venda;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }
}
