package com.example.personal.bank.controller;

import com.example.personal.bank.dto.AuthRequest;
import com.example.personal.bank.dto.TokenResponse;
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
    @PostMapping("/login/email")
    public ResponseEntity<TokenResponse> loginByEmail(@RequestBody AuthRequest request) {
        String token = authService.authenticateByEmail(request.getIdentifier(), request.getPassword());
        return ResponseEntity.ok(new TokenResponse(token));
    }

    @ApiResponse(responseCode = "200", description = "Token issued")
    @PostMapping("/login/phone")
    public ResponseEntity<TokenResponse> loginByPhone(@RequestBody AuthRequest request) {
        String token = authService.authenticateByPhone(request.getIdentifier(), request.getPassword());
        return ResponseEntity.ok(new TokenResponse(token));
    }
}

