package br.com.tcc.validation.usuario.impl;

import br.com.tcc.model.request.AlterarSenhaRequest;
import br.com.tcc.validation.usuario.interfaces.ValidConfirmNewPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

public class ValidConfirmNewPasswordValidator implements ConstraintValidator<ValidConfirmNewPassword, AlterarSenhaRequest> {
    @Override
    public boolean isValid(AlterarSenhaRequest value, ConstraintValidatorContext context) {
        if (StringUtils.isNotBlank(value.getNewPassword()) && StringUtils.isNotBlank(value.getConfirmNewPassword())) {
            return value.getNewPassword().equals(value.getConfirmNewPassword());
        }

        return false;
    }
}
