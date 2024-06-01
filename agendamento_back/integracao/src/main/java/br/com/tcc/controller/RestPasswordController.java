package br.com.tcc.controller;

import br.com.tcc.impl.ResetPasswordService;
import br.com.tcc.model.request.ResetPasswordRequest;
import br.com.tcc.model.request.ResetPasswordValidatorCodeRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/rest-password")
public class RestPasswordController {

    @Autowired
    private ResetPasswordService resetPasswordService;

    @PostMapping("/send-email")
    public ResponseEntity<?> sendEmailWithCode(@RequestBody @Valid ResetPasswordValidatorCodeRequest resetPasswordRequest) throws Exception {
        resetPasswordService.sendEmail(resetPasswordRequest.getEmail());

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "E-mail enviado com sucesso!");

        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    @PostMapping("/validator-code")
    public ResponseEntity<?> validatorCode(@RequestBody @Valid ResetPasswordValidatorCodeRequest resetPasswordRequest) throws Exception {
        resetPasswordService.validatorCode(resetPasswordRequest.getCodigo(), resetPasswordRequest.getEmail());

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "Código válido!");

        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    @PostMapping("/reset")
    public ResponseEntity<?> reset(@RequestBody @Valid ResetPasswordRequest resetPasswordRequest) throws Exception {
        resetPasswordService.restPassword(resetPasswordRequest.getEmail(), resetPasswordRequest.getNovaSenhaConfirmacao());

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "Senha alterada com sucesso!");

        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

}
