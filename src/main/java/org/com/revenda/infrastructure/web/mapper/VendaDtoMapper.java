package org.com.revenda.infrastructure.web.mapper;

import lombok.RequiredArgsConstructor;
import org.com.revenda.application.gateway.VeiculoPersistenceGateway;
import org.com.revenda.domain.entity.Veiculo;
import org.com.revenda.domain.entity.Venda;
import org.com.revenda.infrastructure.web.dto.response.VendaComVeiculoResponse;
import org.com.revenda.infrastructure.web.dto.response.VendaResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class VendaDtoMapper {

    private final VeiculoPersistenceGateway veiculoRepository;

    /**
     * Mapeia uma Venda para VendaResponse simples
     */
    public VendaResponse toResponse(Venda venda) {
        return new VendaResponse(
            venda.getId(),
            venda.getVeiculoId(),
            venda.getCpfCliente(),
            venda.getDataVenda(),
            venda.getCodigoPagamento(),
            venda.getStatusPagamento()
        );
    }

    /**
     * Mapeia uma Venda para VendaComVeiculoResponse, buscando os dados do veículo
     */
    public VendaComVeiculoResponse toVendaComVeiculoResponse(Venda venda) {
        Veiculo veiculo = veiculoRepository.findById(venda.getVeiculoId())
                .orElse(null);

        if (veiculo == null) {
            // Se o veículo não for encontrado, retorna null ou uma resposta parcial
            return null;
        }

        VendaComVeiculoResponse response = new VendaComVeiculoResponse();
        response.setVendaId(venda.getId());
        response.setCodigoPagamento(venda.getCodigoPagamento());
        response.setCpfCliente(venda.getCpfCliente());
        response.setNomeCliente(venda.getNomeCliente());
        response.setValorVenda(venda.getValorVenda());
        response.setStatusPagamento(venda.getStatusPagamento().name());
        response.setDataVenda(venda.getDataVenda());

        // Dados do veículo
        response.setVeiculoId(veiculo.getId());
        response.setMarca(veiculo.getMarca());
        response.setModelo(veiculo.getModelo());
        response.setAno(veiculo.getAno());
        response.setCor(veiculo.getCor());
        response.setPrecoOriginal(veiculo.getPreco());

        return response;
    }

    /**
     * Mapeia uma lista de Vendas para uma lista de VendaResponse
     */
    public List<VendaResponse> toResponseList(List<Venda> vendas) {
        return vendas.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Mapeia uma lista de Vendas para uma lista de VendaComVeiculoResponse
     */
    public List<VendaComVeiculoResponse> toVendaComVeiculoResponseList(List<Venda> vendas) {
        return vendas.stream()
                .map(this::toVendaComVeiculoResponse)
                .filter(response -> response != null) // Filtra vendas sem veículo
                .collect(Collectors.toList());
    }
}
