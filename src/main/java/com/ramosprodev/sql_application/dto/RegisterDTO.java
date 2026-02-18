package com.ramosprodev.sql_application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegisterDTO {

    @NotBlank(message = "The provided username can't be blank.")
    @Size(min = 5, max = 20)
    @Schema(description = "Do not use your real name.", example = "random_user123")
    private String username;

    @NotBlank(message = "The provided password can't be blank.")
    @Size(min = 5)
    @Schema(description = "Ensure only you know your password .", example = "My#supersecure$_password4319.")
    private String password;

    @Email(message = "Please provide a valid E-mail.")
    @Schema(description = "Remember to use a valid E-mail.", example = "ramosprodev@gmail.com")
    private String email;
}
