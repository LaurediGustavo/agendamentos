package br.com.tcc.model;

import br.com.tcc.validation.agendamento.interfaces.ValidAgendamentoDataHoraInicio;
import br.com.tcc.validation.agendamento.interfaces.ValidAgendamentoDoutor;
import br.com.tcc.validation.agendamento.interfaces.ValidAgendamentoPaciente;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Agendamento {

    @ValidAgendamentoDataHoraInicio
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataHoraInicio;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataHoraFim;

    @ValidAgendamentoPaciente
    private Long pacienteId;

    @ValidAgendamentoDoutor
    private Long doutorId;

    private List<Long> procedimentosIds;

}
