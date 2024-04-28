package br.com.tcc.controller;

import br.com.tcc.dto.DoutorDto;
import br.com.tcc.dto.FuncionarioDto;
import br.com.tcc.impl.DoutorService;
import br.com.tcc.impl.FuncionarioService;
import br.com.tcc.model.request.DoutorAgendamentoRequest;
import br.com.tcc.model.request.DoutorRequest;
import br.com.tcc.model.request.FuncionarioRequest;
import br.com.tcc.model.response.*;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/doutor")
public class DoutorController {

    @Autowired
    private DoutorService doutorService;

    @Autowired
    private DoutorTratarResponse doutorTratarResponse;

    @PostMapping(value = "/cadastro", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ATENDENTE')")
    public ResponseEntity<?> cadastro(@Valid @RequestBody DoutorRequest doutorRequest) {
        DoutorDto doutorDto = new DoutorDto();
        doutorDto.setFuncionario(new FuncionarioDto());
        BeanUtils.copyProperties(doutorRequest, doutorDto);
        BeanUtils.copyProperties(doutorRequest.getFuncionario(), doutorDto.getFuncionario());
        Long id = doutorService.cadastrar(doutorDto);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("id", id);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    @PutMapping(value = "/atualizar", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ATENDENTE')")
    public ResponseEntity<?> atualizar(@Valid @RequestBody DoutorRequest doutorRequest) {
        DoutorDto doutorDto = new DoutorDto();
        doutorDto.setFuncionario(new FuncionarioDto());
        BeanUtils.copyProperties(doutorRequest, doutorDto);
        BeanUtils.copyProperties(doutorRequest.getFuncionario(), doutorDto.getFuncionario());
        doutorService.atualizar(doutorDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(value = "/consultar")
    public ResponseEntity<?> consultarDoutorPorNome(@Param("nome") String nome, @Param("desabilitado") Boolean desabilitado) {
        List<DoutorResponse> doutorResponseList = doutorTratarResponse
                .consultarPorNome(nome, desabilitado);

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

    @GetMapping(value = "/consultar/agendamento/{id}")
    public ResponseEntity<?> consultarDoutorPorIdParaAgendamento(@PathVariable("id") Long id) {
        DoutorAgendamentoResponse doutorResponse = doutorTratarResponse
                .consultarDoutorPorId(id);

        return ResponseEntity.status(HttpStatus.OK).body(doutorResponse);
    }

    @GetMapping(value = "/consultar/{id}/{data}")
    public ResponseEntity<?> consultarHorariosEmUso(@PathVariable("id") Long id, @PathVariable("data") String data) {
        List<HorariosDoutorResponse> horarios = doutorTratarResponse
                .consultarHorariosDoutor(id, data);

        return ResponseEntity.status(HttpStatus.OK).body(horarios);
    }

    @DeleteMapping(value = "delete/{id}")
    @PreAuthorize("hasRole('ROLE_ATENDENTE')")
    public ResponseEntity<?> deletar(@PathVariable("id") Long id) {
        if (!doutorService.existsById(id))
            return ResponseEntity.notFound().build();

        doutorService.deletar(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping(value = "revertdelete/{id}")
    @PreAuthorize("hasRole('ROLE_ATENDENTE')")
    public ResponseEntity<?> revertDeleta(@PathVariable("id") Long id) {
        if (!doutorService.existsById(id))
            return ResponseEntity.notFound().build();

        doutorService.revertDelete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
