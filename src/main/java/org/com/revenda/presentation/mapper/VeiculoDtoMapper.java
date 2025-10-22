package org.com.revenda.presentation.mapper;

import org.com.revenda.domain.entity.Veiculo;
import org.com.revenda.presentation.dto.request.CadastrarVeiculoRequest;
import org.com.revenda.presentation.dto.response.VeiculoResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class VeiculoDtoMapper {

    public Veiculo toDomain(CadastrarVeiculoRequest request) {
        return new Veiculo(
            request.marca(),
            request.modelo(),
            request.ano(),
            request.cor(),
            request.preco()
        );
    }

    public VeiculoResponse toResponse(Veiculo veiculo) {
        return new VeiculoResponse(
            veiculo.getId(),
            veiculo.getMarca(),
            veiculo.getModelo(),
            veiculo.getAno(),
            veiculo.getCor(),
            veiculo.getPreco(),
            veiculo.getStatus(),
            veiculo.getDataCadastro()
        );
    }

    public List<VeiculoResponse> toResponseList(List<Veiculo> veiculos) {
        return veiculos.stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }
}
