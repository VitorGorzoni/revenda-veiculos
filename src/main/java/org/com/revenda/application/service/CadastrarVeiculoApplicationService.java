package org.com.revenda.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.com.revenda.domain.entity.Veiculo;
import org.com.revenda.application.usecase.CadastrarVeiculoUseCase;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class CadastrarVeiculoApplicationService {

    private final CadastrarVeiculoUseCase cadastrarVeiculoUseCase;

    public Veiculo execute(Veiculo veiculo) {
        log.info("Executando cadastro de veículo: {} {}", veiculo.getMarca(), veiculo.getModelo());

        Veiculo veiculoCadastrado = cadastrarVeiculoUseCase.execute(veiculo);

        log.info("Veículo cadastrado com sucesso no use case. ID: {}", veiculoCadastrado.getId());

        return veiculoCadastrado;
    }
}
