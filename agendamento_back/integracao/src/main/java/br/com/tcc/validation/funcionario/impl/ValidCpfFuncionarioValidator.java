package br.com.tcc.validation.funcionario.impl;

import br.com.tcc.impl.FuncionarioService;
import br.com.tcc.impl.PacienteService;
import br.com.tcc.model.request.FuncionarioRequest;
import br.com.tcc.validation.funcionario.interfaces.ValidCpfFuncionario;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import uteis.Uteis;

public class ValidCpfFuncionarioValidator implements ConstraintValidator<ValidCpfFuncionario, FuncionarioRequest> {

    @Autowired
    private FuncionarioService funcionarioService;

    @Override
    public boolean isValid(FuncionarioRequest value, ConstraintValidatorContext context) {
        if (StringUtils.isNotBlank(value.getCpf()) && (value.getId() == null || value.getId() == 0L)) {
            return !funcionarioService.existsByCpf(Uteis.removerCaracteresNaoNumericos(value.getCpf()));
        }

        return true;
    }

}
