package br.com.tcc.validation.funcionario.impl;

import br.com.tcc.entity.Funcionario;
import br.com.tcc.entity.Paciente;
import br.com.tcc.repository.FuncionarioRepository;
import br.com.tcc.repository.PacienteRepository;
import br.com.tcc.validation.funcionario.interfaces.ValidFuncionarioId;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class ValidFuncionarioIdValidator implements ConstraintValidator<ValidFuncionarioId, Long> {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        if(value != null && value != 0) {
            Optional<Funcionario> funcionario = funcionarioRepository.findById(value);

            if(funcionario.isPresent()) {
                return true;
            }
            else {
                return false;
            }
        }

        return true;
    }

}
