package com.example.personal.bank.dto;

import lombok.Data;

@Data
public class UpdateEmailRequest {
    private Long id;
    private Long emailId;
    private String email;
}
