package org.com.revenda.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.com.revenda.domain.entity.Venda;
import org.com.revenda.application.usecase.ListarVeiculosVendidosUseCase;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ListarVeiculosVendidosApplicationService {

    private final ListarVeiculosVendidosUseCase listarVeiculosVendidosUseCase;

    public List<Venda> execute() {
        log.debug("Executando listagem de veículos vendidos");

        List<Venda> vendas = listarVeiculosVendidosUseCase.execute();

        log.info("Total de vendas encontradas: {}", vendas.size());

        return vendas;
    }
}
