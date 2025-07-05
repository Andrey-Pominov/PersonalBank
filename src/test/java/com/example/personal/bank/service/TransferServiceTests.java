package com.example.personal.bank.service;

import com.example.personal.bank.dto.user.TransferRequest;
import com.example.personal.bank.entities.Account;
import com.example.personal.bank.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TransferServiceTests {

    private AccountRepository accountRepository;
    private TransferService transferService;

    @BeforeEach
    void setUp() {
        accountRepository = mock(AccountRepository.class);
        transferService = new TransferService(accountRepository);
    }

    @Test
    void testSuccessfulTransfer() {
        Long senderId = 1L;
        Long recipientId = 2L;
        BigDecimal amount = new BigDecimal("100.00");

        Account sender = new Account();
        sender.setId(senderId);
        sender.setBalance(new BigDecimal("500.00"));

        Account recipient = new Account();
        recipient.setId(recipientId);
        recipient.setBalance(new BigDecimal("200.00"));

        TransferRequest request = new TransferRequest(recipientId, amount);

        when(accountRepository.findByUserId(senderId)).thenReturn(Optional.of(sender));
        when(accountRepository.findByUserId(recipientId)).thenReturn(Optional.of(recipient));

        transferService.transfer(senderId, request);

        assertEquals(new BigDecimal("400.00"), sender.getBalance());
        assertEquals(new BigDecimal("300.00"), recipient.getBalance());
        verify(accountRepository, times(1)).save(sender);
        verify(accountRepository, times(1)).save(recipient);
    }

    @Test
    void testTransferToSelfThrowsException() {
        Long userId = 1L;
        TransferRequest request = new TransferRequest(userId, new BigDecimal("50"));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> transferService.transfer(userId, request));

        assertEquals("Cannot transfer to yourself", ex.getMessage());
        verifyNoInteractions(accountRepository);
    }

    @Test
    void testTransferWithInsufficientBalance() {
        Long senderId = 1L;
        Long recipientId = 2L;

        Account sender = new Account();
        sender.setId(senderId);
        sender.setBalance(new BigDecimal("10.00"));

        Account recipient = new Account();
        recipient.setId(recipientId);
        recipient.setBalance(new BigDecimal("100.00"));

        TransferRequest request = new TransferRequest(recipientId, new BigDecimal("50.00"));

        when(accountRepository.findByUserId(senderId)).thenReturn(Optional.of(sender));
        when(accountRepository.findByUserId(recipientId)).thenReturn(Optional.of(recipient));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> transferService.transfer(senderId, request));

        assertEquals("Insufficient balance", ex.getMessage());
        verify(accountRepository, never()).save(any());
    }

    @Test
    void testTransferWithNegativeAmount() {
        Long senderId = 1L;
        Long recipientId = 2L;

        Account sender = new Account();
        sender.setId(senderId);
        sender.setBalance(new BigDecimal("100.00"));

        Account recipient = new Account();
        recipient.setId(recipientId);
        recipient.setBalance(new BigDecimal("100.00"));

        TransferRequest request = new TransferRequest(recipientId, new BigDecimal("-20.00"));

        when(accountRepository.findByUserId(senderId)).thenReturn(Optional.of(sender));
        when(accountRepository.findByUserId(recipientId)).thenReturn(Optional.of(recipient));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> transferService.transfer(senderId, request));

        assertEquals("Transfer amount must be positive", ex.getMessage());
        verify(accountRepository, never()).save(any());
    }
}
