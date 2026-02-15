package com.ramosprodev.sql_application.controller;

import com.ramosprodev.sql_application.entity.UserEntity;
import com.ramosprodev.sql_application.service.PurchaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/purchase")
public class PurchaseController {

    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @Operation(summary = "User balance deposit", description = "Deposits or not the provided balance.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Balance deposited successfully."),
            @ApiResponse(responseCode = "400", description = "Provided quantity invalid.", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found.", content = @Content),
            @ApiResponse(responseCode = "500", description = "An Internal Server Error occurred.", content = @Content)
    })
    @PostMapping("/{userId}/deposit")
    public ResponseEntity<?> depositBalance(@PathVariable Long userId, BigDecimal balance) {
        try {
            UserEntity selectedUser = purchaseService.depositBalance(userId, balance);
            return ResponseEntity.ok(selectedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "User purchase completion", description = "Deposits or not the provided balance.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Purchase has been successfully completed."),
            @ApiResponse(responseCode = "400", description = "Provided cart is empty or insufficient balance.", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found.", content = @Content),
            @ApiResponse(responseCode = "500", description = "An Internal Server Error occurred.", content = @Content)
    })

    @PostMapping("/{userId}/purchase")
    public ResponseEntity<?> completePurchase(@PathVariable Long userId) {
        try {
            UserEntity selectedUser = purchaseService.completePurchase(userId);
            return ResponseEntity.ok(selectedUser);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
