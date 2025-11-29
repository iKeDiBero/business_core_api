package com.kedibero.business_core_api.controller;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kedibero.business_core_api.dto.ApiResponse;
import com.kedibero.business_core_api.dto.PaymentCallbackRequest;
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

    /**
     * Endpoint para recibir la respuesta de pago de Niubiz
     * Este endpoint es público ya que Niubiz hace el POST directamente
     * La URL debe incluir el orderId y opcionalmente un sessionId para Cuotéalo
     * Ejemplo: /api/orders/payment-callback?orderId=123&sessionId=abc123
     */
    @PostMapping("/payment-callback")
    public ResponseEntity<ApiResponse<Object>> paymentCallback(
            @RequestParam("orderId") Long orderId,
            @RequestParam(value = "sessionId", required = false) String sessionId,
            @RequestBody(required = false) PaymentCallbackRequest callbackData) {
        
        try {
            System.out.println("=== Payment Callback POST Received ===");
            System.out.println("Order ID: " + orderId);
            System.out.println("Session ID: " + sessionId);
            if (callbackData != null) {
                System.out.println("Transaction Token: " + callbackData.getTransactionToken());
                System.out.println("Transaction Code: " + callbackData.getTransactionCode());
                System.out.println("Action Code: " + callbackData.getActionCode());
                System.out.println("Action Description: " + callbackData.getActionDescription());
                System.out.println("Authorization Code: " + callbackData.getAuthorizationCode());
                System.out.println("Amount: " + callbackData.getAmount());
                System.out.println("Card Brand: " + callbackData.getCardBrand());
                System.out.println("Full Callback Data: " + callbackData.toString());
            } else {
                System.out.println("Callback Data is NULL");
            }
            
            // Procesar la respuesta del pago
            Map<String, Object> result = orderService.processPaymentCallback(orderId, callbackData);
            
            return ResponseEntity.ok(ApiResponse.success(result));
            
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        } catch (Exception ex) {
            System.err.println("Error processing payment callback: " + ex.getMessage());
            ex.printStackTrace();
            return ResponseEntity.status(500).body(ApiResponse.error("Error al procesar respuesta de pago: " + ex.getMessage()));
        }
    }

    /**
     * Endpoint GET para manejar redirecciones de métodos de pago como Cuotéalo
     * Niubiz puede hacer GET en algunos casos de redirección
     */
    @GetMapping("/payment-callback")
    public ResponseEntity<ApiResponse<Object>> paymentCallbackGet(
            @RequestParam("orderId") Long orderId,
            @RequestParam(value = "sessionId", required = false) String sessionId,
            @RequestParam(value = "transactionCode", required = false) String transactionCode,
            @RequestParam(value = "actionCode", required = false) String actionCode) {
        
        try {
            System.out.println("=== Payment Callback GET Received ===");
            System.out.println("Order ID: " + orderId);
            System.out.println("Session ID: " + sessionId);
            System.out.println("Transaction Code: " + transactionCode);
            System.out.println("Action Code: " + actionCode);
            
            // Crear un objeto de callback con los parámetros recibidos por GET
            PaymentCallbackRequest callbackData = new PaymentCallbackRequest();
            callbackData.setTransactionCode(transactionCode);
            callbackData.setActionCode(actionCode);
            callbackData.setPurchaseNumber(orderId.toString());
            
            // Procesar la respuesta del pago
            Map<String, Object> result = orderService.processPaymentCallback(orderId, callbackData);
            
            return ResponseEntity.ok(ApiResponse.success(result));
            
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        } catch (Exception ex) {
            System.err.println("Error processing payment callback GET: " + ex.getMessage());
            return ResponseEntity.status(500).body(ApiResponse.error("Error al procesar respuesta de pago: " + ex.getMessage()));
        }
    }

    /**
     * Endpoint para recibir el POST de Niubiz (VisanetCheckout action URL)
     * Niubiz envía los datos como application/x-www-form-urlencoded
     * Después de procesar, redirige al frontend con el resultado
     */
    @PostMapping(value = "/niubiz-callback", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Void> niubizCallback(
            @RequestParam("orderId") Long orderId,
            @RequestParam(value = "sessionId", required = false) String sessionId,
            @RequestParam(value = "transactionToken", required = false) String transactionToken,
            @RequestParam Map<String, String> allParams) {
        
        String frontendUrl = "http://localhost:4200/payment-response";
        
        try {
            System.out.println("=== Niubiz Callback POST (form-urlencoded) Received ===");
            System.out.println("Order ID: " + orderId);
            System.out.println("Session ID: " + sessionId);
            System.out.println("Transaction Token: " + transactionToken);
            System.out.println("All params: " + allParams);
            
            // Crear objeto de callback con el token recibido
            PaymentCallbackRequest callbackData = new PaymentCallbackRequest();
            callbackData.setTransactionToken(transactionToken);
            callbackData.setPurchaseNumber(orderId.toString());
            
            // Procesar la respuesta del pago
            Map<String, Object> result = orderService.processPaymentCallback(orderId, callbackData);
            
            // Determinar éxito basado en el nuevo estado
            String newStatus = (String) result.get("newStatus");
            boolean success = "completed".equals(newStatus) || "PAID".equals(newStatus);
            
            // Construir URL de redirección con todos los datos relevantes
            StringBuilder redirectUrl = new StringBuilder(frontendUrl);
            redirectUrl.append("?orderId=").append(orderId);
            redirectUrl.append("&success=").append(success);
            redirectUrl.append("&status=").append(newStatus != null ? newStatus : "unknown");
            
            if (result.get("authorizationCode") != null) {
                redirectUrl.append("&authorizationCode=").append(result.get("authorizationCode"));
            }
            if (result.get("transactionCode") != null) {
                redirectUrl.append("&transactionCode=").append(result.get("transactionCode"));
            }
            if (result.get("actionCode") != null) {
                redirectUrl.append("&actionCode=").append(result.get("actionCode"));
            }
            if (result.get("cardBrand") != null) {
                redirectUrl.append("&cardBrand=").append(
                    java.net.URLEncoder.encode((String) result.get("cardBrand"), java.nio.charset.StandardCharsets.UTF_8));
            }
            if (result.get("cardNumber") != null) {
                redirectUrl.append("&cardNumber=").append(
                    java.net.URLEncoder.encode((String) result.get("cardNumber"), java.nio.charset.StandardCharsets.UTF_8));
            }
            if (result.get("amount") != null) {
                redirectUrl.append("&amount=").append(result.get("amount"));
            }
            
            System.out.println("Redirecting to: " + redirectUrl.toString());
            
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create(redirectUrl.toString()));
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
            
        } catch (Exception ex) {
            System.err.println("Error processing Niubiz callback: " + ex.getMessage());
            ex.printStackTrace();
            
            String redirectUrl = String.format("%s?orderId=%d&success=false&error=%s",
                    frontendUrl, orderId, java.net.URLEncoder.encode(ex.getMessage(), java.nio.charset.StandardCharsets.UTF_8));
            
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create(redirectUrl));
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        }
    }

}


