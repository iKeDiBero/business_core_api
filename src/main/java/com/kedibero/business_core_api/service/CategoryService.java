package com.kedibero.business_core_api.service;

import com.kedibero.business_core_api.dto.CategoryRequest;
import com.kedibero.business_core_api.dto.CategoryResponse;
import com.kedibero.business_core_api.entity.Category;
import com.kedibero.business_core_api.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public CategoryResponse createCategory(CategoryRequest request) {
        System.out.println(">>> CategoryService.createCategory called");
        System.out.println(">>> Name: " + request.getName());
        System.out.println(">>> Description: " + request.getDescription());

        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setIsActive(true);

        System.out.println(">>> Before save - Category: " + category.getName());
        Category saved = categoryRepository.save(category);
        System.out.println(">>> After save - Saved ID: " + saved.getId() + ", Name: " + saved.getName());

        CategoryResponse response = toResponse(saved);
        System.out.println(">>> Response created - ID: " + response.getId() + ", Name: " + response.getName());

        return response;
    }

    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Optional<Category> optional = categoryRepository.findById(id);
        if (optional.isEmpty() || !optional.get().getIsActive()) return null;
        Category category = optional.get();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        Category saved = categoryRepository.save(category);
        return toResponse(saved);
    }

    public boolean deleteCategory(Long id) {
        Optional<Category> optional = categoryRepository.findById(id);
        if (optional.isEmpty() || !optional.get().getIsActive()) return false;
        Category category = optional.get();
        category.setIsActive(false);
        categoryRepository.save(category);
        return true;
    }

    public List<CategoryResponse> listCategories() {
        return categoryRepository.findByIsActiveTrue()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public CategoryResponse getCategory(Long id) {
        Optional<Category> optional = categoryRepository.findById(id);
        if (optional.isEmpty() || !optional.get().getIsActive()) return null;
        return toResponse(optional.get());
    }

    private CategoryResponse toResponse(Category category) {
        System.out.println(">>> toResponse - Category ID: " + category.getId());
        System.out.println(">>> toResponse - Category Name: " + category.getName());
        System.out.println(">>> toResponse - Category Description: " + category.getDescription());
        System.out.println(">>> toResponse - Category isActive: " + category.getIsActive());
        System.out.println(">>> toResponse - Category createdAt: " + category.getCreatedAt());

        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        response.setDescription(category.getDescription());
        response.setIsActive(category.getIsActive());
        response.setCreatedAt(category.getCreatedAt());
        response.setUpdatedAt(category.getUpdatedAt());

        System.out.println(">>> Response built - ID: " + response.getId());
        System.out.println(">>> Response built - Name: " + response.getName());

        return response;
    }
}

