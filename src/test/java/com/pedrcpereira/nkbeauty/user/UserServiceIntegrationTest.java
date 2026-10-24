package com.pedrcpereira.nkbeauty.user;

import com.pedrcpereira.nkbeauty.TestcontainersConfiguration;
import com.pedrcpereira.nkbeauty.common.exception.ConflictException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@Transactional
public class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void passwordIsStoredAsAHash() {
        userService.create(new CreateUserCommand("nicole@example.com", "a-password", "Nicole"));

        assertThat(jdbcTemplate.queryForObject("select password_hash from users where email = ?", String.class, "nicole@example.com"))
                .startsWith("{bcrypt}")
                .isNotEqualTo("a-password");
    }

    @Test
    public void blankNameThrowsConstraintViolationException() {
        assertThatThrownBy(() -> userService.create(new CreateUserCommand("nicole@example.com", "a-password", "")))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    public void duplicateEmailInDifferentCaseThrowsConflictException() {
        userService.create(new CreateUserCommand("nicole@example.com", "a-password", "Nicole"));

        assertThatThrownBy(() -> userService.create(new CreateUserCommand("Nicole@example.COM", "a-password", "Duplicate")))
                .isInstanceOf(ConflictException.class);
    }
}
