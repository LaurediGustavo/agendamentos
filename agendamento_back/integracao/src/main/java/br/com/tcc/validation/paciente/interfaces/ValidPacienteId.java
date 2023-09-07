package br.com.tcc.validation.paciente.interfaces;

import br.com.tcc.validation.paciente.impl.ValidPacienteIdValidator;
import br.com.tcc.validation.paciente.impl.ValidResponsavelValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = ValidPacienteIdValidator.class)
@Target({FIELD, ANNOTATION_TYPE, METHOD})
@Retention(RUNTIME)
public @interface ValidPacienteId {

    String message() default "{paciente.naoEncontrado}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
