package br.com.tcc.validation.usuario.interfaces;

import br.com.tcc.validation.usuario.impl.ValidExisteUsuarioValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = ValidExisteUsuarioValidator.class)
@Target({FIELD, ANNOTATION_TYPE, METHOD})
@Retention(RUNTIME)
public @interface ValidExisteUsuario {

    String message() default "{usuario.usuarioNaoCadastrado}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
