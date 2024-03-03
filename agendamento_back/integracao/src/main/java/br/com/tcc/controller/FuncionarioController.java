package br.com.tcc.controller;

import br.com.tcc.dto.FuncionarioDto;
import br.com.tcc.impl.FuncionarioService;
import br.com.tcc.model.request.FuncionarioRequest;
import br.com.tcc.model.response.FuncionarioResponse;
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
@RequestMapping("/funcionario")
public class FuncionarioController {

    @Autowired
    private FuncionarioService funcionarioService;

    @Autowired
    private FuncionarioTratarResponse funcionarioTratarResponse;

    @PostMapping(value = "/cadastro", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ATENDENTE')")
    public ResponseEntity<?> cadastro(@Valid @RequestBody FuncionarioRequest funcionarioRequest) {
        FuncionarioDto funcionarioDto = new FuncionarioDto();
        BeanUtils.copyProperties(funcionarioRequest, funcionarioDto);
        Long id = funcionarioService.cadastrar(funcionarioDto);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("id", id);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    @PutMapping(value = "/atualizar", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ATENDENTE')")
    public ResponseEntity<?> atualizar(@Valid @RequestBody FuncionarioRequest funcionarioRequest) {
        FuncionarioDto funcionarioDto = new FuncionarioDto();
        BeanUtils.copyProperties(funcionarioRequest, funcionarioDto);
        funcionarioService.atualizar(funcionarioDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(value = "/consultar")
    public ResponseEntity<?> consultarFuncionarioPorNome(@Param("nome") String nome, @Param("desabilitado") Boolean desabilitado) {
        List<FuncionarioResponse> funcionarioResponseList = funcionarioTratarResponse
                .consultarPorNome(nome, desabilitado);

        return ResponseEntity.status(HttpStatus.OK).body(funcionarioResponseList);
    }

    @GetMapping(value = "/consultar/{id}")
    public ResponseEntity<?> consultarFuncionarioPorId(@PathVariable("id") Long id) {
        FuncionarioResponse funcionarioResponse = funcionarioTratarResponse
                .consultarPorId(id);

        return ResponseEntity.status(HttpStatus.OK).body(funcionarioResponse);
    }

    @DeleteMapping(value = "delete/{id}")
    @PreAuthorize("hasRole('ROLE_ATENDENTE')")
    public ResponseEntity<?> deletar(@PathVariable("id") Long id) {
        if (!funcionarioService.existsById(id))
            return ResponseEntity.notFound().build();

        funcionarioService.deletar(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping(value = "revertdelete/{id}")
    @PreAuthorize("hasRole('ROLE_ATENDENTE')")
    public ResponseEntity<?> revertDeleta(@PathVariable("id") Long id) {
        if (!funcionarioService.existsById(id))
            return ResponseEntity.notFound().build();

        funcionarioService.revertDelete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
