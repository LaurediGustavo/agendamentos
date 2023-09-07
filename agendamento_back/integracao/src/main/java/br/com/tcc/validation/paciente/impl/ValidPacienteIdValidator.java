package br.com.tcc.validation.paciente.impl;

import br.com.tcc.entity.Paciente;
import br.com.tcc.repository.PacienteRepository;
import br.com.tcc.validation.paciente.interfaces.ValidPacienteId;
import br.com.tcc.validation.paciente.interfaces.ValidResponsavel;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class ValidPacienteIdValidator implements ConstraintValidator<ValidPacienteId, Long> {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        if(value != null && value != 0) {
            Optional<Paciente> paciente = pacienteRepository.findById(value);

            if(paciente.isPresent()) {
                return true;
            }
            else {
                return false;
            }
        }

        return true;
    }

}
