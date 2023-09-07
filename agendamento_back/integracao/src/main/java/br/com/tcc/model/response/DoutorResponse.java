package br.com.tcc.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class DoutorResponse {
    private Long id;

    private FuncionarioResponse funcionarioResponse;

    private String cro;
}
