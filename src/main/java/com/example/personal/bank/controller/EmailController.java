package com.example.personal.bank.controller;

import com.example.personal.bank.dto.email.AddEmailRequest;
import com.example.personal.bank.dto.email.DeleteEmailRequest;
import com.example.personal.bank.dto.email.EmailDTO;
import com.example.personal.bank.dto.email.UpdateEmailRequest;
import com.example.personal.bank.service.EmailService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/email")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class EmailController {

    private final EmailService emailService;

    @ApiResponse(responseCode = "200", description = "Email created successfully, returns email ID")
    @PostMapping("/add-email")
    public ResponseEntity<Long> addEmail(@Valid @RequestBody AddEmailRequest request, @AuthenticationPrincipal Long id) {
        EmailDTO email = emailService.createEmail(id, request.getEmail());
        return ResponseEntity.ok(email.getId());
    }

    @ApiResponse(responseCode = "200", description = "Email updated successfully, returns email ID")
    @PostMapping("/change-email")
    public ResponseEntity<Long> changeEmail(@Valid @RequestBody UpdateEmailRequest request, @AuthenticationPrincipal Long id) {
        EmailDTO updatedEmail = emailService.updateEmail(id, request.getEmailId(), request.getEmail());
        return ResponseEntity.ok(updatedEmail.getId());
    }

    @ApiResponse(responseCode = "200", description = "Email deleted successfully")
    @DeleteMapping("/delete-email")
    public ResponseEntity<String> deleteEmail(@Valid @RequestBody DeleteEmailRequest request, @AuthenticationPrincipal Long id) {
        emailService.deleteEmail(id, request.getEmailId());
        return ResponseEntity.ok("Email deleted successfully");
    }
}
