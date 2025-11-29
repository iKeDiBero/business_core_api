package com.kedibero.business_core_api.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kedibero.business_core_api.dto.ApiResponse;
import com.kedibero.business_core_api.security.UserDetailsImpl;
import com.kedibero.business_core_api.service.WarehouseService;

@RestController
@RequestMapping("/api/warehouse")
public class WarehouseController {

    @Autowired
    private WarehouseService warehouseService;

    /**
     * Obtiene el resumen del almacén del usuario
     */
    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getWarehouseSummary() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetailsImpl userDetails) {
            try {
                Map<String, Object> summary = warehouseService.getWarehouseSummary(userDetails.getUsername());
                return ResponseEntity.ok(ApiResponse.success(summary));
            } catch (IllegalArgumentException ex) {
                return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
            }
        } else {
            return ResponseEntity.status(401).body(ApiResponse.error("No autenticado"));
        }
    }

    /**
     * Obtiene todos los productos del almacén del usuario
     */
    @GetMapping("/products")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getWarehouseProducts() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetailsImpl userDetails) {
            try {
                List<Map<String, Object>> products = warehouseService.getWarehouseProducts(userDetails.getUsername());
                return ResponseEntity.ok(ApiResponse.success(products));
            } catch (IllegalArgumentException ex) {
                return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
            }
        } else {
            return ResponseEntity.status(401).body(ApiResponse.error("No autenticado"));
        }
    }

    /**
     * Obtiene el historial de compras de un producto específico
     */
    @GetMapping("/products/{productId}/history")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getProductHistory(@PathVariable Long productId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetailsImpl userDetails) {
            try {
                List<Map<String, Object>> history = warehouseService.getProductPurchaseHistory(
                    userDetails.getUsername(), productId);
                return ResponseEntity.ok(ApiResponse.success(history));
            } catch (IllegalArgumentException ex) {
                return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
            }
        } else {
            return ResponseEntity.status(401).body(ApiResponse.error("No autenticado"));
        }
    }
}
