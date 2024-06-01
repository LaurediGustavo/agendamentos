package br.com.tcc.validation.restPassword.interfaces;

import br.com.tcc.validation.restPassword.impl.ValidConfirmNewPasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = ValidConfirmNewPasswordValidator.class)
@Target({ElementType.TYPE})
@Retention(RUNTIME)
public @interface ValidConfirmNewPassword {

    String message() default "{usuario.confirmNewPassword.senhasDiferentes}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
