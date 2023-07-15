package br.com.tcc.validation.agendamento.interfaces;

import br.com.tcc.validation.agendamento.impl.ValidAgendamentoPacienteValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = ValidAgendamentoPacienteValidator.class)
@Target({FIELD, ANNOTATION_TYPE, METHOD})
@Retention(RUNTIME)
public @interface ValidAgendamentoPaciente {

    String message() default "{agendamento.paciente.naoCadastrado}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
