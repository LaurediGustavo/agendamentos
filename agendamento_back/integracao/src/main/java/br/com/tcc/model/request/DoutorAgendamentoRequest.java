package br.com.tcc.model.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DoutorAgendamentoRequest {

    private String nome;

    private Long[] procedimentos;

}
