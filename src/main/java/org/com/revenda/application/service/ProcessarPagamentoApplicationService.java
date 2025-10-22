package org.com.revenda.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.com.revenda.domain.entity.StatusPagamento;
import org.com.revenda.application.usecase.ProcessarPagamentoUseCase;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProcessarPagamentoApplicationService {

    private final ProcessarPagamentoUseCase processarPagamentoUseCase;

    public void execute(String codigoPagamento, StatusPagamento novoStatus) {
        log.info("Processando pagamento. Código: {} - Novo status: {}", codigoPagamento, novoStatus);

        processarPagamentoUseCase.execute(codigoPagamento, novoStatus);

        log.info("Pagamento processado com sucesso. Código: {}", codigoPagamento);
    }
}
