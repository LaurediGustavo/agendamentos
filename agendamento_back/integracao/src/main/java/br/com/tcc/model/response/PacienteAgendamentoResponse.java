package br.com.tcc.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class PacienteAgendamentoResponse {
    private Long id;
    private String nome;
    private String sobrenome;
    private String cpf;
    private String telefone;
}
