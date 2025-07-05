package com.example.personal.bank.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.personal.bank.entities.User;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> findByNameStartingWith(String name, Pageable pageable);
    Page<User> findByDateOfBirthAfter(LocalDate dateOfBirth, Pageable pageable);
}
