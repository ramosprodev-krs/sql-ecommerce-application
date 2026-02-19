package com.ramosprodev.sql_application.controller;

import com.ramosprodev.sql_application.entity.CartEntity;
import com.ramosprodev.sql_application.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PostMapping("/items/{productId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<CartEntity> addItemToCart(@PathVariable Long productId, Integer quantity) {
        try {
            var selectedCart = cartService.addItemToCart(productId, quantity);
            return ResponseEntity.ok(selectedCart);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Cart item removal", description = "Removes the requested item from the cart." )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item successfully removed."),
            @ApiResponse(responseCode = "404", description = "Cart item not found.", content = @Content),
            @ApiResponse(responseCode = "403", description = "You are not authorized to remove this cart item.", content = @Content),
            @ApiResponse(responseCode = "400", description = "Provided data invalid.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error occurred.", content = @Content)
    })
    @DeleteMapping("/items/{cartItemId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<CartEntity> removeCartItem(@PathVariable Long cartItemId) {
        try {
            cartService.removeCartItem(cartItemId);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Cart item reading", description = "Reads the requested cart." )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart successfully read."),
            @ApiResponse(responseCode = "404", description = "Cart not found.", content = @Content),
            @ApiResponse(responseCode = "403", description = "You are not authorized to read this cart.", content = @Content),
            @ApiResponse(responseCode = "400", description = "Provided data invalid.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error occurred.", content = @Content)
    })
    @GetMapping("/read")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<CartEntity> readCart() {
        try {
            var userCart = cartService.readCart();
            return ResponseEntity.ok(userCart);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Cart item clearing", description = "Clears the requested cart." )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item successfully cleared."),
            @ApiResponse(responseCode = "404", description = "Cart not found.", content = @Content),
            @ApiResponse(responseCode = "403", description = "You are not authorized to clear this cart.", content = @Content),
            @ApiResponse(responseCode = "400", description = "Provided data invalid.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error occurred.", content = @Content)
    })
    @DeleteMapping("/clear")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<CartEntity> clearCart() {
        try {
            var userCart = cartService.clearCart();
            return ResponseEntity.ok(userCart);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
