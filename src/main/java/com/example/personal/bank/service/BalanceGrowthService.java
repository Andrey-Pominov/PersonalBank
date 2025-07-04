package com.example.personal.bank.service;

import com.example.personal.bank.entities.Account;
import com.example.personal.bank.repository.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BalanceGrowthService {

    private static final Logger logger = LoggerFactory.getLogger(BalanceGrowthService.class);

    private final AccountRepository accountRepository;

    @Transactional
    @Scheduled(fixedRate = 30_000)
    public void increaseBalancePeriodically() {
        logger.info("Starting periodic balance increase for all accounts");
        List<Account> accounts = accountRepository.findAll();
        logger.debug("Found {} accounts to process", accounts.size());

        for (Account account : accounts) {
            logger.debug("Processing account ID: {} with current balance: {}", account.getId(), account.getBalance());
            BigDecimal initial = account.getInitialBalance();
            BigDecimal max = initial.multiply(BigDecimal.valueOf(2.07));
            BigDecimal newBalance = account.getBalance().multiply(BigDecimal.valueOf(1.10));

            if (newBalance.compareTo(max) > 0) {
                logger.debug("Balance for account ID: {} capped at max: {}", account.getId(), max);
                newBalance = max;
            }

            account.setBalance(newBalance);
            logger.debug("Updated balance for account ID: {} to {}", account.getId(), newBalance);
        }

        accountRepository.saveAll(accounts);
        logger.info("Completed periodic balance increase for {} accounts", accounts.size());
    }
}
