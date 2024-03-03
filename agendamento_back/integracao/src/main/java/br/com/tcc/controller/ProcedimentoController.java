package br.com.tcc.controller;

import br.com.tcc.dto.PacienteDto;
import br.com.tcc.dto.ProcedimentoDto;
import br.com.tcc.impl.ProcedimentoService;
import br.com.tcc.model.request.PacienteRequest;
import br.com.tcc.model.request.ProcedimentoRequest;
import br.com.tcc.model.response.ProcedimentoResponse;
import br.com.tcc.service.ProcedimentoTratarResponse;
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
@RequestMapping("/procedimento")
public class ProcedimentoController {

    @Autowired
    private ProcedimentoTratarResponse procedimentoTratarResponse;

    @Autowired
    private ProcedimentoService procedimentoService;

    @GetMapping(value = "/consultar")
    public ResponseEntity<?> consultarPorTratamento(@Param("tratamento") String tratamento, @Param("desabilitado") Boolean desabilitado) {
        List<ProcedimentoResponse> procedimentoResponseList = procedimentoTratarResponse
                .consultarPorTratamento(tratamento, desabilitado);

        return ResponseEntity.status(HttpStatus.OK).body(procedimentoResponseList);
    }

    @GetMapping(value = "/consultar/{id}")
    public ResponseEntity<?> consultarPorId(@PathVariable("id") Long id) {
        ProcedimentoResponse procedimentoResponse = procedimentoTratarResponse
                .consultarPorId(id);

        return ResponseEntity.status(HttpStatus.OK).body(procedimentoResponse);
    }

    @PostMapping(value = "/cadastro", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ATENDENTE')")
    public ResponseEntity<?> cadastro(@Valid @RequestBody ProcedimentoRequest procedimentoRequest) {
        ProcedimentoDto procedimentoDto = new ProcedimentoDto();
        BeanUtils.copyProperties(procedimentoRequest, procedimentoDto);
        Long id = procedimentoService.cadastrar(procedimentoDto);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("id", id);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    @PutMapping(value = "/atualizar", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ATENDENTE')")
    public ResponseEntity<?> atualizar(@Valid @RequestBody ProcedimentoRequest procedimentoRequest) {
        ProcedimentoDto procedimentoDto = new ProcedimentoDto();
        BeanUtils.copyProperties(procedimentoRequest, procedimentoDto);
        procedimentoService.atualizar(procedimentoDto);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping(value = "delete/{id}")
    public ResponseEntity<?> deletar(@PathVariable("id") Long id) {
        if (!procedimentoService.existsById(id))
            return ResponseEntity.notFound().build();

        procedimentoService.deletar(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping(value = "revertdelete/{id}")
    public ResponseEntity<?> revertDelete(@PathVariable("id") Long id) {
        if (!procedimentoService.existsById(id))
            return ResponseEntity.notFound().build();

        procedimentoService.revertDelete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
