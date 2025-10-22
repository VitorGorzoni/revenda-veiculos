package org.com.revenda.application.usecase;

import lombok.RequiredArgsConstructor;
import org.com.revenda.domain.entity.Veiculo;
import org.com.revenda.domain.repository.VeiculoRepository;

@RequiredArgsConstructor
public class BuscarVeiculoPorIdUseCase {

    private final VeiculoRepository veiculoRepository;

    public Veiculo execute(Long id) {
        return veiculoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Veículo não encontrado com ID: " + id));
    }
}
