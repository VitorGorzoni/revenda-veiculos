package org.com.revenda.domain.usecase;

import lombok.RequiredArgsConstructor;
import org.com.revenda.domain.entity.StatusPagamento;
import org.com.revenda.domain.entity.Veiculo;
import org.com.revenda.domain.entity.Venda;
import org.com.revenda.domain.exception.VeiculoNaoEncontradoException;
import org.com.revenda.domain.repository.VeiculoRepository;
import org.com.revenda.domain.repository.VendaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProcessarPagamentoUseCase {

    private final VendaRepository vendaRepository;
    private final VeiculoRepository veiculoRepository;

    public void execute(String codigoPagamento, StatusPagamento novoStatus) {
        Venda venda = vendaRepository.findByCodigoPagamento(codigoPagamento)
            .orElseThrow(() -> new IllegalArgumentException("Venda não encontrada para o código de pagamento: " + codigoPagamento));

        if (novoStatus == StatusPagamento.CONFIRMADO) {
            venda.confirmarPagamento();

            // Atualizar status do veículo para vendido
            Veiculo veiculo = veiculoRepository.findById(venda.getVeiculoId())
                .orElseThrow(() -> new VeiculoNaoEncontradoException("Veículo não encontrado"));

            veiculo.vender();
            veiculoRepository.save(veiculo);

        } else if (novoStatus == StatusPagamento.CANCELADO) {
            venda.cancelarPagamento();
        }

        vendaRepository.save(venda);
    }
}
