package com.example.personal.bank.controller;

import com.example.personal.bank.dto.auth.AuthRequest;
import com.example.personal.bank.dto.auth.TokenResponse;
import com.example.personal.bank.service.AuthService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @ApiResponse(responseCode = "200", description = "Token issued")
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody AuthRequest request) {
        String token;
        switch (request.getType()) {
            case EMAIL -> token = authService.authenticateByEmail(request.getIdentifier(), request.getPassword());
            case PHONE -> token = authService.authenticateByPhone(request.getIdentifier(), request.getPassword());
            default -> throw new IllegalArgumentException("Unsupported login type: " + request.getType());
        }
        return ResponseEntity.ok(new TokenResponse(token));
    }
}
