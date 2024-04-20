package br.com.tcc.validation.agendamento.interfaces;

import br.com.tcc.validation.agendamento.impl.ValidAgendamentoDoutorValidator;
import br.com.tcc.validation.agendamento.impl.ValidAgendamentoEstendidoValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = ValidAgendamentoEstendidoValidator.class)
@Target({FIELD, ANNOTATION_TYPE, METHOD})
@Retention(RUNTIME)
public @interface ValidAgendamentoEstendido {

    String message() default "{agendamento.consultaNaoEncontrato}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
