package com.example.personal.bank.controller;


import com.example.personal.bank.dto.AddPhoneRequest;
import com.example.personal.bank.dto.DeletePhoneRequest;
import com.example.personal.bank.dto.PhoneDTO;
import com.example.personal.bank.dto.UpdatePhoneRequest;
import com.example.personal.bank.service.PhoneService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/phone")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class PhoneController {
    private final PhoneService phoneService;

    @ApiResponse(responseCode = "200", description = "Phone created successfully, returns phone ID")
    @PostMapping("/add-phone")
    public ResponseEntity<PhoneDTO> addPhone(@RequestBody AddPhoneRequest request) {
        PhoneDTO phone = phoneService.createPhone(request.getId(), request.getPhone());
        return ResponseEntity.ok(phone);
    }

    @ApiResponse(responseCode = "200", description = "Phone updated successfully, returns phone ID")
    @PostMapping("/change-phone")
    public ResponseEntity<PhoneDTO> changePhone(@RequestBody UpdatePhoneRequest request) {
        PhoneDTO phone = phoneService.updatePhone(request.getId(), request.getPhoneId(), request.getPhone());
        return ResponseEntity.ok(phone);
    }

    @ApiResponse(responseCode = "200", description = "Phone deleted successfully")
    @DeleteMapping("/delete-phone")
    public ResponseEntity<String> deletePhone(@RequestBody DeletePhoneRequest request) {
        phoneService.deletePhone(request.getId(), request.getPhoneId());
        return ResponseEntity.ok("Phone deleted successfully");
    }
}
