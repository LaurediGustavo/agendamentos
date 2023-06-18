package br.com.tcc.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.com.tcc.dto.ConsultaDto;
import br.com.tcc.interfaces.ConsultaServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/agendamento")
public class AgendamentoController {

    private static final Logger LOGGER = Logger.getLogger(AgendamentoController.class.getName());
	
	@Autowired
	private ConsultaServiceInterface consultaService;
	
	@PostMapping("/cadastro")
	//@PreAuthorize("hasRole('ROLE_ATENDENTE')")
	public ResponseEntity<?> cadastroAgendamento(@RequestBody ConsultaDto consultaDto) {
		Map<String, Object> responseMap = new HashMap<>();
        HttpStatus status;
		
		try {
			consultaService.persistir(consultaDto);
			status = HttpStatus.CREATED;
		}
//		catch (ConsistirException e) {
//			status = HttpStatus.OK;
//			responseMap = e.getAllErrors();
//		}
		catch (Exception e) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			responseMap.put("error", e);
			LOGGER.log(Level.SEVERE, "Ocorreu uma exceção: ", e);
		}
		
		return ResponseEntity.status(status).body(responseMap);
	}
	
}
