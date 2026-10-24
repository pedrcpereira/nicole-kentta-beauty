package com.pedrcpereira.nkbeauty.auth;

import com.pedrcpereira.nkbeauty.user.User;
import com.pedrcpereira.nkbeauty.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DatabaseUserDetailsServiceTest {

    private static final String EMAIL = "nicole@example.com";
    private static final String HASH = "{bcrypt}$2a$10$abcdefghijklmnopqrstuuJ3s9zYvFQx1QpR7Cw3oWm8a0bE1fG2";

    @Mock
    private UserRepository userRepository;

    private DatabaseUserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        userDetailsService = new DatabaseUserDetailsService(userRepository);
    }

    @Test
    void loadsUserByEmail() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(new User(EMAIL, HASH, "Nicole")));

        UserDetails details = userDetailsService.loadUserByUsername(EMAIL);

        assertThat(details.getUsername()).isEqualTo(EMAIL);
        assertThat(details.getPassword()).isEqualTo(HASH);
        assertThat(details.getAuthorities()).isEmpty();
    }

    @Test
    void normalizesUsernameBeforeLookup() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(new User(EMAIL, HASH, "Nicole")));

        UserDetails details = userDetailsService.loadUserByUsername("  Nicole@Example.com ");

        assertThat(details.getUsername()).isEqualTo(EMAIL);
    }

    @Test
    void throwsWhenUserNotFound() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userDetailsService.loadUserByUsername(EMAIL))
                .isInstanceOf(UsernameNotFoundException.class);
    }
}
