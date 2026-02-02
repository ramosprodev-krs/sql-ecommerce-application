package com.ramosprodev.sql_application.controller;

import com.ramosprodev.sql_application.dto.UserDTO;
import com.ramosprodev.sql_application.entity.UserEntity;
import com.ramosprodev.sql_application.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(description = "Creates a new user and registers it into the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully created."),
            @ApiResponse(responseCode = "500", description = "User couldn't be created due to Internal Server Error.")
        }
    )
    @PostMapping()
    public ResponseEntity<?> createUser(@RequestBody @Valid UserDTO userDTO) {
        try {
            UserEntity createdUser = userService.createUser(userDTO);
            return ResponseEntity.ok("User \"" + createdUser.getUsername() + "\" created successfully.");
        } catch (DataAccessException e) {
            return ResponseEntity.internalServerError().body("Error: Internal Server Error occurred.");
        }
    }

    @Operation(description = "Searches for all of users registered in the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users successfully found."),
            @ApiResponse(responseCode = "500", description = "Users couldn't be searched due to Internal Server Error.")
    }
    )
    @GetMapping("/read")
    public ResponseEntity<?> readAllUsers() {
        try {
            var searchedUsers = userService.readAllUsers();
            return ResponseEntity.ok(searchedUsers);
        } catch (DataAccessException e) {
            return ResponseEntity.internalServerError().body("Error: Internal Server Error occurred.");
        }
    }

    @Operation(description = "Searches for the user based on the provided ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully found."),
            @ApiResponse(responseCode = "404", description = "User does not exist."),
            @ApiResponse(responseCode = "500", description = "User couldn't be searched due to Internal Server Error.")
    }
    )
    @GetMapping("/read/{id}")
    public ResponseEntity<?> readUserById(@PathVariable Long id) {
        try {
            var searchedUser = userService.readUserById(id);
            return ResponseEntity.ok(searchedUser);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        } catch (DataAccessException e) {
            return ResponseEntity.internalServerError().body("Error: Internal Server Error occurred.");
        }
    }

}
