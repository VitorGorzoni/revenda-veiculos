package org.com.revenda.domain.usecase;

import lombok.RequiredArgsConstructor;
import org.com.revenda.domain.repository.VendaRepository;
import org.com.revenda.domain.dto.VendaComVeiculo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListarVeiculosVendidosUseCase {

    private final VendaRepository vendaRepository;

    public List<VendaComVeiculo> execute() {
        return vendaRepository.findVendasComVeiculosOrderByPreco();
    }
}
