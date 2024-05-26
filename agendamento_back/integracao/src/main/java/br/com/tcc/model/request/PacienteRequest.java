package br.com.tcc.model.request;

import br.com.tcc.validation.generic.interfaces.ValidCpf;
import br.com.tcc.validation.generic.interfaces.ValidObrigatorio;
import br.com.tcc.validation.paciente.interfaces.ValidCpfPaciente;
import br.com.tcc.validation.paciente.interfaces.ValidPacienteId;
import br.com.tcc.validation.paciente.interfaces.ValidResponsavel;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
@ValidCpfPaciente
public class PacienteRequest {

    @ValidPacienteId
    @JsonProperty(required = false)
    private Long id;

    @ValidObrigatorio
    private String nome;

    @ValidObrigatorio
    private String sobrenome;

    @ValidObrigatorio
    private LocalDate dataDeNascimento;

    @ValidCpf
    @JsonProperty(required = false)
    private String cpf;

    @ValidObrigatorio
    private String genero;

    @ValidObrigatorio
    private String telefone;

    @JsonProperty(required = false)
    private String informacoesAdicionais;

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

    @JsonProperty(required = false)
    private String parentesco;

    @ValidResponsavel
    @JsonProperty(required = false)
    private Long responsavel_paciente_id;

}
