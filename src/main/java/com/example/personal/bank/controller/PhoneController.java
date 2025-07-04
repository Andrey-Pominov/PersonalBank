package com.example.personal.bank.controller;


import com.example.personal.bank.dto.AddPhoneRequest;
import com.example.personal.bank.dto.DeletePhoneRequest;
import com.example.personal.bank.dto.PhoneDTO;
import com.example.personal.bank.dto.UpdatePhoneRequest;
import com.example.personal.bank.service.PhoneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/phone")
@RequiredArgsConstructor
public class PhoneController {
    private final PhoneService phoneService;

    @Operation(summary = "Add a new phone for a user", description = "Creates a new phone associated with the specified user ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Phone created successfully, returns phone ID"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden: User ID does not match authenticated user")
    })
    @PostMapping("/add-phone")
    public ResponseEntity<PhoneDTO> addPhone(@RequestBody AddPhoneRequest request) {
        PhoneDTO phone = phoneService.createPhone(request.getId(), request.getPhone());
        return ResponseEntity.ok(phone);
    }

    @Operation(summary = "Update an existing phone", description = "Updates the phone for a specific phone ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Phone updated successfully, returns phone ID"),
            @ApiResponse(responseCode = "400", description = "Invalid request data or phone already in use"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden: User ID does not match authenticated user"),
            @ApiResponse(responseCode = "404", description = "Phone or user not found")
    })
    @PostMapping("/change-phone")
    public ResponseEntity<PhoneDTO> changePhone(@RequestBody UpdatePhoneRequest request) {
        PhoneDTO phone = phoneService.updatePhone(request.getId(), request.getPhoneId(), request.getPhone());
        return ResponseEntity.ok(phone);
    }

    @Operation(summary = "Delete an phone", description = "Deletes an phone associated with a user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Phone deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Cannot delete the last phone or invalid request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden: User ID does not match authenticated user"),
            @ApiResponse(responseCode = "404", description = "Phone or user not found")
    })
    @DeleteMapping("/delete-phone")
    public ResponseEntity<String> deletePhone(@RequestBody DeletePhoneRequest request) {
        phoneService.deletePhone(request.getId(), request.getPhoneId());
        return ResponseEntity.ok("Phone deleted successfully");
    }
}
