package com.example.personal.bank.repository;

import com.example.personal.bank.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.personal.bank.entities.EmailData;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmailRepository extends JpaRepository<EmailData, Long> {
    Optional<EmailData> findByEmail(String email);
    List<EmailData> findByUserId(Long userId);
}
