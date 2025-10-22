package org.com.revenda.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.com.revenda.domain.entity.StatusVeiculo;
import org.com.revenda.domain.entity.Veiculo;
import org.com.revenda.application.usecase.ListarVeiculosPorStatusUseCase;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class ListarVeiculosDisponiveisApplicationService {

    private final ListarVeiculosPorStatusUseCase listarVeiculosPorStatusUseCase;

    public List<Veiculo> execute() {
        log.debug("Executando listagem de veículos disponíveis");

        List<Veiculo> veiculos = listarVeiculosPorStatusUseCase.execute(StatusVeiculo.DISPONIVEL);

        log.info("Veículos disponíveis encontrados: {}", veiculos.size());

        return veiculos;
    }
}
