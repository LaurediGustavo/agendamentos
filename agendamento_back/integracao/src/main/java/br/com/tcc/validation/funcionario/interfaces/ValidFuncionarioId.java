package br.com.tcc.validation.funcionario.interfaces;

import br.com.tcc.validation.funcionario.impl.ValidFuncionarioIdValidator;
import br.com.tcc.validation.paciente.impl.ValidPacienteIdValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = ValidFuncionarioIdValidator.class)
@Target({FIELD, ANNOTATION_TYPE, METHOD})
@Retention(RUNTIME)
public @interface ValidFuncionarioId {

    String message() default "{funcionario.naoEncontrato}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
