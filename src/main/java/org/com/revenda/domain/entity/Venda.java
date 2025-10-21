package org.com.revenda.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Venda {
    private Long id;
    private Long veiculoId;
    private String cpfComprador;
    private LocalDateTime dataVenda;
    private String codigoPagamento;
    private StatusPagamento statusPagamento;

    public Venda(Long veiculoId, String cpfComprador, String codigoPagamento) {
        this.veiculoId = veiculoId;
        this.cpfComprador = cpfComprador;
        this.codigoPagamento = codigoPagamento;
        this.dataVenda = LocalDateTime.now();
        this.statusPagamento = StatusPagamento.PENDENTE;
    }

    public Venda(Long veiculoId, String cpfComprador, LocalDateTime dataVenda, String codigoPagamento) {
        this.veiculoId = veiculoId;
        this.cpfComprador = cpfComprador;
        this.dataVenda = dataVenda;
        this.codigoPagamento = codigoPagamento;
        this.statusPagamento = StatusPagamento.PENDENTE;
    }

    public void confirmarPagamento() {
        this.statusPagamento = StatusPagamento.CONFIRMADO;
    }

    public void cancelarPagamento() {
        this.statusPagamento = StatusPagamento.CANCELADO;
    }

    public boolean isPagamentoConfirmado() {
        return StatusPagamento.CONFIRMADO.equals(this.statusPagamento);
    }
}
