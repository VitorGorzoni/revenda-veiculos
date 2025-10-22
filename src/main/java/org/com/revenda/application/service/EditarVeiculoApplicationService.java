package org.com.revenda.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.com.revenda.domain.entity.Veiculo;
import org.com.revenda.application.usecase.EditarVeiculoUseCase;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class EditarVeiculoApplicationService {

    private final EditarVeiculoUseCase editarVeiculoUseCase;

    public Veiculo execute(Long id, Veiculo veiculo) {
        log.info("Executando edição de veículo ID: {}", id);

        Veiculo veiculoEditado = editarVeiculoUseCase.execute(id, veiculo);

        log.info("Veículo editado com sucesso. ID: {}", veiculoEditado.getId());

        return veiculoEditado;
    }
}
