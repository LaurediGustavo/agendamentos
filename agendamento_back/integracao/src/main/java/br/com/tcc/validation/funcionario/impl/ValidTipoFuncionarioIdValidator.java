package br.com.tcc.validation.funcionario.impl;

import br.com.tcc.entity.Paciente;
import br.com.tcc.entity.TipoFuncionario;
import br.com.tcc.repository.PacienteRepository;
import br.com.tcc.repository.TipoFuncionarioRepository;
import br.com.tcc.validation.funcionario.interfaces.ValidTipoFuncionarioId;
import br.com.tcc.validation.paciente.interfaces.ValidPacienteId;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class ValidTipoFuncionarioIdValidator implements ConstraintValidator<ValidTipoFuncionarioId, Long> {

    @Autowired
    private TipoFuncionarioRepository tipoFuncionarioRepository;
    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        if(value != null && value != 0) {
            Optional<TipoFuncionario> tipoFuncionario = tipoFuncionarioRepository.findById(value);

            if(tipoFuncionario.isPresent()) {
                return true;
            }
            else {
                return false;
            }
        }

        return true;
    }

}
