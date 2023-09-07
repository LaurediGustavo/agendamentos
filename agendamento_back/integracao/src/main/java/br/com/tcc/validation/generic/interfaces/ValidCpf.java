package br.com.tcc.validation.generic.interfaces;

import br.com.tcc.validation.generic.impl.ValidCpfValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = ValidCpfValidator.class)
@Target({FIELD, ANNOTATION_TYPE, METHOD})
@Retention(RUNTIME)
public @interface ValidCpf {

    String message() default "{cpf}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
