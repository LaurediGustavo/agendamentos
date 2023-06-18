package br.com.tcc.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ConsultaDto {

	private Date dataHoraInicio;

	private Date dataHoraFim;

	private Long pacienteId;

	private Long doutorId;

	private Long procedimentoId;	

}
