package com.example.shopapp.dtos;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDetailDTO {
    private Long id;

    @NotNull(message = "Order ID is required")
    private Long orderId;

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Number of products is required")
    @Min(value = 1, message = "Number of products must be at least 1")
    private int numberOfProducts;

    @NotNull(message = "Total money is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Total money must be greater than 0")
    private double totalMoney;
}
