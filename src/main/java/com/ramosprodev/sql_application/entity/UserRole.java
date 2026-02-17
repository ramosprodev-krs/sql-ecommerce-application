package com.ramosprodev.sql_application.entity;

import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER"),
    MANAGER("ROLE_MANAGER");

    private String role;

    UserRole(String role) {
        this.role = role;
    }
}
