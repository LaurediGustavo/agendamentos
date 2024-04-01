package br.com.tcc.model.request;

import br.com.tcc.validation.generic.interfaces.ValidObrigatorio;
import br.com.tcc.validation.usuario.interfaces.ValidExisteUsuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
public class UsuarioProfileRequest {

    @ValidExisteUsuario
    private Long id;

    @ValidObrigatorio
    private String nome;

    @ValidObrigatorio
    private String sobrenome;

    @ValidObrigatorio
    private LocalDate dataDeNascimento;

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

    private String bloco;

    @ValidObrigatorio
    private String email;

}
