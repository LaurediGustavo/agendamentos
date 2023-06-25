package br.com.tcc.validation.agendamento.impl;

import br.com.tcc.validation.agendamento.interfaces.ValidDataHoraInicio;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

public class ValidDataHoraInicioValidator implements ConstraintValidator<ValidDataHoraInicio, LocalDateTime> {

    private String message;

    @Override
    public void initialize(ValidDataHoraInicio constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        return false;
    }

}
