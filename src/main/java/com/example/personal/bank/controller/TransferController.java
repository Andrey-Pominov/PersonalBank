package com.example.personal.bank.controller;

import com.example.personal.bank.dto.TransferRequest;
import com.example.personal.bank.service.TransferService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@RequestMapping("/transfer")
@SecurityRequirement(name = "bearerAuth")
public class TransferController {

    private final TransferService transferService;

    @PostMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    @ApiResponse(responseCode = "200", description = "Transfer completed successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    public ResponseEntity<String> transfer(
            @Parameter(description = "Transfer request details, including recipient ID and amount")
            @RequestBody TransferRequest request,
            @Parameter(description = "ID of the authenticated sender, extracted from JWT token", hidden = true)
            @AuthenticationPrincipal Long id
    ) {
        transferService.transfer(id, request);
        return ResponseEntity.ok("Transfer completed");
    }
}

