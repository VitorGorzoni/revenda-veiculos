package org.com.revenda.application.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.com.revenda.domain.entity.Veiculo;
import org.com.revenda.domain.exception.VeiculoNaoEncontradoException;
import org.com.revenda.domain.repository.VeiculoRepository;

@RequiredArgsConstructor
@Log4j2
public class EditarVeiculoUseCase {

    private final VeiculoRepository veiculoRepository;

    public Veiculo execute(Long id, Veiculo veiculoAtualizado) {
        Veiculo veiculo = veiculoRepository.findById(id)
            .orElseThrow(() -> new VeiculoNaoEncontradoException("Veículo não encontrado com ID: " + id));

        if (!veiculo.isDisponivel()) {
            throw new IllegalStateException("Não é possível editar um veículo que não está disponível");
        }

        veiculo.setMarca(veiculoAtualizado.getMarca());
        veiculo.setModelo(veiculoAtualizado.getModelo());
        veiculo.setAno(veiculoAtualizado.getAno());
        veiculo.setCor(veiculoAtualizado.getCor());
        veiculo.setPreco(veiculoAtualizado.getPreco());

        return veiculoRepository.save(veiculo);
    }
}
