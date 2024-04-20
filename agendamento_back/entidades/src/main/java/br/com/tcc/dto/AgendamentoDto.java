package br.com.tcc.dto;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import br.com.tcc.enumerador.StatusConsultaEnum;
import lombok.Data;

@Data
public class AgendamentoDto {

	private Long id;

	private StatusConsultaEnum status;

	private LocalDateTime dataHoraInicio;

	private LocalDateTime dataHoraFim;

	private Long pacienteId;

	private Long doutorId;

	private List<Long> procedimentosIds;

	private Long consultaEstendidaDeId;

}
