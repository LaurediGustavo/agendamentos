package br.com.tcc.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProcedimentoDto {

    private Long id;

    private String tratamento;

    private String tempo;

    private BigDecimal valor;

}
