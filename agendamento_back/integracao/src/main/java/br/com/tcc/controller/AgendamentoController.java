package br.com.tcc.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.com.tcc.dto.AgendamentoDto;
import br.com.tcc.interfaces.ConsultaServiceInterface;
import br.com.tcc.model.Agendamento;
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

    private static final Logger LOGGER = Logger.getLogger(AgendamentoController.class.getName());
	
	@Autowired
	private ConsultaServiceInterface consultaService;
	
	@PostMapping(value = "/cadastro", consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_ATENDENTE')")
	public ResponseEntity<?> cadastroAgendamento(@Valid @RequestBody Agendamento agendamento) {
		Map<String, Object> responseMap = new HashMap<>();
        HttpStatus status;
		
		try {
			AgendamentoDto agendamentoDto = new AgendamentoDto();
			BeanUtils.copyProperties(agendamento, agendamentoDto);
			consultaService.persistir(agendamentoDto);
			status = HttpStatus.CREATED;
		}
		catch (Exception e) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			responseMap.put("error", e);
			LOGGER.log(Level.SEVERE, "Ocorreu uma exceção: ", e);
		}
		
		return ResponseEntity.status(status).body(responseMap);
	}
	
}
