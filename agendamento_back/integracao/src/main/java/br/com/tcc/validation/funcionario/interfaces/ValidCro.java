package br.com.tcc.validation.funcionario.interfaces;

import br.com.tcc.validation.funcionario.impl.ValidCroValidator;
import br.com.tcc.validation.funcionario.impl.ValidTipoFuncionarioIdValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = ValidCroValidator.class)
@Target({ElementType.TYPE})
@Retention(RUNTIME)
public @interface ValidCro {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
