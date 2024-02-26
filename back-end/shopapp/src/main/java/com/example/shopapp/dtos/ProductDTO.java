package com.example.shopapp.dtos;

import lombok.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductDTO {
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @Min(value = 0, message = "Price must be greater or equal to 0")
    @Max(value = 10000000, message = "Price must be less or equal to 10,000,000")
    @NotNull(message = "Price is required")
    private double price;

    private String thumbnail;

    private int starRating;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Category ID is required")
    private Long categoryId;
}
