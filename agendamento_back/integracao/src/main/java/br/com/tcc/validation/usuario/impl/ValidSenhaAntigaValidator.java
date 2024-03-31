package br.com.tcc.validation.usuario.impl;

import br.com.tcc.security.securityConfig.UserDetailsServiceImpl;
import br.com.tcc.validation.usuario.interfaces.ValidSenhaAntiga;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

public class ValidSenhaAntigaValidator implements ConstraintValidator<ValidSenhaAntiga, String>  {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isNotBlank(value)) {
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            UserDetails userDetails = userDetailsService.loadUserByUsername(userName);

            return passwordEncoder.matches(value, userDetails.getPassword());
        }

        return false;
    }

}
