package br.com.tcc.model.request;

import br.com.tcc.validation.generic.interfaces.ValidObrigatorio;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class ProcedimentoRequest {

    @JsonProperty(required = false)
    private Long id;
    @ValidObrigatorio
    private String tratamento;
    @ValidObrigatorio
    private String tempo;
    @ValidObrigatorio
    private BigDecimal valor;
    private Boolean desabilitado;
}
