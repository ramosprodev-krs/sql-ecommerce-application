package com.ramosprodev.sql_application.controller;

import com.ramosprodev.sql_application.entity.OrderEntity;
import com.ramosprodev.sql_application.entity.UserEntity;
import com.ramosprodev.sql_application.service.EmailService;
import com.ramosprodev.sql_application.service.PurchaseService;
import com.ramosprodev.sql_application.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/purchase")
public class PurchaseController {

    private final PurchaseService purchaseService;
    private final UserService userService;
    private final EmailService emailService;

    public PurchaseController(PurchaseService purchaseService, UserService userService, EmailService emailService) {
        this.purchaseService = purchaseService;
        this.userService = userService;
        this.emailService = emailService;
    }

    /**
     * This controller provides all the purchase related methods.
     * The @PreAuthorize validation is also present in this class.
     **/

    // 1. Balance deposit
    @Operation(summary = "User balance deposit", description = "Deposits the provided balance.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Balance deposited successfully."),
            @ApiResponse(responseCode = "400", description = "Bad format provided.", content = @Content),
            @ApiResponse(responseCode = "401", description = "You are not logged in or authenticated.", content = @Content),
            @ApiResponse(responseCode = "403", description = "You are not authorized.", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found.", content = @Content),
            @ApiResponse(responseCode = "500", description = "An Internal Server Error occurred.", content = @Content)
    })
    @PostMapping("/{userId}/deposit")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserEntity> depositBalance(@PathVariable Long userId, @RequestParam BigDecimal balance) {
        try {
            UserEntity selectedUser = purchaseService.depositBalance(userId, balance);
            emailService.sendDepositNotification(
                    selectedUser.getEmail(),
                    selectedUser.getUsername(),
                    balance,
                    selectedUser.getUserBalance()
            );
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
            @ApiResponse(responseCode = "401", description = "You are not logged in or authenticated.", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found.", content = @Content),
            @ApiResponse(responseCode = "500", description = "An Internal Server Error occurred.", content = @Content)
    })

    @PostMapping("/complete")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<OrderEntity> completePurchase() {
        try {
            OrderEntity generatedOrder = purchaseService.completePurchase();
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            UserEntity user = (UserEntity) userService.loadUserByUsername(username);
            emailService.sendGeneratedOrder(user.getEmail(), user.getUsername(), generatedOrder);
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
            @ApiResponse(responseCode = "401", description = "You are not logged in or authenticated.", content = @Content),
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

    // 2. Get my balance
    @Operation(summary = "User balance", description = "Returns the user current balance.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Balance returned successfully.", content = @Content),
            @ApiResponse(responseCode = "401", description = "You are not logged in or authenticated.", content = @Content),
            @ApiResponse(responseCode = "403", description = "You are not authorized.", content = @Content),
            @ApiResponse(responseCode = "500", description = "An Internal Server Error occurred.", content = @Content)
    })
    @GetMapping("/balance")
    public ResponseEntity<BigDecimal> getMyBalance() {
        try {
            BigDecimal balance = purchaseService.getMyBalance();
            return ResponseEntity.ok(balance);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (DataAccessException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
