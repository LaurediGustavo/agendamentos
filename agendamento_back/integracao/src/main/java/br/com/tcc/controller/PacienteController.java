package br.com.tcc.controller;

import br.com.tcc.dto.PacienteDto;
import br.com.tcc.impl.PacienteService;
import br.com.tcc.model.request.PacienteRequest;
import br.com.tcc.model.response.PacienteAgendamentoResponse;
import br.com.tcc.model.response.PacienteResponse;
import br.com.tcc.service.PacienteTratarResponse;
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
@RequestMapping("/paciente")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private PacienteTratarResponse pacienteTratarResponse;

    @PostMapping(value = "/cadastro", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ATENDENTE')")
    public ResponseEntity<?> cadastro(@Valid @RequestBody PacienteRequest pacienteRequest) {
        PacienteDto pacienteDto = new PacienteDto();
        BeanUtils.copyProperties(pacienteRequest, pacienteDto);
        pacienteService.cadastrar(pacienteDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping(value = "/atualizar", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ATENDENTE')")
    public ResponseEntity<?> atualizar(@Valid @RequestBody PacienteRequest pacienteRequest) {
        PacienteDto pacienteDto = new PacienteDto();
        BeanUtils.copyProperties(pacienteRequest, pacienteDto);
        pacienteService.atualizar(pacienteDto);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping(value = "/consultar")
    public ResponseEntity<?> consultarPorCpfNome(@Param("cpf") String cpf, @Param("nome") String nome) {
        List<PacienteResponse> pacienteResponseList = pacienteTratarResponse
                .consultarPorCpfNome(cpf, nome);

        return ResponseEntity.status(HttpStatus.OK).body(pacienteResponseList);
    }

    @GetMapping(value = "/consultar/{id}")
    public ResponseEntity<?> consultarPorId(@PathVariable("id") Long id) {
        PacienteResponse pacienteResponse = pacienteTratarResponse
                .consultarPorId(id);

        return ResponseEntity.status(HttpStatus.OK).body(pacienteResponse);
    }

    @GetMapping(value = "/consultar/agendamento")
    public ResponseEntity<?> consultarParaAgendamento(@Param("nome") String nome) {
        List<PacienteAgendamentoResponse> pacienteResponseList = pacienteTratarResponse
                .consultarPorNome(nome);

        return ResponseEntity.status(HttpStatus.OK).body(pacienteResponseList);
    }

}
