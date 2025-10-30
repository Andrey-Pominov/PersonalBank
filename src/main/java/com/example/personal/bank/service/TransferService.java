package com.example.personal.bank.service;

import com.example.personal.bank.dto.user.TransferRequest;
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
        logger.info("Initiating transfer from user {} to user {} with amount {}",
                senderUserId, request.getRecipientUserId(), request.getAmount());

        if (senderUserId.equals(request.getRecipientUserId())) {
            logger.warn("Transfer rejected: Sender and recipient are the same (user ID: {})", senderUserId);
            throw new IllegalArgumentException("Cannot transfer to yourself");
        }

        BigDecimal amount = request.getAmount();
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive");
        }

        Account sender = accountRepository.findForUpdateByUserId(senderUserId)
                .orElseThrow(() -> new IllegalArgumentException("Sender account not found"));

        Account recipient = accountRepository.findForUpdateByUserId(request.getRecipientUserId())
                .orElseThrow(() -> new IllegalArgumentException("Recipient account not found"));

        if (sender.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        sender.setBalance(sender.getBalance().subtract(amount));
        recipient.setBalance(recipient.getBalance().add(amount));

        logger.info("Transfer completed: {} -> {} | amount: {}", senderUserId, request.getRecipientUserId(), amount);
    }
}
