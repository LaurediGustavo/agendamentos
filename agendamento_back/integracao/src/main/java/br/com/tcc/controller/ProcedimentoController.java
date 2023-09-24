package br.com.tcc.controller;

import br.com.tcc.model.response.PacienteResponse;
import br.com.tcc.model.response.ProcedimentoResponse;
import br.com.tcc.service.ProcedimentoTratarResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/procedimento")
public class ProcedimentoController {

    @Autowired
    private ProcedimentoTratarResponse procedimentoTratarResponse;

    @GetMapping(value = "/consultar")
    public ResponseEntity<?> consultarPorTratamento(@Param("tratamento") String tratamento) {
        List<ProcedimentoResponse> procedimentoResponseList = procedimentoTratarResponse
                .consultarPorTratamento(tratamento);

        return ResponseEntity.status(HttpStatus.OK).body(procedimentoResponseList);
    }

    @GetMapping(value = "/consultar/{id}")
    public ResponseEntity<?> consultarPorId(@PathVariable("id") Long id) {
        ProcedimentoResponse procedimentoResponse = procedimentoTratarResponse
                .consultarPorId(id);

        return ResponseEntity.status(HttpStatus.OK).body(procedimentoResponse);
    }

}
