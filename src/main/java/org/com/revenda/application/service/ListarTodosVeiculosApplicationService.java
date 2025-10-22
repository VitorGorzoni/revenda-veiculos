package org.com.revenda.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.com.revenda.domain.entity.Veiculo;
import org.com.revenda.application.usecase.ListarTodosVeiculosUseCase;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class ListarTodosVeiculosApplicationService {

    private final ListarTodosVeiculosUseCase listarTodosVeiculosUseCase;

    public List<Veiculo> execute() {
        log.debug("Executando listagem de todos os veículos");

        List<Veiculo> veiculos = listarTodosVeiculosUseCase.execute();

        log.info("Total de veículos encontrados: {}", veiculos.size());

        return veiculos;
    }
}
