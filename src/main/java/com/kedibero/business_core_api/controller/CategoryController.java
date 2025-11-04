package com.kedibero.business_core_api.controller;

import com.kedibero.business_core_api.dto.CategoryRequest;
import com.kedibero.business_core_api.dto.CategoryResponse;
import com.kedibero.business_core_api.dto.ApiResponse;
import com.kedibero.business_core_api.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(@RequestBody CategoryRequest request) {
        System.out.println("=== CREATE CATEGORY DEBUG ===");
        System.out.println("Request name: " + request.getName());
        System.out.println("Request description: " + request.getDescription());

        CategoryResponse response = categoryService.createCategory(request);

        System.out.println("Response: " + response);
        if (response != null) {
            System.out.println("Response id: " + response.getId());
            System.out.println("Response name: " + response.getName());
            System.out.println("Response description: " + response.getDescription());
            System.out.println("Response isActive: " + response.getIsActive());
            System.out.println("Response createdAt: " + response.getCreatedAt());
        }
        System.out.println("===========================");

        if (response == null) {
            return ResponseEntity.status(500).body(ApiResponse.error("No se pudo crear la categoría"));
        }
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(@PathVariable Long id, @RequestBody CategoryRequest request) {
        CategoryResponse response = categoryService.updateCategory(id, request);
        if (response == null) return ResponseEntity.status(404).body(ApiResponse.error("Category not found or inactive"));
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long id) {
        boolean deleted = categoryService.deleteCategory(id);
        if (!deleted) return ResponseEntity.status(404).body(ApiResponse.error("Category not found or already inactive"));
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> listCategories() {
        List<CategoryResponse> categories = categoryService.listCategories();
        return ResponseEntity.ok(ApiResponse.success(categories));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategory(@PathVariable Long id) {
        CategoryResponse response = categoryService.getCategory(id);
        if (response == null) return ResponseEntity.status(404).body(ApiResponse.error("Category not found or inactive"));
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
