package br.com.tcc.validation.restPassword.impl;

import br.com.tcc.model.request.ResetPasswordRequest;
import br.com.tcc.validation.restPassword.interfaces.ValidConfirmNewPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

public class ValidConfirmNewPasswordValidator implements ConstraintValidator<ValidConfirmNewPassword, ResetPasswordRequest> {
    @Override
    public boolean isValid(ResetPasswordRequest value, ConstraintValidatorContext context) {
        if (StringUtils.isNotBlank(value.getNovaSenha()) && StringUtils.isNotBlank(value.getNovaSenhaConfirmacao())) {
            return value.getNovaSenha().equals(value.getNovaSenhaConfirmacao());
        }

        return false;
    }
}
