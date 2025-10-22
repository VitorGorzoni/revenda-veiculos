package org.com.revenda.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Venda {
    private Long id;
    private Long veiculoId;
    private String cpfCliente;
    private String nomeCliente;
    private BigDecimal valorVenda;
    private String codigoPagamento;
    private StatusPagamento statusPagamento;
    private LocalDateTime dataVenda;

    public Venda() {}

    public Venda(Long veiculoId, String cpfCliente, String nomeCliente, BigDecimal valorVenda) {
        this.veiculoId = veiculoId;
        this.cpfCliente = cpfCliente;
        this.nomeCliente = nomeCliente;
        this.valorVenda = valorVenda;
        this.codigoPagamento = "PAG-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.statusPagamento = StatusPagamento.PENDENTE;
        this.dataVenda = LocalDateTime.now();
    }

    // Getters
    public Long getId() { return id; }
    public Long getVeiculoId() { return veiculoId; }
    public String getCpfCliente() { return cpfCliente; }
    public String getNomeCliente() { return nomeCliente; }
    public BigDecimal getValorVenda() { return valorVenda; }
    public String getCodigoPagamento() { return codigoPagamento; }
    public StatusPagamento getStatusPagamento() { return statusPagamento; }
    public LocalDateTime getDataVenda() { return dataVenda; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setVeiculoId(Long veiculoId) { this.veiculoId = veiculoId; }
    public void setCpfCliente(String cpfCliente) { this.cpfCliente = cpfCliente; }
    public void setNomeCliente(String nomeCliente) { this.nomeCliente = nomeCliente; }
    public void setValorVenda(BigDecimal valorVenda) { this.valorVenda = valorVenda; }
    public void setCodigoPagamento(String codigoPagamento) { this.codigoPagamento = codigoPagamento; }
    public void setStatusPagamento(StatusPagamento statusPagamento) { this.statusPagamento = statusPagamento; }
    public void setDataVenda(LocalDateTime dataVenda) { this.dataVenda = dataVenda; }

    // Métodos de negócio
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
