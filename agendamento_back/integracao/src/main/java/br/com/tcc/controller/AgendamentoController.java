package br.com.tcc.controller;

import br.com.tcc.dto.AgendamentoDto;
import br.com.tcc.impl.AgendamentoService;
import br.com.tcc.model.AgendamentoRequest;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/agendamento")
public class AgendamentoController {
	
	@Autowired
	private AgendamentoService agendamentoService;
	
	@PostMapping(value = "/cadastro", consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_ATENDENTE')")
	public ResponseEntity<?> cadastroAgendamento(@Valid @RequestBody AgendamentoRequest agendamento) {
		AgendamentoDto agendamentoDto = new AgendamentoDto();
		BeanUtils.copyProperties(agendamento, agendamentoDto);
		agendamentoService.persistir(agendamentoDto);

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
}
