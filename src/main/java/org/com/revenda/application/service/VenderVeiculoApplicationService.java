package org.com.revenda.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.com.revenda.domain.entity.Venda;
import org.com.revenda.domain.repository.VendaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Log4j2
public class VenderVeiculoApplicationService {

    private final VendaRepository vendaRepository;

    @Transactional
    public Venda execute(Long veiculoId, String cpfCliente, String nomeCliente, BigDecimal valorVenda) {
        log.info("Executando venda do veículo ID: {} para cliente CPF: {}", veiculoId, cpfCliente);

        // Criar a venda
        Venda venda = new Venda(veiculoId, cpfCliente, nomeCliente, valorVenda);

        // Salvar a venda
        Venda vendaSalva = vendaRepository.save(venda);

        log.info("Venda executada com sucesso. Código pagamento: {}", vendaSalva.getCodigoPagamento());

        return vendaSalva;
    }
}

