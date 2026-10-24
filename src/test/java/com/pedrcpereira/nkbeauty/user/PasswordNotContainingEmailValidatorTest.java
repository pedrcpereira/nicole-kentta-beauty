package com.pedrcpereira.nkbeauty.user;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class PasswordNotContainingEmailValidatorTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void rejectsPasswordContainingEmailLocalPart() {
        var command = new CreateUserCommand("nicole@example.com", "my-nicole-2026", "Nicole");

        Set<ConstraintViolation<CreateUserCommand>> violations = validator.validate(command);

        assertThat(violations)
                .singleElement()
                .satisfies(v -> assertThat(v.getPropertyPath()).hasToString("password"));
    }

    @Test
    void rejectsPasswordContainingEmailLocalPartInDifferentCase() {
        var command = new CreateUserCommand("Nicole@Example.com", "My-NICOLE-2026", "Nicole");

        Set<ConstraintViolation<CreateUserCommand>> violations = validator.validate(command);

        assertThat(violations)
                .singleElement()
                .satisfies(v -> assertThat(v.getPropertyPath()).hasToString("password"));
    }

    @Test
    void acceptsPasswordNotContainingEmail() {
        var command = new CreateUserCommand("nicole@example.com", "a-long-password", "Nicole");

        assertThat(validator.validate(command)).isEmpty();
    }

    @Test
    void ignoresEmailWithoutAtSign() {
        var command = new CreateUserCommand("nicole", "my-nicole-2026", "Nicole");

        assertThat(new PasswordNotContainingEmailValidator().isValid(command, null)).isTrue();
    }
}
