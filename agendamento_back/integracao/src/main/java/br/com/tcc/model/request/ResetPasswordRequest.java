package br.com.tcc.model.request;

import br.com.tcc.validation.generic.interfaces.ValidObrigatorio;
import br.com.tcc.validation.restPassword.interfaces.ValidConfirmNewPassword;
import br.com.tcc.validation.restPassword.interfaces.ValidEmail;
import lombok.Data;

@Data
@ValidConfirmNewPassword
public class ResetPasswordRequest {

    @ValidObrigatorio
    @ValidEmail
    private String email;

    private String novaSenha;

    @ValidObrigatorio
    private String novaSenhaConfirmacao;

}
