package com.pedrcpereira.nkbeauty.user;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Locale;

public class PasswordNotContainingEmailValidator
        implements ConstraintValidator<PasswordNotContainingEmail, EmailAndPassword> {

    @Override
    public boolean isValid(EmailAndPassword value, ConstraintValidatorContext context) {
        if (value == null || value.email() == null || value.password() == null) {
            return true;
        }

        int at = value.email().indexOf('@');
        if (at <= 0) {
            return true;
        }

        String localPart = value.email().substring(0, at).toLowerCase(Locale.ROOT);
        String password = value.password().toLowerCase(Locale.ROOT);

        if (!password.contains(localPart)) {
            return true;
        }

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                .addPropertyNode("password")
                .addConstraintViolation();
        return false;
    }
}
