package org.com.revenda.application.usecase;

import lombok.RequiredArgsConstructor;
import org.com.revenda.domain.entity.Venda;
import org.com.revenda.domain.repository.VendaRepository;

import java.util.List;

@RequiredArgsConstructor
public class ListarVeiculosVendidosUseCase {

    private final VendaRepository vendaRepository;

    public List<Venda> execute() {
        return vendaRepository.findAllOrderByValorVendaDesc();
    }
}
