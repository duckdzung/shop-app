package com.example.shopapp.services.interfaces;

import org.springframework.data.domain.Page;

import com.example.shopapp.dtos.ProductDTO;
import com.example.shopapp.dtos.ProductImageDTO;
import com.example.shopapp.models.Product;
import com.example.shopapp.responses.ProductResponse;

public interface IProductService {
    Page<ProductResponse> getAllProducts(int page, int limit, Long categoryId, String keyword);

    Product getProductById(Long id);

    ProductDTO createProduct(ProductDTO productDTO);

    ProductDTO updateProduct(Long id, ProductDTO productDTO);

    void deleteProduct(Long id);

    public ProductImageDTO createProductImage(Long productId, ProductImageDTO productImageDTO) throws Exception;

    boolean existsByName(String name);
}
