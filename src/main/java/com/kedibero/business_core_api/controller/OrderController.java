package com.kedibero.business_core_api.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kedibero.business_core_api.dto.ApiResponse;
import com.kedibero.business_core_api.security.UserDetailsImpl;
import com.kedibero.business_core_api.service.NiubizService;
import com.kedibero.business_core_api.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private NiubizService niubizService;

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

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> getOrderById(@PathVariable Long id) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetailsImpl userDetails) {
            try {
                Object order = orderService.getOrderById(id, userDetails.getUsername());
                return ResponseEntity.ok(ApiResponse.success(order));
            } catch (IllegalArgumentException ex) {
                return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
            }
        } else {
            return ResponseEntity.status(401).body(ApiResponse.error("No autenticado"));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Object>> getAllOrders() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetailsImpl userDetails) {
            try {
                List<Map<String, Object>> orders = orderService.getAllOrders(userDetails.getUsername());
                return ResponseEntity.ok(ApiResponse.success(orders));
            } catch (IllegalArgumentException ex) {
                return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
            }
        } else {
            return ResponseEntity.status(401).body(ApiResponse.error("No autenticado"));
        }
    }

    @PostMapping("/{orderId}/payment-token")
    public ResponseEntity<ApiResponse<Object>> generatePaymentToken(@PathVariable Long orderId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetailsImpl userDetails) {
            try {
                // Obtener la orden para verificar que pertenece al usuario y obtener el monto
                Object orderData = orderService.getOrderById(orderId, userDetails.getUsername());
                
                if (orderData instanceof Object[]) {
                    Object[] orderArray = (Object[]) orderData;
                    double amount = ((Number) orderArray[2]).doubleValue();
                    
                    // Generar token de sesión
                    String sessionToken = niubizService.generateSessionToken(amount);
                    
                    Map<String, Object> response = new java.util.HashMap<>();
                    response.put("sessionToken", sessionToken);
                    response.put("merchantId", niubizService.getMerchantId());
                    response.put("amount", amount);
                    response.put("orderId", orderId);
                    
                    return ResponseEntity.ok(ApiResponse.success(response));
                }
                
                return ResponseEntity.badRequest().body(ApiResponse.error("Formato de orden inválido"));
            } catch (IllegalArgumentException ex) {
                return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
            } catch (Exception ex) {
                return ResponseEntity.status(500).body(ApiResponse.error("Error al generar token de pago: " + ex.getMessage()));
            }
        } else {
            return ResponseEntity.status(401).body(ApiResponse.error("No autenticado"));
        }
    }

}


