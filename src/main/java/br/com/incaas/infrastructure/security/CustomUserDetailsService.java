package br.com.incaas.infrastructure.security;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final PasswordEncoder encoder;

    private final Map<String, String> passwords = new HashMap<>();

    @PostConstruct
    void init() {
        passwords.put("admin", encoder.encode("admin123"));
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        String encoded = passwords.get(username);
        if (encoded == null) {
            throw new UsernameNotFoundException("Usuário não encontrado: " + username);
        }

        return User.builder()
                .username(username)
                .password(encoded)
                .roles("USER")
                .build();
    }
}
