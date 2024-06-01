package br.com.tcc.validation.restPassword.impl;

import br.com.tcc.repository.FuncionarioRepository;
import br.com.tcc.security.securityConfig.UserDetailsServiceImpl;
import br.com.tcc.validation.restPassword.interfaces.ValidEmail;
import br.com.tcc.validation.usuario.interfaces.ValidSenhaAntiga;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

public class ValidEmailValidator implements ConstraintValidator<ValidEmail, String>  {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return funcionarioRepository.existsByEmail(value);
    }

}
