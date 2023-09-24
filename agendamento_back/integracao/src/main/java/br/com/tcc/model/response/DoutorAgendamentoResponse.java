package br.com.tcc.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class DoutorAgendamentoResponse {

    private Long id;

    private String nome;

    private String sobreNome;

}
