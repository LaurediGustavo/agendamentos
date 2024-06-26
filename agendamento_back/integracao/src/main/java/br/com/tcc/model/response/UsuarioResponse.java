package br.com.tcc.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
public class UsuarioResponse {
    private Long id;
    private String nome;
    private String sobrenome;
    private LocalDate dataDeNascimento;
    private String cpf;
    private String genero;
    private String telefone;
    private String cep;
    private String logradouro;
    private String bairro;
    private Integer numero;
    private String bloco;
    private String email;
}
