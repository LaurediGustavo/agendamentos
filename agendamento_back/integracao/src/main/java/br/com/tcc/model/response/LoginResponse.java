package br.com.tcc.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LoginResponse {

    private Long usuarioId;

    private String tokenJwt;

    private List<String> roles;

    @JsonProperty(required = false)
    private String message;

}
