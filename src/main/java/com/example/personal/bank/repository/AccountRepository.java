package com.example.personal.bank.repository;

import com.example.personal.bank.entities.Account;
import com.example.personal.bank.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByUser(User user);

    Optional<Account> findByUserId(Long userId);
}
