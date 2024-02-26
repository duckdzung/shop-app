package com.example.shopapp.repositories;

import com.example.shopapp.models.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.*;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByName(String name);

    // Custom query
    @Query("SELECT p FROM Product p WHERE (:categoryId = 0 OR p.category.id = :categoryId) " +
            "AND (:keyword = '' OR LOWER(p.name) LIKE %:keyword%)")
    Page<Product> findAllByCategoryIdAndKeyword(Long categoryId, String keyword, Pageable pageable);
}
