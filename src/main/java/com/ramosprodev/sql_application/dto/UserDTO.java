package com.ramosprodev.sql_application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {

    @NotBlank(message = "The provided username can't be blank.")
    @Min(value = 5, message = "The provided username is too short.")
    @Min(value = 20, message = "The provided username is too long.")
    @Schema(description = "Do not use your real name.", example = "random_user123")
    private String username;

    @NotBlank(message = "The provided password can't be blank.")
    @Min(value = 5, message = "The provided password is too short.")
    @Schema(description = "Ensure only you know your password .", example = "My#supersecure$_password4319.")
    private String password;

    @Column(name = "email")
    @Email(message = "Please provide a valid E-mail.")
    @Schema(description = "Remember to use a valid E-mail.", example = "ramosprodev@gmail.com")
    private String email;
}
