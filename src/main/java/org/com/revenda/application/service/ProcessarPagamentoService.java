package org.com.revenda.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.com.revenda.application.gateway.VeiculoPersistenceGateway;
import org.com.revenda.application.gateway.VendaPersistenceGateway;
import org.com.revenda.application.usecase.ProcessarPagamentoUseCase;
import org.com.revenda.domain.entity.Veiculo;
import org.com.revenda.domain.entity.Venda;
import org.com.revenda.domain.enums.StatusPagamento;
import org.com.revenda.domain.exception.VeiculoNaoEncontradoException;

/**
 * Implementação do Use Case de processar pagamento.
 */
@RequiredArgsConstructor
@Slf4j
public class ProcessarPagamentoService implements ProcessarPagamentoUseCase {

    private final VendaPersistenceGateway vendaPersistenceGateway;
    private final VeiculoPersistenceGateway veiculoPersistenceGateway;

    @Override
    public void execute(String codigoPagamento, StatusPagamento novoStatus) {
        log.info("Processando pagamento: {} - Status: {}", codigoPagamento, novoStatus);

        Venda venda = vendaPersistenceGateway.findByCodigoPagamento(codigoPagamento)
            .orElseThrow(() -> new IllegalArgumentException("Código de pagamento não encontrado: " + codigoPagamento));

        // Buscar o veículo
        Veiculo veiculo = veiculoPersistenceGateway.findById(venda.getVeiculoId())
            .orElseThrow(() -> new VeiculoNaoEncontradoException("Veículo não encontrado com ID: " + venda.getVeiculoId()));

        if (novoStatus == StatusPagamento.CONFIRMADO) {
            venda.confirmarPagamento();
            // Marcar veículo como vendido após confirmação de pagamento
            veiculo.marcarComoVendido();
            log.info("Pagamento confirmado. Veículo ID {} marcado como VENDIDO", veiculo.getId());

        } else if (novoStatus == StatusPagamento.CANCELADO) {
            venda.cancelarPagamento();
            // Devolver veículo para disponível em caso de cancelamento
            veiculo.marcarComoDisponivel();
            log.info("Pagamento cancelado. Veículo ID {} retornou para DISPONÍVEL", veiculo.getId());
        }

        vendaPersistenceGateway.save(venda);
        veiculoPersistenceGateway.save(veiculo);
        log.info("Pagamento processado com sucesso");
    }
}
