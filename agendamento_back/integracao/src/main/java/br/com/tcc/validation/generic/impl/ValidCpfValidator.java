package br.com.tcc.validation.generic.impl;

import br.com.tcc.validation.generic.interfaces.ValidCpf;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import uteis.Uteis;

public class ValidCpfValidator implements ConstraintValidator<ValidCpf, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(StringUtils.isNotBlank(value) && !Uteis.cpfValido(value)) {
            return false;
        }

        return true;
    }

}
