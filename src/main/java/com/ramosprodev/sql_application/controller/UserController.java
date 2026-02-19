package com.ramosprodev.sql_application.controller;

import com.ramosprodev.sql_application.dto.UserDTO;
import com.ramosprodev.sql_application.entity.UserEntity;
import com.ramosprodev.sql_application.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    // Following CRUD Methods applied with endpoints:

    // 1. Create user
    @Operation(summary = "User creation", description = "Creates a new user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully created."),
            @ApiResponse(responseCode = "400", description = "User DTO was provided null.", content = @Content),
            @ApiResponse(responseCode = "403", description = "You are not authorized to create users.", content = @Content),
            @ApiResponse(responseCode = "500", description = "An Internal Server Error occurred.", content = @Content)
        }
    )
    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserEntity> createUser(@RequestBody @Valid UserDTO userDTO) {
        try {
            UserEntity createdUser = userService.createUser(userDTO);
            return ResponseEntity.ok(createdUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (DataAccessException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // 2. Read all users
    @Operation(summary = "Users list", description = "Returns a list of all users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users successfully found."),
            @ApiResponse(responseCode = "403", description = "You are not authorized to read other users.", content = @Content),
            @ApiResponse(responseCode = "500", description = "An Internal Server Error occurred.", content = @Content)
    }
    )
    @GetMapping("/read/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserEntity>> readAllUsers() {
        try {
            var selectedUsers = userService.readAllUsers();
            return ResponseEntity.ok(selectedUsers);
        } catch (DataAccessException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // 2.1 Read single user
    @Operation(summary = "User reading", description = "Returns the requested user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully found."),
            @ApiResponse(responseCode = "404", description = "User does not exist in the database.", content = @Content),
            @ApiResponse(responseCode = "403", description = "You are not authorized to read another user.", content = @Content),
            @ApiResponse(responseCode = "500", description = "An Internal Server Error occurred.", content = @Content)
    }
    )
    @GetMapping("/read/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<UserEntity> readUserById(@PathVariable Long id) {
        try {
            var selectedUser = userService.readUserById(id);
            return ResponseEntity.ok(selectedUser);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (DataAccessException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // 3. Update single user
    @Operation(summary = "User data update", description = "Updates the requested user (username/password/e-mail).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully updated and returned."),
            @ApiResponse(responseCode = "404", description = "User does not exist in the database.", content = @Content),
            @ApiResponse(responseCode = "403", description = "You are not authorized to update another user.", content = @Content),
            @ApiResponse(responseCode = "500", description = "An Internal Server Error occurred.", content = @Content)
    }
    )
    @PatchMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<UserEntity> updateUserById(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
        try {
            var updatedUser = userService.updateUserById(id, userDTO);
            return ResponseEntity.ok(updatedUser);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (DataAccessException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // 4. Delete a single user
    @Operation(summary = "User deletion", description = "Deletes the requested user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully deleted from the database.", content = @Content),
            @ApiResponse(responseCode = "404", description = "User does not exist in the database.", content = @Content),
            @ApiResponse(responseCode = "403", description = "You are not authorized to delete another user.", content = @Content),
            @ApiResponse(responseCode = "500", description = "An Internal Server Error occurred.", content = @Content)
    })
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (DataAccessException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
