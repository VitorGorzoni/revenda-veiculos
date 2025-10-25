package org.com.revenda.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.com.revenda.application.gateway.VeiculoPersistenceGateway;
import org.com.revenda.application.gateway.VendaPersistenceGateway;
import org.com.revenda.application.usecase.VenderVeiculoUseCase;
import org.com.revenda.domain.entity.Veiculo;
import org.com.revenda.domain.entity.Venda;
import org.com.revenda.domain.exception.VeiculoNaoEncontradoException;

import java.math.BigDecimal;

/**
 * Implementação do Use Case de vender veículo.
 */
@RequiredArgsConstructor
@Slf4j
public class VenderVeiculoService implements VenderVeiculoUseCase {

    private final VeiculoPersistenceGateway veiculoPersistenceGateway;
    private final VendaPersistenceGateway vendaPersistenceGateway;

    @Override
    public Venda execute(Long veiculoId, String cpfCliente, String nomeCliente, BigDecimal valorVenda) {
        log.info("Iniciando venda do veículo ID: {}", veiculoId);

        // Buscar o veículo
        Veiculo veiculo = veiculoPersistenceGateway.findById(veiculoId)
            .orElseThrow(() -> new VeiculoNaoEncontradoException("Veículo não encontrado com ID: " + veiculoId));

        // Verificar se o veículo está disponível
        if (!veiculo.isDisponivel()) {
            throw new IllegalStateException("Veículo não está disponível para venda");
        }

        // Criar a venda
        Venda venda = new Venda(veiculoId, cpfCliente, nomeCliente, valorVenda);

        // Marcar veículo como reservado (aguardando confirmação de pagamento)
        veiculo.marcarComoReservado();
        veiculoPersistenceGateway.save(veiculo);

        // Salvar a venda
        Venda vendaSalva = vendaPersistenceGateway.save(venda);

        log.info("Venda registrada com sucesso. Veículo reservado. Código de pagamento: {}", vendaSalva.getCodigoPagamento());
        return vendaSalva;
    }
}
