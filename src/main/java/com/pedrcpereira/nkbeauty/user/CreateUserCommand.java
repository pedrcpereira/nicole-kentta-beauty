package com.pedrcpereira.nkbeauty.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@PasswordNotContainingEmail
public record CreateUserCommand(
                @NotBlank @Email
                String email,
                @NotBlank @Size(min = 8, max = 72)
                String password,
                @NotBlank @Size(max = 100)
                String name) implements EmailAndPassword {

        public CreateUserCommand {
                email = Emails.normalize(email);
        }
}
