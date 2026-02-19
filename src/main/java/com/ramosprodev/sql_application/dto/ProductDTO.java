package com.ramosprodev.sql_application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
public class ProductDTO {

    @NotBlank(message = "The provided product name can't be blank.")
    @Size(min = 5, max = 20)
    @Schema(description = "Name the product (and the producer if you'd wish)", example = "LEGO Star Wars X-Wing")
    private String name;

    @NotBlank(message = "The provided description can't be blank.")
    @Size(min = 5, max = 20)
    @Schema(description = "Describe and detail your product's properties, producers, important info...",
            example = "High quality LEGO pieces, offering the best comfort you can find while building.")
    private String description;

    @NotNull(message = "Stock quantity cannot be negative.")
    @Schema(description = "Set the initial amount of stock for your product (can't be negative).", example = "1, 10, 20...")
    private Integer stockQuantity;

    @NotNull(message = "Price can't be null.")
    @Schema(description = "Set the price of your product.", example = "100.00, 200.00, 500.00...")
    private BigDecimal price;
}
