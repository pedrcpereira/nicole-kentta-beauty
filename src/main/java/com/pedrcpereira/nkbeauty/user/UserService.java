package com.pedrcpereira.nkbeauty.user;

import com.pedrcpereira.nkbeauty.common.exception.ConflictException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Locale;
import java.util.Map;

@Service
@Validated
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final String UNIQUE_CONSTRAINT_NAME = "uk_users_email";

    @Transactional
    public User create(@Valid CreateUserCommand createUserCommand) {

        if (userRepository.existsByEmail(createUserCommand.email())) {
            throw new ConflictException("Email already exists", Map.of("field", "email"));
        }

        String passwordHash = passwordEncoder.encode(createUserCommand.password());

        try {
            return userRepository.saveAndFlush(new User(createUserCommand.email(), passwordHash, createUserCommand.name()));
        } catch (DataIntegrityViolationException e) {
            if (e.getCause() instanceof ConstraintViolationException cve
                    && UNIQUE_CONSTRAINT_NAME.equals(cve.getConstraintName())) {
                throw new ConflictException("Email already exists", Map.of("field", "email"));
            }
            throw e;
        }
    }
}
