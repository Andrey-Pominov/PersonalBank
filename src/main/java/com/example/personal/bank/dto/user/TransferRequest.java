package com.example.personal.bank.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class TransferRequest {
    private Long recipientUserId;
    private BigDecimal amount;
}
