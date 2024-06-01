package br.com.tcc.validation.restPassword.interfaces;

import br.com.tcc.validation.restPassword.impl.ValidEmailValidator;
import br.com.tcc.validation.usuario.impl.ValidSenhaAntigaValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = ValidEmailValidator.class)
@Target({FIELD, ANNOTATION_TYPE, METHOD})
@Retention(RUNTIME)
public @interface ValidEmail {

    String message() default "{send.email.funcionario.nao.encontrato}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
