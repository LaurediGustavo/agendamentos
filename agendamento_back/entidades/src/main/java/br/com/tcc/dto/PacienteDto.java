package br.com.tcc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PacienteDto {

    private Long id;

    private String nome;

    private String sobrenome;

    private Integer idade;

    private String cpf;

    private String genero;

    private String telefone;

    private String informacoesAdicionais;

    private String logradouro;

    private String bairro;

    private Integer numero;

    private String bloco;

    private String parentesco;

    private Long responsavel_paciente_id;

}
