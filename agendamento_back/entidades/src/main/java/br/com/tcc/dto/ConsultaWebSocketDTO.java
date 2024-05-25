package br.com.tcc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ConsultaWebSocketDTO {

    private Long consultaId;

    private String pacienteNome;

    private LocalDateTime dataHoraInicio;

    private LocalDateTime dataHoraFim;

    private String status;

}
