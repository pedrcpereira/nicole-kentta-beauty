package com.pedrcpereira.nkbeauty.user;

import com.pedrcpereira.nkbeauty.common.exception.ConflictException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;
    private PasswordEncoder encoder;

    @BeforeEach
    public void setUp() {
        encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        userService = new UserService(userRepository, encoder);
    }

    @Test
    void createNormalizesEmail() {
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User user = userService.create(new CreateUserCommand(" Nicole@Example.COM  ", "password", "Nicole"));

        assertThat(user.getEmail()).isEqualTo("nicole@example.com");
    }

    @Test
    void createHashesPassword() {
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User user = userService.create(new CreateUserCommand("nicole@example.com", "password", "Nicole"));

        assertThat(user.getPasswordHash()).isNotEqualTo("password").startsWith("{bcrypt}");
        assertThat(encoder.matches("password", user.getPasswordHash())).isTrue();
    }

    @Test
    void createRejectsEmailTakenInDifferentCase() {
        when(userRepository.existsByEmail("nicole@example.com")).thenReturn(true);

        assertThatThrownBy(() -> userService.create(
                new CreateUserCommand("Nicole@Example.com", "password", "Nicole")))
                .isInstanceOf(ConflictException.class);

        verify(userRepository, never()).save(any());
    }
}
