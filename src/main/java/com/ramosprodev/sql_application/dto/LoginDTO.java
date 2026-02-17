package com.ramosprodev.sql_application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoginDTO {

    @NotBlank(message = "The provided product username can't be blank.")
    @Schema(description = "Provide your username.", example = "johndoe123")
    private String username;

    @NotBlank(message = "The provided password can't be blank.")
    @Schema(description = "Provide your password.", example = "secret_password123@")
    private String password;
}
