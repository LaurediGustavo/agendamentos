package br.com.tcc.validation.generic.impl;

import br.com.tcc.validation.generic.interfaces.ValidObrigatorio;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidObrigatorioValidator implements ConstraintValidator<ValidObrigatorio, Object> {

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        return value != null;
    }

}
