package com.example.shopapp.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.shopapp.dtos.CategoryDTO;
import com.example.shopapp.models.Category;
import com.example.shopapp.repositories.CategoryRepository;
import com.example.shopapp.services.interfaces.ICategoryService;

@Service

public class CategoryService implements ICategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();

        return categories.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        return modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO, Category.class);
        category = categoryRepository.save(category);

        return modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Category not found with id: " + id);
        }

        Category newCategory = modelMapper.map(categoryDTO, Category.class);
        newCategory.setId(id);
        newCategory = categoryRepository.save(newCategory);

        return modelMapper.map(newCategory, CategoryDTO.class);
    }

    @Override
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Category not found with id: " + id);
        }

        categoryRepository.deleteById(id);
    }
}
