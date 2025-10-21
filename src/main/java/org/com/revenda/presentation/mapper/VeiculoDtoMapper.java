package org.com.revenda.presentation.mapper;

import org.com.revenda.domain.entity.Veiculo;
import org.com.revenda.domain.dto.VendaComVeiculo;
import org.com.revenda.presentation.dto.request.CadastrarVeiculoRequest;
import org.com.revenda.presentation.dto.response.VeiculoResponse;
import org.com.revenda.presentation.dto.response.VeiculoVendidoResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class VeiculoDtoMapper {

    public Veiculo toDomain(CadastrarVeiculoRequest request) {
        return new Veiculo(
            request.getMarca(),
            request.getModelo(),
            request.getAno(),
            request.getCor(),
            request.getPreco()
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

    public VeiculoVendidoResponse toVeiculoVendidoResponse(VendaComVeiculo vendaComVeiculo) {
        return new VeiculoVendidoResponse(
            vendaComVeiculo.getVenda().getId(),
            vendaComVeiculo.getVenda().getCpfComprador(),
            vendaComVeiculo.getVenda().getDataVenda(),
            vendaComVeiculo.getVeiculo().getMarca(),
            vendaComVeiculo.getVeiculo().getModelo(),
            vendaComVeiculo.getVeiculo().getAno(),
            vendaComVeiculo.getVeiculo().getCor(),
            vendaComVeiculo.getVeiculo().getPreco()
        );
    }

    public List<VeiculoVendidoResponse> toVeiculoVendidoResponseList(List<VendaComVeiculo> vendasComVeiculos) {
        return vendasComVeiculos.stream()
            .map(this::toVeiculoVendidoResponse)
            .collect(Collectors.toList());
    }
}
