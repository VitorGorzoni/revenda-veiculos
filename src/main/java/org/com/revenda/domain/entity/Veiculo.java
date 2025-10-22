package org.com.revenda.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Veiculo {
    private Long id;
    private String marca;
    private String modelo;
    private Integer ano;
    private String cor;
    private BigDecimal preco;
    private StatusVeiculo status;
    private LocalDateTime dataCadastro;

    public Veiculo() {
        this.status = StatusVeiculo.DISPONIVEL;
        this.dataCadastro = LocalDateTime.now();
    }

    public Veiculo(String marca, String modelo, Integer ano, String cor, BigDecimal preco) {
        this.marca = marca;
        this.modelo = modelo;
        this.ano = ano;
        this.cor = cor;
        this.preco = preco;
        this.status = StatusVeiculo.DISPONIVEL;
        this.dataCadastro = LocalDateTime.now();
    }

    // Getters
    public Long getId() { return id; }
    public String getMarca() { return marca; }
    public String getModelo() { return modelo; }
    public Integer getAno() { return ano; }
    public String getCor() { return cor; }
    public BigDecimal getPreco() { return preco; }
    public StatusVeiculo getStatus() { return status; }
    public LocalDateTime getDataCadastro() { return dataCadastro; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setMarca(String marca) { this.marca = marca; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public void setAno(Integer ano) { this.ano = ano; }
    public void setCor(String cor) { this.cor = cor; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }
    public void setStatus(StatusVeiculo status) { this.status = status; }
    public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }

    // Métodos de negócio
    public void marcarComoVendido() {
        if (this.status == StatusVeiculo.VENDIDO) {
            throw new IllegalStateException("Veículo já está vendido");
        }
        this.status = StatusVeiculo.VENDIDO;
    }

    public void marcarComoDisponivel() {
        this.status = StatusVeiculo.DISPONIVEL;
    }

    public boolean isDisponivel() {
        return StatusVeiculo.DISPONIVEL.equals(this.status);
    }

    public boolean isVendido() {
        return StatusVeiculo.VENDIDO.equals(this.status);
    }
}

