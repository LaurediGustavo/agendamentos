package br.com.tcc.model.request;

import br.com.tcc.validation.agendamento.interfaces.*;
import br.com.tcc.validation.generic.interfaces.ValidObrigatorio;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ValidAgendamentoDataHoraFinal
public class AgendamentoRequest {

    @ValidAgendamentoId
    @JsonProperty(required = false)
    private Long id;

    @ValidAgendamentoStatus
    @JsonProperty(required = false)
    private String status;

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
