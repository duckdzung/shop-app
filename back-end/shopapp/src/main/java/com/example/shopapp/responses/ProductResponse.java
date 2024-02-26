package com.example.shopapp.responses;

import org.modelmapper.ModelMapper;

import com.example.shopapp.models.Product;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductResponse {
    private Long id;

    private String name;

    private double price;

    private int starRating;

    private String thumbnail;

    private String description;

    private Long categoryId;

    public static ProductResponse fromProduct(Product product) {
        ModelMapper modelMapper = new ModelMapper();

        return modelMapper.map(product, ProductResponse.class);
    }
}
