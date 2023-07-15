package br.com.tcc.validation.agendamento.impl;

import br.com.tcc.repository.PacienteRepository;
import br.com.tcc.validation.agendamento.interfaces.ValidAgendamentoDataHoraInicio;
import br.com.tcc.validation.agendamento.interfaces.ValidAgendamentoPaciente;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

public class ValidAgendamentoPacienteValidator implements ConstraintValidator<ValidAgendamentoPaciente, Long> {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Override
    public void initialize(ValidAgendamentoPaciente constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        if(pacienteRepository.findById(value).isEmpty()) {
            return false;
        }

        return true;
    }
}
