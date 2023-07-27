package br.com.tcc.dto;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class AgendamentoDto {

	private LocalDateTime dataHoraInicio;

	private LocalDateTime dataHoraFim;

	private Long pacienteId;

	private Long doutorId;

	private List<Long> procedimentosIds;

}
