package br.com.tcc.validation.generic.impl;

import br.com.tcc.validation.generic.interfaces.ValidObrigatorio;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

public class ValidObrigatorioValidator implements ConstraintValidator<ValidObrigatorio, Object> {

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if(value instanceof String) {
            return StringUtils.isNotBlank((String) value);
        }
        else {
            return value != null;
        }
    }

}
