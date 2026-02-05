package com.ramosprodev.sql_application.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(name = "name")
    @NotBlank(message = "The provided product name can't be blank.")
    @Min(value = 2, message = "The provided product name is too short.")
    @Max(value = 20, message = "The provided product name is too long.")
    private String name;

    @Column(name = "description")
    @NotBlank(message = "The provided description can't be blank.")
    @Min(value = 2, message = "The provided description is too short.")
    @Max(value = 20, message = "The provided description is too long.")
    private String description;

    @Column(name = "stock_quantity")
    @NotNull(message = "Stock quantity cannot be negative.")
    private Integer stockQuantity;

    @Column(name = "price")
    @NotNull(message = "Price can't be null.")
    private BigDecimal price;
}
