package com.ramosprodev.sql_application.controller;

import com.ramosprodev.sql_application.dto.LoginDTO;
import com.ramosprodev.sql_application.dto.RegisterDTO;
import com.ramosprodev.sql_application.entity.UserEntity;
import com.ramosprodev.sql_application.service.TokenService;
import com.ramosprodev.sql_application.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthenticationController(@Lazy AuthenticationManager authenticationManager,
                          TokenService tokenService,
                          UserService userService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.userService = userService;
    }

    // 1. Login
    @Operation(summary = "User login", description = "Logs the user in.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully logged in."),
            @ApiResponse(responseCode = "403", description = "Invalid username or password provided.", content = @Content),
            @ApiResponse(responseCode = "404", description = "User does not exist in the database.", content = @Content),
            @ApiResponse(responseCode = "500", description = "An Internal Server Error occurred.", content = @Content)
    }
    )
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid LoginDTO loginDTO) {
        var authToken = new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());
        var authentication = this.authenticationManager.authenticate(authToken);
        var user = (UserEntity) authentication.getPrincipal();
        var token = this.tokenService.generateToken(user);
        return ResponseEntity.ok(token);
    }

    // 2. Register
    @Operation(summary = "User registration", description = "Registers a new user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User successfully registered."),
            @ApiResponse(responseCode = "400", description = "Register DTO was provided null.", content = @Content),
            @ApiResponse(responseCode = "500", description = "An Internal Server Error occurred.", content = @Content)
    }
    )
    @PostMapping("/register")
    public ResponseEntity<UserEntity> createUser(@RequestBody @Valid RegisterDTO registerDTO) {
        try {
            UserEntity createdUser = userService.registerUser(registerDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (DataAccessException e) {
            return ResponseEntity.internalServerError().build();
        }

    }


}
