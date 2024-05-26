package br.com.tcc.validation.paciente.impl;

import br.com.tcc.impl.PacienteService;
import br.com.tcc.model.request.PacienteRequest;
import br.com.tcc.validation.paciente.interfaces.ValidCpfPaciente;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import uteis.Uteis;

public class ValidCpfPacienteValidator  implements ConstraintValidator<ValidCpfPaciente, PacienteRequest> {

    @Autowired
    private PacienteService pacienteService;

    @Override
    public boolean isValid(PacienteRequest value, ConstraintValidatorContext context) {
        if (StringUtils.isNotBlank(value.getCpf()) && (value.getId() == null || value.getId() == 0L)) {
            return !pacienteService.existsByCpf(Uteis.removerCaracteresNaoNumericos(value.getCpf()));
        }

        return true;
    }

}
