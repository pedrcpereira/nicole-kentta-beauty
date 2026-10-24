package com.pedrcpereira.nkbeauty.auth;

import com.pedrcpereira.nkbeauty.user.Emails;
import com.pedrcpereira.nkbeauty.user.User;
import com.pedrcpereira.nkbeauty.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DatabaseUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(Emails.normalize(username))
                .orElseThrow(() -> new UsernameNotFoundException("Email not found."));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPasswordHash())
                .build();
    }
}
