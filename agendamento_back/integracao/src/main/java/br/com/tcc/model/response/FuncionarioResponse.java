package br.com.tcc.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class FuncionarioResponse {
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
    private TipoFuncionarioResponse tipoFuncionarioResponse;
}