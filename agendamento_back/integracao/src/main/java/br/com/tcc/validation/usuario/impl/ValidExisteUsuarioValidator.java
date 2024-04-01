package br.com.tcc.validation.usuario.impl;

import br.com.tcc.repository.UserRepository;
import br.com.tcc.validation.usuario.interfaces.ValidExisteUsuario;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class ValidExisteUsuarioValidator implements ConstraintValidator<ValidExisteUsuario, Long>  {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        return userRepository.existsById(value);
    }

}
