package com.kedibero.business_core_api.controller;

import com.kedibero.business_core_api.dto.BrandRequest;
import com.kedibero.business_core_api.dto.BrandResponse;
import com.kedibero.business_core_api.dto.ApiResponse;
import com.kedibero.business_core_api.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/brands")
public class BrandController {
    @Autowired
    private BrandService brandService;

    @PostMapping
    public ResponseEntity<ApiResponse<BrandResponse>> createBrand(@RequestBody BrandRequest request) {
        BrandResponse response = brandService.createBrand(request);
        if (response == null) {
            return ResponseEntity.status(500).body(ApiResponse.error("No se pudo crear la marca"));
        }
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BrandResponse>> updateBrand(@PathVariable Long id, @RequestBody BrandRequest request) {
        BrandResponse response = brandService.updateBrand(id, request);
        if (response == null) return ResponseEntity.status(404).body(ApiResponse.error("Brand not found or inactive"));
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBrand(@PathVariable Long id) {
        boolean deleted = brandService.deleteBrand(id);
        if (!deleted) return ResponseEntity.status(404).body(ApiResponse.error("Brand not found or already inactive"));
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BrandResponse>>> listBrands() {
        List<BrandResponse> brands = brandService.listBrands();
        return ResponseEntity.ok(ApiResponse.success(brands));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BrandResponse>> getBrand(@PathVariable Long id) {
        BrandResponse response = brandService.getBrand(id);
        if (response == null) return ResponseEntity.status(404).body(ApiResponse.error("Brand not found or inactive"));
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}

