package org.com.revenda.application.usecase;

import lombok.RequiredArgsConstructor;
import org.com.revenda.domain.entity.Veiculo;
import org.com.revenda.domain.repository.VeiculoRepository;

import java.util.List;

@RequiredArgsConstructor
public class ListarTodosVeiculosUseCase {

    private final VeiculoRepository veiculoRepository;

    public List<Veiculo> execute() {
        return veiculoRepository.findAll();
    }
}
