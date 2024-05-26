package br.com.tcc.validation.doutor.interfaces;

import br.com.tcc.validation.doutor.impl.ValidCpfDoutorValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = ValidCpfDoutorValidator.class)
@Target({ElementType.TYPE})
@Retention(RUNTIME)
public @interface ValidCpfDoutor {

    String message() default "{cpf.jaCadastrado}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
