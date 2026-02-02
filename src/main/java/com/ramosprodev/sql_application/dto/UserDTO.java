package com.ramosprodev.sql_application.dto;

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
    private String username;

    @NotBlank(message = "The provided password can't be blank.")
    @Min(value = 5, message = "The provided password is too short.")
    private String password;

    @Column(name = "email")
    @Email(message = "Please provide a valid E-mail.")
    private String email;
}
