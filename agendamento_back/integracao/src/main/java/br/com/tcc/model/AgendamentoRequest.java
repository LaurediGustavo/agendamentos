package br.com.tcc.model;

import br.com.tcc.validation.agendamento.interfaces.ValidAgendamentoDataHoraFinal;
import br.com.tcc.validation.agendamento.interfaces.ValidAgendamentoDoutor;
import br.com.tcc.validation.agendamento.interfaces.ValidAgendamentoPaciente;
import br.com.tcc.validation.agendamento.interfaces.ValidAgendamentoProcedimento;
import br.com.tcc.validation.generic.interfaces.ValidObrigatorio;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ValidAgendamentoDataHoraFinal
public class AgendamentoRequest {

    @ValidObrigatorio
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataHoraInicio;

    @ValidObrigatorio
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataHoraFim;

    @ValidObrigatorio
    @ValidAgendamentoPaciente
    private Long pacienteId;

    @ValidObrigatorio
    @ValidAgendamentoDoutor
    private Long doutorId;

    @ValidObrigatorio
    @ValidAgendamentoProcedimento
    private List<Long> procedimentosIds;

}
