package com.example.shopapp.services.interfaces;

import java.util.List;

import com.example.shopapp.dtos.CategoryDTO;

public interface ICategoryService {
    List<CategoryDTO> getAllCategories();

    CategoryDTO getCategoryById(Long id);

    CategoryDTO createCategory(CategoryDTO categoryDTO);

    CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO);

    void deleteCategory(Long id);
}
