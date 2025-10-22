package org.com.revenda.domain.usecase;

import lombok.RequiredArgsConstructor;
import org.com.revenda.domain.entity.Veiculo;
import org.com.revenda.domain.entity.Venda;
import org.com.revenda.domain.exception.VeiculoNaoEncontradoException;
import org.com.revenda.domain.repository.VeiculoRepository;
import org.com.revenda.domain.repository.VendaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VenderVeiculoUseCase {

    private final VeiculoRepository veiculoRepository;
    private final VendaRepository vendaRepository;

    public Venda execute(Long veiculoId, String cpfComprador, LocalDateTime dataVenda) {
        // Validar CPF
        if (cpfComprador == null || cpfComprador.trim().isEmpty()) {
            throw new IllegalArgumentException("CPF é obrigatório");
        }

        String cpfLimpo = cpfComprador.replaceAll("[^0-9]", "");
        if (cpfLimpo.length() != 11) {
            throw new IllegalArgumentException("CPF deve ter 11 dígitos");
        }

        Veiculo veiculo = veiculoRepository.findById(veiculoId)
            .orElseThrow(() -> new VeiculoNaoEncontradoException("Veículo não encontrado com ID: " + veiculoId));

        if (!veiculo.isDisponivel()) {
            throw new IllegalStateException("Veículo não está disponível para venda");
        }

        String codigoPagamento = gerarCodigoPagamento();
        Venda venda = new Venda(veiculoId, cpfComprador, dataVenda, codigoPagamento);

        return vendaRepository.save(venda);
    }

    private String gerarCodigoPagamento() {
        return "PAG-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
