package com.pedrcpereira.nkbeauty.user;

import com.pedrcpereira.nkbeauty.common.exception.ConflictException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static String normalizeEmail(String raw) {
        return raw.strip().toLowerCase(Locale.ROOT);
    }

    @Transactional
    public User create(CreateUserCommand createUserCommand) {
        String email = normalizeEmail(createUserCommand.email());

        if (userRepository.existsByEmail(email)) {
            throw new ConflictException("Email already exists", Map.of("field", "email"));
        }

        String password = passwordEncoder.encode(createUserCommand.password());

        User user = new User(email, password, createUserCommand.name());
        userRepository.save(user);

        return user;
    }
}
