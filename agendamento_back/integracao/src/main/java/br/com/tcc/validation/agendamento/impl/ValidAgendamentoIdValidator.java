package br.com.tcc.validation.agendamento.impl;

import br.com.tcc.repository.ConsultaRepository;
import br.com.tcc.validation.agendamento.interfaces.ValidAgendamentoId;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class ValidAgendamentoIdValidator implements ConstraintValidator<ValidAgendamentoId, Long> {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {

        if(value == null || value == 0 || consultaRepository.findById(value).isPresent()) {
            return true;
        }

        return false;
    }

}
