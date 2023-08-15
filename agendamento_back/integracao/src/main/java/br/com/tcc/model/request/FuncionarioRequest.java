package br.com.tcc.model.request;

import br.com.tcc.validation.funcionario.interfaces.ValidCro;
import br.com.tcc.validation.generic.interfaces.ValidCpf;
import br.com.tcc.validation.generic.interfaces.ValidObrigatorio;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@ValidCro
public class FuncionarioRequest {

    @JsonProperty(required = false)
    private Long id;

    @ValidObrigatorio
    private String nome;

    @ValidObrigatorio
    private String sobrenome;

    @ValidObrigatorio
    private Integer idade;

    @ValidObrigatorio
    @ValidCpf
    private String cpf;

    @ValidObrigatorio
    private String genero;

    @ValidObrigatorio
    private String telefone;

    @ValidObrigatorio
    private String logradouro;

    @ValidObrigatorio
    private String bairro;

    @ValidObrigatorio
    private String numero;

    private String bloco;

    @ValidObrigatorio
    private Long tipo_funcionario_id;

    @JsonProperty(required = false)
    private String cro;
}
