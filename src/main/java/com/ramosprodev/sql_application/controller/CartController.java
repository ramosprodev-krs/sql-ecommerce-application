package com.ramosprodev.sql_application.controller;

import com.ramosprodev.sql_application.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // 1. Cart item addition method
    @Operation(summary = "Cart item addition", description = "Adds the requested item to the cart." )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item successfully added."),
            @ApiResponse(responseCode = "404", description = "User or product not found.", content = @Content),
            @ApiResponse(responseCode = "400", description = "Provided quantity invalid.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error occurred.", content = @Content)
    })
    @PostMapping("/{userId}/items/{productId}")
    public ResponseEntity<?> addItemToCart(@PathVariable Long userId, @PathVariable Long productId, Integer quantity) {
        try {
            var selectedCart = cartService.addItemToCart(userId, productId, quantity);
            return ResponseEntity.ok(selectedCart);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{userId}/items/{cartItemId}")
    public ResponseEntity<?> removeCartItem(@PathVariable Long userId, @PathVariable Long cartItemId) {
        try {
            cartService.removeCartItem(userId, cartItemId);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
