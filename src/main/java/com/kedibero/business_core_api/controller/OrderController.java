package com.kedibero.business_core_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.kedibero.business_core_api.service.OrderService;
import com.kedibero.business_core_api.dto.ApiResponse;
import com.kedibero.business_core_api.security.UserDetailsImpl;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/from-cart")
    public ResponseEntity<ApiResponse<Object>> createOrderFromCart() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetailsImpl userDetails) {
            try {
                Object order = orderService.createOrderFromCart(userDetails.getUsername());
                return ResponseEntity.ok(ApiResponse.success(order));
            } catch (IllegalArgumentException ex) {
                return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
            }
        } else {
            return ResponseEntity.status(401).body(ApiResponse.error("No autenticado"));
        }
    }
}
