package br.com.tcc.model.request;

import br.com.tcc.validation.funcionario.interfaces.ValidFuncionarioId;
import br.com.tcc.validation.generic.interfaces.ValidCpf;
import br.com.tcc.validation.generic.interfaces.ValidObrigatorio;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class FuncionarioRequest {

    @JsonProperty(required = false)
    @ValidFuncionarioId
    private Long id;

    @ValidObrigatorio
    private String nome;

    @ValidObrigatorio
    private String sobrenome;

    @ValidObrigatorio
    private LocalDate dataDeNascimento;

    @ValidObrigatorio
    @ValidCpf
    private String cpf;

    @ValidObrigatorio
    private String genero;

    @ValidObrigatorio
    private String telefone;

    @ValidObrigatorio
    private String cep;

    @ValidObrigatorio
    private String logradouro;

    @ValidObrigatorio
    private String bairro;

    @ValidObrigatorio
    private Integer numero;

    @JsonProperty(required = false)
    private String bloco;

}
