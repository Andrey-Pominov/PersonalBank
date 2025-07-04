package com.example.personal.bank.service;

import com.example.personal.bank.dto.TransferRequest;
import com.example.personal.bank.entities.Account;
import com.example.personal.bank.repository.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransferService {

    private static final Logger logger = LoggerFactory.getLogger(TransferService.class);

    private final AccountRepository accountRepository;

    @Transactional
    public void transfer(Long senderUserId, TransferRequest request) {
        logger.info("Initiating transfer from user ID: {} to user ID: {} with amount: {}",
                senderUserId, request.getRecipientUserId(), request.getAmount());

        if (senderUserId.equals(request.getRecipientUserId())) {
            logger.warn("Transfer rejected: Sender and recipient are the same (user ID: {})", senderUserId);
            throw new IllegalArgumentException("Cannot transfer to yourself");
        }

        Account sender = accountRepository.findByUserId(senderUserId)
                .orElseThrow(() -> {
                    logger.error("Transfer failed: Sender account not found for user ID: {}", senderUserId);
                    return new IllegalArgumentException("Sender account not found");
                });

        Account recipient = accountRepository.findByUserId(request.getRecipientUserId())
                .orElseThrow(() -> {
                    logger.error("Transfer failed: Recipient account not found for user ID: {}", request.getRecipientUserId());
                    return new IllegalArgumentException("Recipient account not found");
                });

        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            logger.warn("Transfer rejected: Invalid amount {} for user ID: {}", request.getAmount(), senderUserId);
            throw new IllegalArgumentException("Transfer amount must be positive");
        }

        if (sender.getBalance().compareTo(request.getAmount()) < 0) {
            logger.warn("Transfer rejected: Insufficient balance {} for user ID: {}, requested amount: {}",
                    sender.getBalance(), senderUserId, request.getAmount());
            throw new IllegalArgumentException("Insufficient balance");
        }

        sender.setBalance(sender.getBalance().subtract(request.getAmount()));
        recipient.setBalance(recipient.getBalance().add(request.getAmount()));
        logger.debug("Updated sender (ID: {}) balance to: {}, recipient (ID: {}) balance to: {}",
                sender.getId(), sender.getBalance(), recipient.getId(), recipient.getBalance());

        accountRepository.save(sender);
        accountRepository.save(recipient);
        logger.info("Transfer completed successfully from user ID: {} to user ID: {} with amount: {}",
                senderUserId, request.getRecipientUserId(), request.getAmount());
    }
}
