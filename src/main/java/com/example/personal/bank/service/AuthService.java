package com.example.personal.bank.service;

import com.example.personal.bank.entities.EmailData;
import com.example.personal.bank.entities.PhoneData;
import com.example.personal.bank.entities.User;
import com.example.personal.bank.repository.EmailRepository;
import com.example.personal.bank.repository.PhoneRepository;
import com.example.personal.bank.repository.UserRepository;
import com.example.personal.bank.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final EmailRepository emailRepository;
    private final PhoneRepository phoneRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public String authenticate(User user, String rawPassword) {
        logger.debug("Attempting authentication for user ID: {}", user != null ? user.getId() : "null");
        boolean isValid = user != null && passwordEncoder.matches(rawPassword, user.getPassword());
        if (!isValid) {
            logger.warn("Authentication failed: Invalid credentials for user ID: {}", user != null ? user.getId() : "null");
            throw new BadCredentialsException("Invalid credentials");
        }
        String token = jwtTokenProvider.generateToken(user.getId());
        logger.info("Authentication successful for user ID: {}. Generated JWT token.", user.getId());
        return token;
    }

    public String authenticateByEmail(String email, String rawPassword) {
        logger.debug("Attempting authentication by email: {}", email);
        EmailData emailData = emailRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.warn("Authentication failed: Email {} not found", email);
                    return new BadCredentialsException("Invalid email");
                });
        User user = userRepository.findById(emailData.getUser().getId())
                .orElseThrow(() -> {
                    logger.error("Authentication failed: User not found for email {}", email);
                    return new BadCredentialsException("User not found");
                });
        return authenticate(user, rawPassword);
    }

    public String authenticateByPhone(String phone, String rawPassword) {
        logger.debug("Attempting authentication by phone: {}", phone);
        PhoneData phoneData = phoneRepository.findByPhone(phone)
                .orElseThrow(() -> {
                    logger.warn("Authentication failed: Phone {} not found", phone);
                    return new BadCredentialsException("Invalid phone");
                });
        User user = userRepository.findById(phoneData.getUser().getId())
                .orElseThrow(() -> {
                    logger.error("Authentication failed: User not found for phone {}", phone);
                    return new BadCredentialsException("User not found");
                });
        return authenticate(user, rawPassword);
    }

    public static Long getAuthenticatedUserId() {
        logger.debug("Retrieving authenticated user ID from SecurityContext");
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof Long userId) {
            logger.debug("Successfully retrieved user ID: {}", userId);
            return userId;
        }
        logger.error("Invalid JWT token: Principal is not a valid JWT");
        throw new SecurityException("Invalid JWT token");
    }
}
