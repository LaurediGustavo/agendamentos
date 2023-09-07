package br.com.tcc.validation.funcionario.interfaces;

import br.com.tcc.validation.funcionario.impl.ValidTipoFuncionarioIdValidator;
import br.com.tcc.validation.paciente.impl.ValidPacienteIdValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = ValidTipoFuncionarioIdValidator.class)
@Target({FIELD, ANNOTATION_TYPE, METHOD})
@Retention(RUNTIME)
public @interface ValidTipoFuncionarioId {

    String message() default "{funcionario.tipoFuncionario.naoEncontrado}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
