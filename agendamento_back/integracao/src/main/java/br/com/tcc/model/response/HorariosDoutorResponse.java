package br.com.tcc.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@Setter
@Getter
public class HorariosDoutorResponse {

    private Long doutorId;

    private LocalDateTime dataInicio;

    private LocalDateTime dataFim;

}
