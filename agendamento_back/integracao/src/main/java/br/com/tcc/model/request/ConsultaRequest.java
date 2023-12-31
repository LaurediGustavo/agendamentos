package br.com.tcc.model.request;

import java.time.LocalDateTime;

public class ConsultaRequest {
    private Long id;
    private LocalDateTime dataHoraInicio;
    private LocalDateTime dataHoraFinal;
    private Long paciente_id;
    private Long doutor_id;
    private Long procedimento_id;
    private String status;
}
