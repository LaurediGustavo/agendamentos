package br.com.tcc.model.request;

import br.com.tcc.validation.generic.interfaces.ValidObrigatorio;
import br.com.tcc.validation.usuario.interfaces.ValidConfirmNewPassword;
import br.com.tcc.validation.usuario.interfaces.ValidSenhaAntiga;
import lombok.Data;

@Data
@ValidConfirmNewPassword
public class AlterarSenhaRequest {

    @ValidObrigatorio
    @ValidSenhaAntiga
    private String currentPassword;

    @ValidObrigatorio
    private String newPassword;

    @ValidObrigatorio
    private String confirmNewPassword;

}
