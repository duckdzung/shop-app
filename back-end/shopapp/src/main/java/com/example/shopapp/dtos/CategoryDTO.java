package com.example.shopapp.dtos;

import lombok.*;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryDTO {
    private Long id;

    @NotNull(message = "Category's name cannot be empty.")
    String name;
}
