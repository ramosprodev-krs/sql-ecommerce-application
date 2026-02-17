package com.ramosprodev.sql_application.controller;

import com.ramosprodev.sql_application.dto.ProductDTO;
import com.ramosprodev.sql_application.service.ProductService;
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
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Following CRUD Methods applied with endpoints:

    // 1. Create product
    @Operation(summary = "Product creation", description = "Creates a new product.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product created successfully."),
            @ApiResponse(responseCode = "400", description = "Product DTO was provided null.", content = @Content),
            @ApiResponse(responseCode = "500", description = "An Internal Server Error occurred.", content = @Content)
    })
    @PostMapping()
    public ResponseEntity<?> createProduct(@RequestBody @Valid ProductDTO productDTO) {
        try {
            var createdProduct = productService.createProduct(productDTO);
            return ResponseEntity.ok(createdProduct);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 2. Read all products
    @Operation(summary = "Products list", description =  "Returns a list of all the products.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products found successfully.", content = @Content),
            @ApiResponse(responseCode = "500", description = "An Internal Server Error occurred.", content = @Content)
    })
    @GetMapping("/read/all")
    public ResponseEntity<?> readAllProducts() {
        try {
            var selectedProducts = productService.readAllProducts();
            return ResponseEntity.ok(selectedProducts);
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 2.1 Read a single product
    @Operation(summary = "Product reading", description = "Returns the requested product." )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product successfully found."),
            @ApiResponse(responseCode = "404", description = "Product not found.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error occurred.", content = @Content)
    })
    @GetMapping("/read/{id}")
    public ResponseEntity<?> readProductById(@PathVariable Long id) {
        try {
            var selectedUser = productService.readProductById(id);
            return ResponseEntity.ok(selectedUser);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 3. Update a single product
    @Operation(summary = "Product data update",
            description = "Updates the requested product (name/description/stock quantity/price)." )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product successfully updated."),
            @ApiResponse(responseCode = "404", description = "Product not found.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error occurred.", content = @Content)
    })
    @PatchMapping("/update/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody @Valid ProductDTO productDTO) {
        try {
            var updatedUser = productService.updateProduct(id, productDTO);
            return ResponseEntity.ok(updatedUser);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 4. Delete a single product
    @Operation(summary = "Product deletion", description = "Deletes the requested product." )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product successfully deleted."),
            @ApiResponse(responseCode = "404", description = "Product not found.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error occurred.", content = @Content)
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }
}
