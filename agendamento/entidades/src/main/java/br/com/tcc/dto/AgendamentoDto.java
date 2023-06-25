package br.com.tcc.dto;

import java.util.Date;

import lombok.Data;

@Data
public class AgendamentoDto {

	private Date dataHoraInicio;

	private Date dataHoraFim;

	private Long pacienteId;

	private Long doutorId;

	private Long procedimentoId;	

}
