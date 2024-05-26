package br.com.tcc.validation.doutor.impl;

import br.com.tcc.impl.FuncionarioService;
import br.com.tcc.model.request.DoutorRequest;
import br.com.tcc.validation.doutor.interfaces.ValidCpfDoutor;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import uteis.Uteis;

public class ValidCpfDoutorValidator implements ConstraintValidator<ValidCpfDoutor, DoutorRequest> {

    @Autowired
    private FuncionarioService funcionarioService;

    @Override
    public boolean isValid(DoutorRequest value, ConstraintValidatorContext context) {
        if (StringUtils.isNotBlank(value.getFuncionario().getCpf()) && (value.getId() == null || value.getId() == 0L)) {
            return !funcionarioService.existsByCpf(Uteis.removerCaracteresNaoNumericos(value.getFuncionario().getCpf()));
        }

        return true;
    }

}
