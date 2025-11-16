package com.kedibero.business_core_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.kedibero.business_core_api.service.CartService;
import com.kedibero.business_core_api.dto.CartRequest;
import com.kedibero.business_core_api.dto.ApiResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
// import org.springframework.security.core.userdetails.UserDetails;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping
public ResponseEntity<ApiResponse<Long>> createCart(
        @RequestBody CartRequest cartRequest,
        @AuthenticationPrincipal UserDetails userDetails) {
    try {
        Long cartId = cartService.createCart(cartRequest, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success(cartId));
    } catch (IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
    }
}

    @PutMapping("/{cartId}")
    public ResponseEntity<ApiResponse<Long>> updateCart(
            @PathVariable Long cartId,
            @RequestBody CartRequest cartRequest,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            Long updatedCartId = cartService.updateCart(cartId, cartRequest, userDetails.getUsername());
            return ResponseEntity.ok(ApiResponse.success(updatedCartId));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<Object>> getCartByUser(@PathVariable Long userId) {
        Object cart = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(cart));
    }

    @GetMapping("/user/{userId}/exists")
    public ResponseEntity<ApiResponse<Object>> userHasCart(@PathVariable Long userId) {
        Object cart = cartService.getCartByUserId(userId);
        if (cart == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(ApiResponse.success(cart));
    }

}