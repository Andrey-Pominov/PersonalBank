

package com.example.personal.bank.service;

import com.example.personal.bank.dto.phone.PhoneDTO;
import com.example.personal.bank.entities.PhoneData;
import com.example.personal.bank.entities.User;
import com.example.personal.bank.repository.PhoneRepository;
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
public class PhoneService {
    private static final Logger logger = LoggerFactory.getLogger(PhoneService.class);
    private final PhoneRepository phoneRepository;
    private final UserRepository userRepository;

    public List<PhoneDTO> findByUserId(Long userId) {
        logger.info("Fetching phones for user: {}", userId);
        return phoneRepository.findByUserId(userId)
                .stream()
                .map(this::mapToPhoneDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    @CacheEvict(value = {"users", "userSearch"}, key = "#userId")
    public PhoneDTO createPhone(Long userId, String phone) {
        logger.info("Creating phone for user {}: {}", userId, phone);
        validateUserAccess(userId);
        if (phoneRepository.findByPhone(phone).isPresent()) {
            throw new IllegalArgumentException("Phone already in use: " + phone);
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        PhoneData phoneData = new PhoneData();
        phoneData.setPhone(phone);
        phoneData.setUser(user);
        PhoneData saved = phoneRepository.save(phoneData);
        return mapToPhoneDTO(saved);
    }

    @Transactional
    @CacheEvict(value = {"users", "userSearch"}, key = "#userId")
    public PhoneDTO updatePhone(Long userId, Long phoneId, String phone) {
        logger.info("Updating phone {} for user {}", phoneId, userId);
        validateUserAccess(userId);
        PhoneData phoneData = phoneRepository.findById(phoneId)
                .filter(p -> p.getUser().getId().equals(userId))
                .orElseThrow(() -> new IllegalArgumentException("Phone not found or not owned by user"));
        if (phoneRepository.findByPhone(phone)
                .filter(p -> !p.getId().equals(phoneId))
                .isPresent()) {
            throw new IllegalArgumentException("Phone already in use: " + phone);
        }
        phoneData.setPhone(phone);
        PhoneData saved = phoneRepository.save(phoneData);
        return mapToPhoneDTO(saved);
    }

    @Transactional
    @CacheEvict(value = {"users", "userSearch"}, key = "#userId")
    public void deletePhone(Long userId, Long phoneId) {
        logger.info("Deleting phone {} for user {}", phoneId, userId);
        validateUserAccess(userId);
        PhoneData phoneData = phoneRepository.findById(phoneId)
                .filter(p -> p.getUser().getId().equals(userId))
                .orElseThrow(() -> new IllegalArgumentException("Phone not found or not owned by user"));
        long phoneCount = phoneRepository.findByUserId(userId).size();
        if (phoneCount <= 1) {
            throw new IllegalStateException("Cannot delete the last phone for user");
        }
        phoneRepository.delete(phoneData);
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

    private PhoneDTO mapToPhoneDTO(PhoneData phoneData) {
        PhoneDTO dto = new PhoneDTO();
        dto.setId(phoneData.getId());
        dto.setPhone(phoneData.getPhone());
        return dto;
    }

}
