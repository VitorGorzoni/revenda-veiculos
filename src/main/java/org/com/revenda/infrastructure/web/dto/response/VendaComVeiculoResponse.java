package org.com.revenda.infrastructure.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VendaComVeiculoResponse {
    private Long vendaId;
    private String codigoPagamento;
    private String cpfCliente;
    private String nomeCliente;
    private BigDecimal valorVenda;
    private String statusPagamento;
    private LocalDateTime dataVenda;

    // Dados do veículo
    private Long veiculoId;
    private String marca;
    private String modelo;
    private Integer ano;
    private String cor;
    private BigDecimal precoOriginal;
}
