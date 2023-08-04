package com.digiteo.neovoteIV.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UsernameArrayLengthValidator.class)
public @interface UsernameMinLength {
    String message() default "El nombre de usuario debe tener como mínimo {value} caracteres.";
    int value() default 3;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
