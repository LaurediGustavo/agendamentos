package br.com.tcc.validation.agendamento.impl;

import br.com.tcc.repository.ProcedimentoRepository;
import br.com.tcc.validation.agendamento.interfaces.ValidAgendamentoProcedimento;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import uteis.Uteis;

import java.util.List;

public class ValidAgendamentoProcedimentoValidator implements ConstraintValidator<ValidAgendamentoProcedimento, List<Long>> {

    @Autowired
    private ProcedimentoRepository procedimentoRepository;

    private String message = "";

    @Override
    public boolean isValid(List<Long> value, ConstraintValidatorContext context) {
        if(Uteis.isNotNullOrNotEmpty(value)) {
            boolean allIdsValid = true;
            int count = 0;
            for (Long id : value) {
                if (procedimentoRepository.findById(id).isEmpty()) {
                    allIdsValid = false;
                    addInvalidIdMessage(context, count);
                }

                count++;
            }

            if(!allIdsValid) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(message)
                        .addConstraintViolation();
            }

            return allIdsValid;
        }

        return false;
    }

    private void addInvalidIdMessage(ConstraintValidatorContext context, int count) {
        message += "Procedimento de índice [" + count + "] não cadastrado. ";
    }

}
