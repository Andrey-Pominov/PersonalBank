package com.example.personal.bank.controller;

import com.example.personal.bank.dto.AddEmailRequest;
import com.example.personal.bank.dto.DeleteEmailRequest;
import com.example.personal.bank.dto.EmailDTO;
import com.example.personal.bank.dto.UpdateEmailRequest;
import com.example.personal.bank.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Add a new email for a user", description = "Creates a new email associated with the specified user ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Email created successfully, returns email ID"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden: User ID does not match authenticated user")
    })
    @PostMapping("/add-email")
    public ResponseEntity<Long> addEmail(@Valid @RequestBody AddEmailRequest request) {
        EmailDTO email = emailService.createEmail(request.getId(), request.getEmail());
        return ResponseEntity.ok(email.getId());
    }

    @Operation(summary = "Update an existing email", description = "Updates the email address for a specific email ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Email updated successfully, returns email ID"),
            @ApiResponse(responseCode = "400", description = "Invalid request data or email already in use"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden: User ID does not match authenticated user"),
            @ApiResponse(responseCode = "404", description = "Email or user not found")
    })
    @PostMapping("/change-email")
    public ResponseEntity<Long> changeEmail(@Valid @RequestBody UpdateEmailRequest request) {
        EmailDTO updatedEmail = emailService.updateEmail(request.getId(), request.getEmailId(), request.getEmail());
        return ResponseEntity.ok(updatedEmail.getId());
    }

    @Operation(summary = "Delete an email", description = "Deletes an email associated with a user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Email deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Cannot delete the last email or invalid request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden: User ID does not match authenticated user"),
            @ApiResponse(responseCode = "404", description = "Email or user not found")
    })
    @DeleteMapping("/delete-email")
    public ResponseEntity<String> deleteEmail(@Valid @RequestBody DeleteEmailRequest request) {
        emailService.deleteEmail(request.getId(), request.getEmailId());
        return ResponseEntity.ok("Email deleted successfully");
    }
}
