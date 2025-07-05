package com.example.personal.bank.service;

import com.example.personal.bank.dto.user.UserDTO;
import com.example.personal.bank.entities.User;
import com.example.personal.bank.repository.EmailRepository;
import com.example.personal.bank.repository.PhoneRepository;
import com.example.personal.bank.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final EmailRepository emailRepository;
    private final PhoneRepository phoneRepository;

    @Cacheable(value = "users", key = "#id")
    public Optional<UserDTO> findById(Long id) {
        logger.info("Fetching user with id: {}", id);
        return userRepository.findById(id)
                .map(this::mapToUserDTO);
    }

    @Cacheable(value = "userSearch", key = "{#dateOfBirth, #name, #email, #phone, #pageable.pageNumber, #pageable.pageSize}")
    public Page<UserDTO> searchUsers(LocalDate dateOfBirth, String name, String email, String phone, Pageable pageable) {
        logger.info("Searching users with filters: dateOfBirth={}, name={}, email={}, phone={}", dateOfBirth, name, email, phone);

        if (email != null) {
            return emailRepository.findByEmail(email)
                    .map(emailData -> userRepository.findById(emailData.getUser().getId())
                            .map(user -> new PageImpl<>(List.of(mapToUserDTO(user)), pageable, 1))
                            .orElseGet(() -> new PageImpl<>(List.of(), pageable, 0)))
                    .orElse(new PageImpl<>(List.of(), pageable, 0));
        }


        if (phone != null) {
            return phoneRepository.findByPhone(phone)
                    .map(phoneData -> userRepository.findById(phoneData.getUser().getId())
                            .map(user -> new PageImpl<>(List.of(mapToUserDTO(user)), pageable, 1))
                            .orElseGet(() -> new PageImpl<>(List.of(), pageable, 0)))
                    .orElse(new PageImpl<>(List.of(), pageable, 0));
        }

        if (name != null) {
            return userRepository.findByNameStartingWith(name, pageable)
                    .map(this::mapToUserDTO);
        }

        if (dateOfBirth != null) {
            return userRepository.findByDateOfBirthAfter(dateOfBirth, pageable)
                    .map(this::mapToUserDTO);
        }

        return userRepository.findAll(pageable)
                .map(this::mapToUserDTO);
    }

    private UserDTO mapToUserDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setDateOfBirth(user.getDateOfBirth());
        return dto;
    }
}
