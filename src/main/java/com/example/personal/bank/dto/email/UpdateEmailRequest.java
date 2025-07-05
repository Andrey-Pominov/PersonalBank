package com.example.personal.bank.dto.email;

import lombok.Data;

@Data
public class UpdateEmailRequest {
    private Long emailId;
    private String email;
}
