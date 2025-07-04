package com.example.personal.bank.controller;

import com.example.personal.bank.dto.TransferRequest;
import com.example.personal.bank.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transfer")
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<String> transfer(@RequestBody TransferRequest request,
                                           @AuthenticationPrincipal(expression = "principal") Long senderUserId) {
        transferService.transfer(senderUserId, request);
        return ResponseEntity.ok("Transfer completed");
    }
}

