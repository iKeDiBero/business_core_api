package com.kedibero.business_core_api.controller;

import com.kedibero.business_core_api.dto.ProductRequest;
import com.kedibero.business_core_api.dto.ProductResponse;
import com.kedibero.business_core_api.dto.ApiResponse;
import com.kedibero.business_core_api.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(@RequestBody ProductRequest request) {
        ProductResponse response = productService.createProduct(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(@PathVariable Long id, @RequestBody ProductRequest request) {
        ProductResponse response = productService.updateProduct(id, request);
        if (response == null) return ResponseEntity.status(404).body(ApiResponse.error("Product not found or inactive"));
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        boolean deleted = productService.deleteProduct(id);
        if (!deleted) return ResponseEntity.status(404).body(ApiResponse.error("Product not found or already inactive"));
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponse>>> listProducts() {
        List<ProductResponse> products = productService.listProducts();
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProduct(@PathVariable Long id) {
        ProductResponse response = productService.getProduct(id);
        if (response == null) return ResponseEntity.status(404).body(ApiResponse.error("Product not found or inactive"));
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}
