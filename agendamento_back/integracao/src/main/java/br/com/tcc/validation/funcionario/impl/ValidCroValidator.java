package br.com.tcc.validation.funcionario.impl;

import br.com.tcc.entity.TipoFuncionario;
import br.com.tcc.model.request.FuncionarioRequest;
import br.com.tcc.repository.TipoFuncionarioRepository;
import br.com.tcc.validation.funcionario.interfaces.ValidCro;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class ValidCroValidator implements ConstraintValidator<ValidCro, FuncionarioRequest> {

    private final String TIPO_FUNCIONARIO_DOUTOR = "DOUTOR";

    @Autowired
    private TipoFuncionarioRepository tipoFuncionarioRepository;
    @Override
    public boolean isValid(FuncionarioRequest value, ConstraintValidatorContext context) {
        Optional<TipoFuncionario> tipoFuncionarioOptional = tipoFuncionarioRepository.findById(value.getTipo_funcionario_id());

        if(tipoFuncionarioOptional.isPresent()) {
            TipoFuncionario tipoFuncionario = tipoFuncionarioOptional.get();

            if(TIPO_FUNCIONARIO_DOUTOR.equalsIgnoreCase(tipoFuncionario.getNome()) && StringUtils.isBlank(value.getCro())) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("{funcionario.cro.deveSerInformado}")
                        .addConstraintViolation();

                return false;
            }
            else {
                if(!TIPO_FUNCIONARIO_DOUTOR.equalsIgnoreCase(tipoFuncionario.getNome()) && StringUtils.isNotBlank(value.getCro())) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate("{funcionario.cro.naoDeveSerInformado}")
                            .addConstraintViolation();

                    return false;
                }
            }
        }

        return true;
    }

}
