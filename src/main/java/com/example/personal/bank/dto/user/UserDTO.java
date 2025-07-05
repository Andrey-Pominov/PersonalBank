package com.example.personal.bank.dto.user;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDTO {
    private Long id;
    private String name;
    private LocalDate dateOfBirth;
}

