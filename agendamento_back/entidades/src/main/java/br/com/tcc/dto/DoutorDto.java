package br.com.tcc.dto;

import lombok.Data;

import java.util.List;

@Data
public class DoutorDto {

    private Long id;

    private FuncionarioDto funcionario;

    private String cro;

    private List<Long> procedimentos;

}
