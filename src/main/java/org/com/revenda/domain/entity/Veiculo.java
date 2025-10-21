package org.com.revenda.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Veiculo {
    private Long id;
    private String marca;
    private String modelo;
    private Integer ano;
    private String cor;
    private BigDecimal preco;
    private StatusVeiculo status;
    private LocalDateTime dataCadastro;

    public Veiculo(String marca, String modelo, Integer ano, String cor, BigDecimal preco) {
        this.marca = marca;
        this.modelo = modelo;
        this.ano = ano;
        this.cor = cor;
        this.preco = preco;
        this.status = StatusVeiculo.DISPONIVEL;
        this.dataCadastro = LocalDateTime.now();
    }

    public void vender() {
        this.status = StatusVeiculo.VENDIDO;
    }

    public boolean isDisponivel() {
        return StatusVeiculo.DISPONIVEL.equals(this.status);
    }

    public boolean isVendido() {
        return StatusVeiculo.VENDIDO.equals(this.status);
    }
}
