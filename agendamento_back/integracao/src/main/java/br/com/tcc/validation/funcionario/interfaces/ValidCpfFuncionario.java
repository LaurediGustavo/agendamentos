package br.com.tcc.validation.funcionario.interfaces;

import br.com.tcc.validation.funcionario.impl.ValidCpfFuncionarioValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = ValidCpfFuncionarioValidator.class)
@Target({ElementType.TYPE})
@Retention(RUNTIME)
public @interface ValidCpfFuncionario {

    String message() default "{cpf.jaCadastrado}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
