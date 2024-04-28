package br.com.tcc.model.response;

import br.com.tcc.validation.agendamento.interfaces.*;
import br.com.tcc.validation.generic.interfaces.ValidObrigatorio;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    private String pacienteNome;

    private Long doutorId;

    private List<ProcedimentoResponse> procedimentos;

    private BigDecimal valorTotal;

    private LocalTime tempoAproximado;

    private AgendamentoResponse remercado;

    private AgendamentoResponse consultaEstendidaDe;

}
