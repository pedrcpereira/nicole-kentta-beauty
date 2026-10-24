package com.pedrcpereira.nkbeauty.user;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordNotContainingEmailValidator.class)
public @interface PasswordNotContainingEmail {

    String message() default "must not contain the email address";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
