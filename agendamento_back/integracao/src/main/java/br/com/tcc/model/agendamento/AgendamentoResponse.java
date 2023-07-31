package br.com.tcc.model.agendamento;

import br.com.tcc.validation.agendamento.interfaces.*;
import br.com.tcc.validation.generic.interfaces.ValidObrigatorio;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AgendamentoResponse {

    private Long id;

    private String status;

    private LocalDateTime dataHoraInicio;

    private LocalDateTime dataHoraFim;

    private Long pacienteId;

    private Long doutorId;

    private List<Long> procedimentosIds;

}
