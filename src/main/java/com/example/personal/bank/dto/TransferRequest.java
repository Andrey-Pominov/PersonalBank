package com.example.personal.bank.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferRequest {
    private Long recipientUserId;
    private BigDecimal amount;
}
