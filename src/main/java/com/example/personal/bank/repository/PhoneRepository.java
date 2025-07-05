package com.example.personal.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.personal.bank.entities.PhoneData;

import java.util.List;
import java.util.Optional;

public interface PhoneRepository extends JpaRepository<PhoneData, Long> {
    Optional<PhoneData> findByPhone(String phone);
    List<PhoneData> findByUserId(Long userId);
}
