package com.example.personal.bank.service;

import com.example.personal.bank.entities.Account;
import com.example.personal.bank.entities.User;
import com.example.personal.bank.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);
    private final AccountRepository accountRepository;

    @Cacheable(value = "accounts", key = "#userId")
    public Account findByUserId(Long userId) {
        logger.info("Fetching account for user: {}", userId);
        User user = new User();
        user.setId(userId);
        return accountRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Account not found for user: " + userId));
    }

}
