package org.com.revenda.domain.usecase;

import lombok.RequiredArgsConstructor;
import org.com.revenda.domain.entity.Veiculo;
import org.com.revenda.domain.repository.VeiculoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListarTodosVeiculosUseCase {

    private final VeiculoRepository veiculoRepository;

    public List<Veiculo> execute() {
        return veiculoRepository.findAll();
    }
}

