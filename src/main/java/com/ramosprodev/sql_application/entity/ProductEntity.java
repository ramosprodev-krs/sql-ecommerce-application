package com.ramosprodev.sql_application.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
    @Size(min = 5, max = 20)
    private String name;

    @Column(name = "description")
    @NotBlank(message = "The provided description can't be blank.")
    @Size(min = 5, max = 20)
    private String description;

    @Column(name = "stock_quantity")
    @NotNull(message = "Stock quantity cannot be negative.")
    private Integer stockQuantity;

    @Column(name = "price")
    @NotNull(message = "Price can't be null.")
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private UserEntity creator;
}
