package org.com.revenda.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.com.revenda.application.gateway.VeiculoPersistenceGateway;
import org.com.revenda.application.usecase.EditarVeiculoUseCase;
import org.com.revenda.domain.entity.Veiculo;
import org.com.revenda.domain.exception.VeiculoNaoEncontradoException;

/**
 * Implementação do Use Case de editar veículo.
 */
@RequiredArgsConstructor
@Slf4j
public class EditarVeiculoService implements EditarVeiculoUseCase {

    private final VeiculoPersistenceGateway veiculoPersistenceGateway;

    @Override
    public Veiculo execute(Long id, Veiculo veiculoAtualizado) {
        log.info("Editando veículo ID: {}", id);

        Veiculo veiculo = veiculoPersistenceGateway.findById(id)
            .orElseThrow(() -> new VeiculoNaoEncontradoException("Veículo não encontrado com ID: " + id));

        if (!veiculo.isDisponivel()) {
            throw new IllegalStateException("Não é possível editar um veículo que não está disponível");
        }

        veiculo.setMarca(veiculoAtualizado.getMarca());
        veiculo.setModelo(veiculoAtualizado.getModelo());
        veiculo.setAno(veiculoAtualizado.getAno());
        veiculo.setCor(veiculoAtualizado.getCor());
        veiculo.setPreco(veiculoAtualizado.getPreco());

        return veiculoPersistenceGateway.save(veiculo);
    }
}

