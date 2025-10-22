package org.com.revenda.application.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.com.revenda.domain.entity.Veiculo;
import org.com.revenda.domain.repository.VeiculoRepository;

@RequiredArgsConstructor
@Slf4j
public class CadastrarVeiculoUseCase {

    private final VeiculoRepository veiculoRepository;

    public Veiculo execute(Veiculo veiculo) {
        log.info("Executando use case de cadastrar veículo: {} {}", veiculo.getMarca(), veiculo.getModelo());

        Veiculo veiculoSalvo = veiculoRepository.save(veiculo);

        log.info("Veículo cadastrado com sucesso. ID: {}", veiculoSalvo.getId());
        return veiculoSalvo;
    }
}
