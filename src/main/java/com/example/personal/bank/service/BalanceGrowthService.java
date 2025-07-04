package com.example.personal.bank.service;

import com.example.personal.bank.entities.Account;
import com.example.personal.bank.repository.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BalanceGrowthService {

    private final AccountRepository accountRepository;

    @Transactional
    @Scheduled(fixedRate = 30_000)
    public void increaseBalancePeriodically() {
        List<Account> accounts = accountRepository.findAll();

        for (Account account : accounts) {
            BigDecimal initial = account.getInitialBalance();
            BigDecimal max = initial.multiply(BigDecimal.valueOf(2.07));
            BigDecimal newBalance = account.getBalance().multiply(BigDecimal.valueOf(1.10));

            if (newBalance.compareTo(max) > 0) {
                newBalance = max;
            }

            account.setBalance(newBalance);
        }

        accountRepository.saveAll(accounts);
    }
}

