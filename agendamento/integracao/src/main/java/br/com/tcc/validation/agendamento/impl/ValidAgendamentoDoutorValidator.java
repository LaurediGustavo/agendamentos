package br.com.tcc.validation.agendamento.impl;

import br.com.tcc.repository.DoutorRepository;
import br.com.tcc.validation.agendamento.interfaces.ValidAgendamentoDoutor;
import br.com.tcc.validation.agendamento.interfaces.ValidAgendamentoPaciente;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class ValidAgendamentoDoutorValidator implements ConstraintValidator<ValidAgendamentoDoutor, Long> {

    @Autowired
    private DoutorRepository doutorRepository;

    @Override
    public void initialize(ValidAgendamentoDoutor constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        if(doutorRepository.findById(value).isEmpty()) {
            return false;
        }

        return true;
    }
}
