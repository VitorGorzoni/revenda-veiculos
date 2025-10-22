package org.com.revenda.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.com.revenda.domain.entity.StatusPagamento;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "vendas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendaJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "veiculo_id", nullable = false)
    private Long veiculoId;

    @Column(name = "cpf_cliente", nullable = false, length = 14)
    private String cpfCliente;

    @Column(name = "nome_cliente", nullable = false, length = 200)
    private String nomeCliente;

    @Column(name = "valor_venda", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorVenda;

    @Column(name = "codigo_pagamento", nullable = false, unique = true, length = 50)
    private String codigoPagamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_pagamento", nullable = false, length = 20)
    private StatusPagamento statusPagamento;

    @Column(name = "data_venda", nullable = false)
    private LocalDateTime dataVenda;
}

