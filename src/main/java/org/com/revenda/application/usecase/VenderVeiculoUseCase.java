package org.com.revenda.application.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.com.revenda.domain.entity.Veiculo;
import org.com.revenda.domain.entity.Venda;
import org.com.revenda.domain.repository.VeiculoRepository;
import org.com.revenda.domain.repository.VendaRepository;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Log4j2
public class VenderVeiculoUseCase {

    private final VeiculoRepository veiculoRepository;
    private final VendaRepository vendaRepository;

    public Venda execute(Long veiculoId, String cpfCliente, String nomeCliente, BigDecimal valorVenda) {
        // Buscar o veículo
        Veiculo veiculo = veiculoRepository.findById(veiculoId)
            .orElseThrow(() -> new IllegalArgumentException("Veículo não encontrado com ID: " + veiculoId));

        // Verificar se o veículo está disponível
        if (!veiculo.isDisponivel()) {
            throw new IllegalStateException("Veículo não está disponível para venda");
        }

        // Criar a venda
        Venda venda = new Venda(veiculoId, cpfCliente, nomeCliente, valorVenda);

        // Marcar veículo como vendido
        veiculo.marcarComoVendido();
        veiculoRepository.save(veiculo);

        // Salvar a venda
        return vendaRepository.save(venda);
    }
}
