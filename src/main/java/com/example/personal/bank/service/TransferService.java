package com.example.personal.bank.service;

import com.example.personal.bank.dto.TransferRequest;
import com.example.personal.bank.entities.Account;
import com.example.personal.bank.repository.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final AccountRepository accountRepository;

    @Transactional
    public void transfer(Long senderUserId, TransferRequest request) {
        if (senderUserId.equals(request.getRecipientUserId())) {
            throw new IllegalArgumentException("Cannot transfer to yourself");
        }

        Account sender = accountRepository.findByUserId(senderUserId)
                .orElseThrow(() -> new IllegalArgumentException("Sender account not found"));

        Account recipient = accountRepository.findByUserId(request.getRecipientUserId())
                .orElseThrow(() -> new IllegalArgumentException("Recipient account not found"));

        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive");
        }

        if (sender.getBalance().compareTo(request.getAmount()) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        sender.setBalance(sender.getBalance().subtract(request.getAmount()));
        recipient.setBalance(recipient.getBalance().add(request.getAmount()));

        accountRepository.save(sender);
        accountRepository.save(recipient);
    }
}
