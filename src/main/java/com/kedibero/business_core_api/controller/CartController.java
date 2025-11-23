package com.kedibero.business_core_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.kedibero.business_core_api.service.CartService;
import com.kedibero.business_core_api.dto.CartRequest;
import com.kedibero.business_core_api.dto.ApiResponse;
import com.kedibero.business_core_api.security.UserDetailsImpl;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping
    public ResponseEntity<ApiResponse<Object>> createCart(@RequestBody CartRequest cartRequest) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetailsImpl userDetails) {
            try {
                Object cart = cartService.createCart(cartRequest, userDetails.getUsername());
                return ResponseEntity.ok(ApiResponse.success(cart));
            } catch (IllegalArgumentException ex) {
                return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
            }
        } else {
            return ResponseEntity.status(401).body(ApiResponse.error("No autenticado"));
        }
    }

    @PutMapping
    public ResponseEntity<ApiResponse<Object>> updateCart(@RequestBody CartRequest cartRequest) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetailsImpl userDetails) {
            try {
                Object cart = cartService.updateCart(cartRequest, userDetails.getUsername());
                return ResponseEntity.ok(ApiResponse.success(cart));
            } catch (IllegalArgumentException ex) {
                return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
            }
        } else {
            return ResponseEntity.status(401).body(ApiResponse.error("No autenticado"));
        }
    }

    @GetMapping("/user/exists")
    public ResponseEntity<ApiResponse<Object>> userHasCart() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetailsImpl userDetails) {
            Object cart = cartService.getCartByUsername(userDetails.getUsername());
            if (cart == null) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(ApiResponse.success(cart));
        } else {
            return ResponseEntity.status(401).body(ApiResponse.error("No autenticado"));
        }
    }
}