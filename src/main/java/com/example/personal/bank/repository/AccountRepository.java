package com.example.personal.bank.repository;

import com.example.personal.bank.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.personal.bank.entities.Account;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    public Account findByAccountNumber(String accountNumber);
    public Optional<Account> findByUser(User user);
}
