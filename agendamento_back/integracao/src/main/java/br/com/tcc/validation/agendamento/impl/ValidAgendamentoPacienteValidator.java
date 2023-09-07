package br.com.tcc.validation.agendamento.impl;

import br.com.tcc.repository.PacienteRepository;
import br.com.tcc.validation.agendamento.interfaces.ValidAgendamentoPaciente;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class ValidAgendamentoPacienteValidator implements ConstraintValidator<ValidAgendamentoPaciente, Long> {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        if(pacienteRepository.findById(value).isEmpty()) {
            return false;
        }

        return true;
    }
}
