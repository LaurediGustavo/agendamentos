package br.com.tcc.controller;

import br.com.tcc.dto.PacienteDto;
import br.com.tcc.impl.UsuarioService;
import br.com.tcc.model.request.AlterarSenhaRequest;
import br.com.tcc.model.request.PacienteRequest;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PutMapping(value = "/alterarsenha", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> alterarSenha(@Valid @RequestBody AlterarSenhaRequest alterarSenhaRequest) {
        usuarioService.alterarSenha(alterarSenhaRequest.getConfirmNewPassword());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
