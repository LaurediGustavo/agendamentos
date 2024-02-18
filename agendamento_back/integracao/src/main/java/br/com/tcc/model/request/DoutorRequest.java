package br.com.tcc.model.request;

import br.com.tcc.validation.funcionario.interfaces.ValidFuncionarioId;
import br.com.tcc.validation.generic.interfaces.ValidObrigatorio;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class DoutorRequest {

    @JsonProperty(required = false)
    @ValidFuncionarioId
    private Long id;

    private FuncionarioRequest funcionario;

    @ValidObrigatorio
    private String cro;

    @ValidObrigatorio
    private List<Long> procedimentos;

}
