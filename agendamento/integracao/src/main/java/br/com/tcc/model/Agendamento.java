package br.com.tcc.model;

import br.com.tcc.validation.agendamento.interfaces.ValidDataHoraInicio;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Agendamento {

    @ValidDataHoraInicio
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataHoraInicio;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataHoraFim;

    private Long pacienteId;

    private Long doutorId;

    private Long procedimentoId;

}
