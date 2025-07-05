package com.example.personal.bank.controller;

import com.example.personal.bank.dto.user.TransferRequest;
import com.example.personal.bank.service.TransferService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transfer")
@SecurityRequirement(name = "bearerAuth")
public class TransferController {

    private final TransferService transferService;

    @PostMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    @ApiResponse(responseCode = "200", description = "Transfer completed successfully")
    public ResponseEntity<String> transfer(@Valid @RequestBody TransferRequest request, @AuthenticationPrincipal Long id) {
        transferService.transfer(id, request);
        return ResponseEntity.ok("Transfer completed");
    }
}

