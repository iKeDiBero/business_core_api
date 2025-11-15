package com.kedibero.business_core_api.controller;

import com.kedibero.business_core_api.dto.ModelRequest;
import com.kedibero.business_core_api.dto.ModelResponse;
import com.kedibero.business_core_api.dto.ApiResponse;
import com.kedibero.business_core_api.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/models")
public class ModelController {
    @Autowired
    private ModelService modelService;

    @PostMapping
    public ResponseEntity<ApiResponse<ModelResponse>> createModel(@RequestBody ModelRequest request) {
        ModelResponse response = modelService.createModel(request);
        if (response == null) {
            return ResponseEntity.status(500).body(ApiResponse.error("No se pudo crear el modelo"));
        }
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ModelResponse>> updateModel(@PathVariable Long id, @RequestBody ModelRequest request) {
        ModelResponse response = modelService.updateModel(id, request);
        if (response == null) return ResponseEntity.status(404).body(ApiResponse.error("Model not found or inactive"));
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteModel(@PathVariable Long id) {
        boolean deleted = modelService.deleteModel(id);
        if (!deleted) return ResponseEntity.status(404).body(ApiResponse.error("Model not found or already inactive"));
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ModelResponse>>> listModels(@RequestParam(required = false) Long brandId) {
        List<ModelResponse> models;
        if (brandId != null) {
            models = modelService.listModelsByBrand(brandId);
        } else {
            models = modelService.listModels();
        }
        return ResponseEntity.ok(ApiResponse.success(models));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ModelResponse>> getModel(@PathVariable Long id) {
        ModelResponse response = modelService.getModel(id);
        if (response == null) return ResponseEntity.status(404).body(ApiResponse.error("Model not found or inactive"));
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}

