package com.example.personal.bank.dto;

import lombok.Data;

@Data
public class UpdatePhoneRequest {
    public Long id;
    public Long phoneId;
    public String phone;

}
