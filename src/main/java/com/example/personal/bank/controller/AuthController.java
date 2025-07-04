package com.example.personal.bank.controller;

import com.example.personal.bank.dto.AuthRequest;
import com.example.personal.bank.dto.TokenResponse;
import com.example.personal.bank.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Login by email", description = "Authenticate user by email and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token issued"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/login/email")
    public ResponseEntity<TokenResponse> loginByEmail(@RequestBody AuthRequest request) {
        String token = authService.authenticateByEmail(request.getIdentifier(), request.getPassword());
        return ResponseEntity.ok(new TokenResponse(token));
    }

    @Operation(summary = "Login by phone", description = "Authenticate user by phone and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token issued"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/login/phone")
    public ResponseEntity<TokenResponse> loginByPhone(@RequestBody AuthRequest request) {
        String token = authService.authenticateByPhone(request.getIdentifier(), request.getPassword());
        return ResponseEntity.ok(new TokenResponse(token));
    }
}

