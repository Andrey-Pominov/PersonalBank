package com.example.personal.bank.service;

import com.example.personal.bank.entities.EmailData;
import com.example.personal.bank.entities.PhoneData;
import com.example.personal.bank.entities.User;
import com.example.personal.bank.repository.EmailRepository;
import com.example.personal.bank.repository.PhoneRepository;
import com.example.personal.bank.repository.UserRepository;
import com.example.personal.bank.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final EmailRepository emailRepository;
    private final PhoneRepository phoneRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public String authenticate(User user, String rawPassword) {
        boolean isValid = user != null && passwordEncoder.matches(rawPassword, user.getPassword());
        if (!isValid) throw new BadCredentialsException("Invalid credentials");

        return jwtTokenProvider.generateToken(user.getId());
    }

    public String authenticateByEmail(String email, String rawPassword) {
        EmailData emailData = emailRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Invalid email"));
        User user = userRepository.findById(emailData.getUser().getId())
                .orElseThrow(() -> new BadCredentialsException("User not found"));
        return authenticate(user, rawPassword);
    }

    public String authenticateByPhone(String phone, String rawPassword) {
        PhoneData phoneData = phoneRepository.findByPhone(phone)
                .orElseThrow(() -> new BadCredentialsException("Invalid phone"));
        User user = userRepository.findById(phoneData.getUser().getId())
                .orElseThrow(() -> new BadCredentialsException("User not found"));
        return authenticate(user, rawPassword);
    }


    public static Long getAuthenticatedUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof Jwt jwt) {
            Object claim = jwt.getClaim("userId");
            if (claim != null) {
                try {
                    return Long.parseLong(claim.toString());
                } catch (NumberFormatException e) {
                    throw new SecurityException("Invalid userId in token", e);
                }
            }
        }
        throw new SecurityException("Invalid JWT token");
    }

}
