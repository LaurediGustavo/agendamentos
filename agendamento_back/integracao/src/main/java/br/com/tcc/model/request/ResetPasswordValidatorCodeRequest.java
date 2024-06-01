package br.com.tcc.model.request;

import br.com.tcc.validation.generic.interfaces.ValidObrigatorio;
import br.com.tcc.validation.restPassword.interfaces.ValidEmail;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ResetPasswordValidatorCodeRequest {

    @ValidObrigatorio
    @ValidEmail
    private String email;

    @JsonProperty(required = false)
    private String codigo;

}
