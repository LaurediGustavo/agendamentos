package br.com.tcc.controller;

import br.com.tcc.impl.UsuarioService;
import br.com.tcc.model.request.AlterarSenhaRequest;
import br.com.tcc.model.response.UsuarioResponse;
import br.com.tcc.service.UsuarioTratarResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.hibernate5.SpringSessionContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioTratarResponse usuarioTratarResponse;

    @PutMapping(value = "/alterarsenha", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> alterarSenha(@Valid @RequestBody AlterarSenhaRequest alterarSenhaRequest) {
        usuarioService.alterarSenha(alterarSenhaRequest.getConfirmNewPassword());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping(value = "/usuariologado")
    public ResponseEntity<?> usuarioLogado() {
        UsuarioResponse user = usuarioTratarResponse.usuarioLogado();
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PutMapping("/alterarimagem")
    public ResponseEntity<String> alterarImagem(@RequestParam("image") MultipartFile image) throws IOException {
        usuarioService.alterarImagem(image);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping(value = "/imagem")
    public ResponseEntity<?> imagem() throws MalformedURLException {
        Map<String, Object> mapa = usuarioTratarResponse.imagem();
        return ResponseEntity.status(HttpStatus.OK)
                .contentType((MediaType) mapa.get(UsuarioTratarResponse.MEDIA_TYPE))
                .body((Resource) mapa.get(UsuarioTratarResponse.RESOURCE));
    }

    @PutMapping("/removerimagem")
    public ResponseEntity<String> removerImagem() throws IOException {
        usuarioService.removerImagem();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
