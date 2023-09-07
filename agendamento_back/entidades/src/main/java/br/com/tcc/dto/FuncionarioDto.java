package br.com.tcc.dto;

import lombok.Data;

@Data
public class FuncionarioDto {

    private Long id;

    private String nome;

    private String sobrenome;

    private Integer idade;

    private String cpf;

    private String genero;

    private String telefone;

    private String logradouro;

    private String bairro;

    private Integer numero;

    private String bloco;

    private Long tipo_funcionario_id;

    private String cro;

}
