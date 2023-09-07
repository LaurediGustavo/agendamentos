package br.com.tcc.validation.agendamento.interfaces;

import br.com.tcc.validation.agendamento.impl.ValidAgendamentoDataHoraFinalValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = ValidAgendamentoDataHoraFinalValidator.class)
@Target({ElementType.TYPE})
@Retention(RUNTIME)
public @interface ValidAgendamentoDataHoraFinal {

    String message() default "{agendamento.dataHoraFinal.menorQueDataInicial}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
