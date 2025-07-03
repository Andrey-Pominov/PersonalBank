package com.example.personal.bank.service;

import com.example.personal.bank.dto.EmailDTO;
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
    public EmailDTO createEmail(Long userId, EmailDTO emailDTO) {
        logger.info("Creating email for user {}: {}", userId, emailDTO.getEmail());
        validateUserAccess(userId);
        if (emailRepository.findByEmail(emailDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already in use: " + emailDTO.getEmail());
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        EmailData emailData = new EmailData();
        emailData.setEmail(emailDTO.getEmail());
        emailData.setUser(user);
        EmailData saved = emailRepository.save(emailData);
        return mapToEmailDTO(saved);
    }

    @Transactional
    @CacheEvict(value = {"users", "userSearch"}, key = "#userId")
    public EmailDTO updateEmail(Long userId, Long emailId, EmailDTO emailDTO) {
        logger.info("Updating email {} for user {}", emailId, userId);
        validateUserAccess(userId);
        EmailData emailData = emailRepository.findById(emailId)
                .filter(e -> e.getUser().getId().equals(userId))
                .orElseThrow(() -> new IllegalArgumentException("Email not found or not owned by user"));
        if (emailRepository.findByEmail(emailDTO.getEmail())
                .filter(e -> !e.getId().equals(emailId))
                .isPresent()) {
            throw new IllegalArgumentException("Email already in use: " + emailDTO.getEmail());
        }
        emailData.setEmail(emailDTO.getEmail());
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
        Long authenticatedUserId = getAuthenticatedUserId(); // Assume this retrieves JWT user ID
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

    // Placeholder for JWT user ID retrieval
    private Long getAuthenticatedUserId() {
        // Implement JWT extraction, e.g., via SecurityContextHolder
        return 1L; // Replace with actual logic
    }
}
