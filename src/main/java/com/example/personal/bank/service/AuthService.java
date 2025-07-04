package com.example.personal.bank.service;

import com.example.personal.bank.configuration.JwtTokenProvider;
import com.example.personal.bank.entities.User;
import com.example.personal.bank.repository.UserRepository;
import org.springframework.security.oauth2.jwt.Jwt;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public String authenticate(String identifier, String rawPassword, Function<String, Optional<User>> finder) {
        User user = finder.apply(identifier).orElse(null);

        boolean isValid = user != null && passwordEncoder.matches(rawPassword, user.getPassword());
        if (!isValid) throw new BadCredentialsException("Invalid credentials");

        return jwtTokenProvider.generateToken(user.getId());
    }

    public String authenticateByEmail(String email, String rawPassword) {
        return authenticate(email, rawPassword, userRepository::findByEmail);
    }

    public String authenticateByPhone(String phone, String rawPassword) {
        return authenticate(phone, rawPassword, userRepository::findByPhone);
    }

    public static Long getAuthenticatedUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof Jwt jwt) {
            return Long.valueOf(jwt.getClaim("userId").toString());
        }
        throw new SecurityException("Invalid JWT token");
    }
}
