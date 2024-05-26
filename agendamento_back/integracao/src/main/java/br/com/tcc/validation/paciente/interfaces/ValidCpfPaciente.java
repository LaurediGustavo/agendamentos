package br.com.tcc.validation.paciente.interfaces;

import br.com.tcc.validation.paciente.impl.ValidCpfPacienteValidator;
import br.com.tcc.validation.paciente.impl.ValidResponsavelValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = ValidCpfPacienteValidator.class)
@Target({ElementType.TYPE})
@Retention(RUNTIME)
public @interface ValidCpfPaciente {

    String message() default "{cpf.jaCadastrado}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
