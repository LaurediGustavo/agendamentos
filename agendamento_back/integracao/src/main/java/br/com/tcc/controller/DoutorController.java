package br.com.tcc.controller;

import br.com.tcc.dto.FuncionarioDto;
import br.com.tcc.impl.DoutorService;
import br.com.tcc.impl.FuncionarioService;
import br.com.tcc.model.request.DoutorAgendamentoRequest;
import br.com.tcc.model.request.FuncionarioRequest;
import br.com.tcc.model.response.DoutorAgendamentoResponse;
import br.com.tcc.model.response.DoutorResponse;
import br.com.tcc.model.response.FuncionarioResponse;
import br.com.tcc.model.response.PacienteResponse;
import br.com.tcc.service.DoutorTratarResponse;
import br.com.tcc.service.FuncionarioTratarResponse;
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
@RequestMapping("/doutor")
public class DoutorController {

    @Autowired
    private DoutorService doutorService;

    @Autowired
    private DoutorTratarResponse doutorTratarResponse;

    @PostMapping(value = "/cadastro", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ATENDENTE')")
    public ResponseEntity<?> cadastro(@Valid @RequestBody FuncionarioRequest funcionarioRequest) {
        FuncionarioDto funcionarioDto = new FuncionarioDto();
        BeanUtils.copyProperties(funcionarioRequest, funcionarioDto);
        doutorService.cadastrar(funcionarioDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping(value = "/atualizar", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ATENDENTE')")
    public ResponseEntity<?> atualizar(@Valid @RequestBody FuncionarioRequest funcionarioRequest) {
        FuncionarioDto funcionarioDto = new FuncionarioDto();
        BeanUtils.copyProperties(funcionarioRequest, funcionarioDto);
        doutorService.atualizar(funcionarioDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(value = "/consultar")
    public ResponseEntity<?> consultarDoutorPorNome(@Param("nome") String nome) {
        List<DoutorResponse> doutorResponseList = doutorTratarResponse
                .consultarPorNome(nome);

        return ResponseEntity.status(HttpStatus.OK).body(doutorResponseList);
    }

    @GetMapping(value = "/consultar/{id}")
    public ResponseEntity<?> consultarDoutorPorId(@PathVariable("id") Long id) {
        DoutorResponse doutorResponse = doutorTratarResponse
                .consultarPorId(id);

        return ResponseEntity.status(HttpStatus.OK).body(doutorResponse);
    }

    @PostMapping(value = "/consultar/agendamento")
    public ResponseEntity<?> consultarDoutorParaAgendamento(@RequestBody DoutorAgendamentoRequest request) {
        List<DoutorAgendamentoResponse> doutorResponseList = doutorTratarResponse
                .consultarPorNomeEProcedimento(request.getNome(), request.getProcedimentos());

        return ResponseEntity.status(HttpStatus.OK).body(doutorResponseList);
    }

}
