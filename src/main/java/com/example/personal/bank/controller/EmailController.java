package com.example.personal.bank.controller;

import com.example.personal.bank.dto.AddEmailRequest;
import com.example.personal.bank.dto.DeleteEmailRequest;
import com.example.personal.bank.dto.EmailDTO;
import com.example.personal.bank.dto.UpdateEmailRequest;
import com.example.personal.bank.service.EmailService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/email")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class EmailController {

    private final EmailService emailService;

    @ApiResponse(responseCode = "200", description = "Email created successfully, returns email ID")
    @PostMapping("/add-email")
    public ResponseEntity<Long> addEmail(@Valid @RequestBody AddEmailRequest request) {
        EmailDTO email = emailService.createEmail(request.getId(), request.getEmail());
        return ResponseEntity.ok(email.getId());
    }

    @ApiResponse(responseCode = "200", description = "Email updated successfully, returns email ID")
    @PostMapping("/change-email")
    public ResponseEntity<Long> changeEmail(@Valid @RequestBody UpdateEmailRequest request) {
        EmailDTO updatedEmail = emailService.updateEmail(request.getId(), request.getEmailId(), request.getEmail());
        return ResponseEntity.ok(updatedEmail.getId());
    }

    @ApiResponse(responseCode = "200", description = "Email deleted successfully")
    @DeleteMapping("/delete-email")
    public ResponseEntity<String> deleteEmail(@Valid @RequestBody DeleteEmailRequest request) {
        emailService.deleteEmail(request.getId(), request.getEmailId());
        return ResponseEntity.ok("Email deleted successfully");
    }
}
