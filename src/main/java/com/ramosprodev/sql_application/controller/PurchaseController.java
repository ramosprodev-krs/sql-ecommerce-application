package com.ramosprodev.sql_application.controller;

import com.ramosprodev.sql_application.entity.OrderEntity;
import com.ramosprodev.sql_application.entity.UserEntity;
import com.ramosprodev.sql_application.service.PurchaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/purchase")
public class PurchaseController {

    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @Operation(summary = "User balance deposit", description = "Deposits the provided balance.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Balance deposited successfully."),
            @ApiResponse(responseCode = "400", description = "Provided quantity invalid.", content = @Content),
            @ApiResponse(responseCode = "403", description = "You are not authorized to deposit.", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found.", content = @Content),
            @ApiResponse(responseCode = "500", description = "An Internal Server Error occurred.", content = @Content)
    })
    @PostMapping("/{userId}/deposit")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserEntity> depositBalance(@PathVariable Long userId, BigDecimal balance) {
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

    @Operation(summary = "User purchase completion", description = "Deposits the provided balance.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Purchase has been successfully completed."),
            @ApiResponse(responseCode = "400", description = "Provided cart is empty or insufficient balance.", content = @Content),
            @ApiResponse(responseCode = "403", description = "You are not authorized to complete this purchase.", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found.", content = @Content),
            @ApiResponse(responseCode = "500", description = "An Internal Server Error occurred.", content = @Content)
    })

    @PostMapping("/complete")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<OrderEntity> completePurchase() {
        try {
            OrderEntity generatedOrder= purchaseService.completePurchase();
            return ResponseEntity.ok(generatedOrder);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "User orders list", description = "Shows the user orders list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List successfully returned."),
            @ApiResponse(responseCode = "403", description = "You are not authorized to complete this request", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found.", content = @Content),
            @ApiResponse(responseCode = "500", description = "An Internal Server Error occurred.", content = @Content)
    })
    @GetMapping("/orders")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<OrderEntity>> readOrders() {
        try{
            var orders = purchaseService.readOrders();
            return ResponseEntity.ok(orders);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
