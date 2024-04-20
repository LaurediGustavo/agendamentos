package br.com.tcc.controller;

import br.com.tcc.dto.AgendamentoDto;
import br.com.tcc.entity.Consulta;
import br.com.tcc.enumerador.StatusConsultaEnum;
import br.com.tcc.impl.AgendamentoService;
import br.com.tcc.model.request.AgendamentoRequest;
import br.com.tcc.model.response.AgendamentoResponse;
import br.com.tcc.service.AgendamentoTratarResponse;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agendamento")
public class AgendamentoController {
	
	@Autowired
	private AgendamentoService agendamentoService;

	@Autowired
	private AgendamentoTratarResponse agendamentoTratarResponse;
	
	@PostMapping(value = "/cadastro", consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_ATENDENTE')")
	public ResponseEntity<?> cadastroAgendamento(@Valid @RequestBody AgendamentoRequest agendamento) {
		AgendamentoDto agendamentoDto = new AgendamentoDto();
		BeanUtils.copyProperties(agendamento, agendamentoDto);
		Consulta consulta = agendamentoService.persistir(agendamentoDto);

		return consultarPorId(consulta.getId());
	}

	@PutMapping(value = "/atualizar", consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_ATENDENTE')")
	public ResponseEntity<?> atualizaAgendamento(@Valid @RequestBody AgendamentoRequest agendamento) {
		AgendamentoDto agendamentoDto = new AgendamentoDto();
		BeanUtils.copyProperties(agendamento, agendamentoDto);
		agendamentoDto.setStatus(StatusConsultaEnum.valueOf(agendamento.getStatus()));
		Consulta consulta = agendamentoService.persistir(agendamentoDto);

		return consultarPorId(consulta.getId());
	}

	@GetMapping(value = "/consultar")
	public ResponseEntity<?> consultarTodos(@Param("doutorId") Long doutorId,
												 @Param("pacienteId") Long pacienteId,
												 @RequestParam("horario") String horario) {
		List<AgendamentoResponse> agendamentos = agendamentoTratarResponse
				.consultarPorHorarioDoutorPaciente(doutorId, pacienteId, horario);

		return ResponseEntity.status(HttpStatus.OK).body(agendamentos);
	}

	@GetMapping(value = "/consultar/{id}")
	public ResponseEntity<?> consultarPorId(@PathVariable("id") Long id) {
		AgendamentoResponse agendamento = agendamentoTratarResponse
				.consultarPorId(id);

		return ResponseEntity.ok(agendamento);
	}

	@GetMapping(value = "/consultarstatuspaciente")
	public ResponseEntity<?> consultarTodosStatusPaciente(@Param("pacienteId") Long pacienteId,
															@Param("status") String status) {
		List<AgendamentoResponse> agendamentos = agendamentoTratarResponse
				.consultarPorStausPaciente(pacienteId, StatusConsultaEnum.valueOf(status));

		return ResponseEntity.status(HttpStatus.OK).body(agendamentos);
	}
	
}
