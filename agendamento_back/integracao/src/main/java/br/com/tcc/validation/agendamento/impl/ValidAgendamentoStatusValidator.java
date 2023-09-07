package br.com.tcc.validation.agendamento.impl;

import br.com.tcc.enumerador.StatusConsultaEnum;
import br.com.tcc.validation.agendamento.interfaces.ValidAgendamentoStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

public class ValidAgendamentoStatusValidator implements ConstraintValidator<ValidAgendamentoStatus, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            if(StringUtils.isNotBlank(value)) {
                StatusConsultaEnum status = StatusConsultaEnum.valueOf(value);
            }
            return true;
        }
        catch (Exception ex) {
            return false;
        }
    }

}
