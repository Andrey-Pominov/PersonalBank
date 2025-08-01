package com.example.personal.bank.service;

import com.example.personal.bank.dto.email.EmailDTO;
import com.example.personal.bank.entities.EmailData;
import com.example.personal.bank.entities.User;
import com.example.personal.bank.repository.EmailRepository;
import com.example.personal.bank.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.personal.bank.service.AuthService.getAuthenticatedUserId;

@Service
@RequiredArgsConstructor
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private final EmailRepository emailRepository;
    private final UserRepository userRepository;

    public List<EmailDTO> findByUserId(Long userId) {
        logger.info("Fetching emails for user: {}", userId);
        return emailRepository.findByUserId(userId)
                .stream()
                .map(this::mapToEmailDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    @CacheEvict(value = {"users", "userSearch"}, key = "#userId")
    public EmailDTO createEmail(Long userId, String email) {
        logger.info("Creating email for user {}: {}", userId, email);
        validateUserAccess(userId);
        if (emailRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already in use: " + email);
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        EmailData emailData = new EmailData();
        emailData.setEmail(email);
        emailData.setUser(user);
        EmailData saved = emailRepository.save(emailData);
        return mapToEmailDTO(saved);
    }

    @Transactional
    @CacheEvict(value = {"users", "userSearch"}, key = "#userId")
    public EmailDTO updateEmail(Long userId, Long emailId, String email) {
        logger.info("Updating email {} for user {}", emailId, userId);
        validateUserAccess(userId);
        EmailData emailData = emailRepository.findById(emailId)
                .filter(e -> e.getUser().getId().equals(userId))
                .orElseThrow(() -> new IllegalArgumentException("Email not found or not owned by user"));
        if (emailRepository.findByEmail(email)
                .filter(e -> !e.getId().equals(emailId))
                .isPresent()) {
            throw new IllegalArgumentException("Email already in use: " + email);
        }
        emailData.setEmail(email);
        EmailData saved = emailRepository.save(emailData);
        return mapToEmailDTO(saved);
    }

    @Transactional
    @CacheEvict(value = {"users", "userSearch"}, key = "#userId")
    public void deleteEmail(Long userId, Long emailId) {
        logger.info("Deleting email {} for user {}", emailId, userId);
        validateUserAccess(userId);
        EmailData emailData = emailRepository.findById(emailId)
                .filter(e -> e.getUser().getId().equals(userId))
                .orElseThrow(() -> new IllegalArgumentException("Email not found or not owned by user"));
        long emailCount = emailRepository.findByUserId(userId).size();
        if (emailCount <= 1) {
            throw new IllegalStateException("Cannot delete the last email for user");
        }
        emailRepository.delete(emailData);
    }

    private void validateUserAccess(Long userId) {
        Long authenticatedUserId = getAuthenticatedUserId();
        if (!authenticatedUserId.equals(userId)) {
            throw new SecurityException("Unauthorized access to user data");
        }
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found: " + userId);
        }
    }

    private EmailDTO mapToEmailDTO(EmailData emailData) {
        EmailDTO dto = new EmailDTO();
        dto.setId(emailData.getId());
        dto.setEmail(emailData.getEmail());
        return dto;
    }

}
