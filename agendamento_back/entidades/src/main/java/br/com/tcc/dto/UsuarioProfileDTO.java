package br.com.tcc.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UsuarioProfileDTO {

    private Long id;

    private String nome;

    private String sobrenome;

    private LocalDate dataDeNascimento;

    private String genero;

    private String telefone;

    private String cep;

    private String logradouro;

    private String bairro;

    private Integer numero;

    private String bloco;

    private String email;

}
