package br.com.tcc.controller;

import br.com.tcc.model.response.PacienteResponse;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/procedimento")
public class ProcedimentoController {



    @GetMapping(value = "/consultar/agendamento")
    public ResponseEntity<?> consultarPorTratamento(@Param("tratamento") String tratamento) {
        //List<PacienteResponse> pacienteResponseList = pacienteTratarResponse
        //        .consultarPorCpfNome(cpf, nome);

        return null;// ResponseEntity.status(HttpStatus.OK).body(pacienteResponseList);
    }

}
