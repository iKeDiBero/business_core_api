package com.kedibero.business_core_api.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kedibero.business_core_api.dto.PaymentCallbackRequest;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class OrderService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private NiubizService niubizService;

    @Transactional
    public Object createOrderFromCart(String username) {
        // Obtener el usuario
        @SuppressWarnings("unchecked")
        List<Object> userResult = (List<Object>) entityManager.createNativeQuery(
                "SELECT id FROM users WHERE username = ?")
                .setParameter(1, username)
                .getResultList();

        if (userResult.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado.");
        }

        Long userId = ((Number) userResult.get(0)).longValue();

        // Obtener el carrito activo del usuario
        @SuppressWarnings("unchecked")
        List<Object> cartResult = (List<Object>) entityManager.createNativeQuery(
                "SELECT id FROM carts WHERE user_id = ? AND status = TRUE LIMIT 1")
                .setParameter(1, userId)
                .getResultList();

        if (cartResult.isEmpty()) {
            throw new IllegalArgumentException("El usuario no tiene un carrito activo.");
        }

        Long cartId = ((Number) cartResult.get(0)).longValue();

        // Obtener los items del carrito
        @SuppressWarnings("unchecked")
        List<Object[]> cartItems = (List<Object[]>) entityManager.createNativeQuery(
                "SELECT product_id, quantity, price FROM cart_items WHERE cart_id = ? AND status = TRUE")
                .setParameter(1, cartId)
                .getResultList();

        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException("El carrito está vacío.");
        }

        // Calcular el total
        double total = 0.0;
        for (Object[] item : cartItems) {
            int quantity = ((Number) item[1]).intValue();
            double price = ((Number) item[2]).doubleValue();
            total += quantity * price;
        }

        // Crear la orden
        entityManager.createNativeQuery(
                "INSERT INTO orders (user_id, total, status, created_at, updated_at) VALUES (?, ?, 'pending', NOW(), NOW())")
                .setParameter(1, userId)
                .setParameter(2, total)
                .executeUpdate();

        Long orderId = ((Number) entityManager.createNativeQuery("SELECT LAST_INSERT_ID()").getSingleResult())
                .longValue();

        // Copiar los items del carrito a la orden
        for (Object[] item : cartItems) {
            Long productId = ((Number) item[0]).longValue();
            int quantity = ((Number) item[1]).intValue();
            double price = ((Number) item[2]).doubleValue();

            entityManager.createNativeQuery(
                    "INSERT INTO order_items (order_id, product_id, quantity, price, created_at) VALUES (?, ?, ?, ?, NOW())")
                    .setParameter(1, orderId)
                    .setParameter(2, productId)
                    .setParameter(3, quantity)
                    .setParameter(4, price)
                    .executeUpdate();
        }

        // Limpiar el carrito (marcar items como inactivos y actualizar el carrito)
        entityManager.createNativeQuery(
                "UPDATE cart_items SET status = FALSE WHERE cart_id = ?")
                .setParameter(1, cartId)
                .executeUpdate();

        entityManager.createNativeQuery(
                "UPDATE carts SET status = FALSE, updated_at = NOW() WHERE id = ?")
                .setParameter(1, cartId)
                .executeUpdate();

        // Retornar la orden creada
        Map<String, Object> order = new HashMap<>();
        order.put("id", orderId);
        order.put("userId", userId);
        order.put("total", total);
        order.put("status", "pending");
        order.put("itemCount", cartItems.size());

        return order;
    }

    public Object getOrderById(Long id, String username) {
        // Obtener el usuario
        @SuppressWarnings("unchecked")
        List<Object> userResult = (List<Object>) entityManager.createNativeQuery(
                "SELECT id FROM users WHERE username = ?")
                .setParameter(1, username)
                .getResultList();

        if (userResult.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado.");
        }

        Long userId = ((Number) userResult.get(0)).longValue();

        // Obtener la orden solo si pertenece al usuario
        @SuppressWarnings("unchecked")
        List<Object> orderResult = (List<Object>) entityManager.createNativeQuery(
                "SELECT * FROM orders WHERE id = ? AND user_id = ?")
                .setParameter(1, id)
                .setParameter(2, userId)
                .getResultList();

        if (orderResult.isEmpty()) {
            throw new IllegalArgumentException("Orden no encontrada o no pertenece al usuario.");
        }

        return orderResult.get(0);
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getAllOrders(String username) {
        // Obtener el usuario
        List<Object> userResult = (List<Object>) entityManager.createNativeQuery(
                "SELECT id FROM users WHERE username = ?")
                .setParameter(1, username)
                .getResultList();

        if (userResult.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado.");
        }

        Long userId = ((Number) userResult.get(0)).longValue();

        // Obtener las órdenes del usuario
        List<Object[]> ordersResult = (List<Object[]>) entityManager.createNativeQuery(
                "SELECT id, user_id, total, status, created_at, updated_at FROM orders WHERE user_id = ? ORDER BY created_at DESC")
                .setParameter(1, userId)
                .getResultList();

        List<Map<String, Object>> orders = new java.util.ArrayList<>();

        for (Object[] orderRow : ordersResult) {
            Long orderId = ((Number) orderRow[0]).longValue();
            
        // Obtener los items de la orden con información del producto
            List<Object[]> itemsResult = (List<Object[]>) entityManager.createNativeQuery(
                    "SELECT oi.id, oi.product_id, oi.quantity, oi.price, " +
                    "p.name, p.description, p.sku " +
                    "FROM order_items oi " +
                    "INNER JOIN products p ON oi.product_id = p.id " +
                    "WHERE oi.order_id = ?")
                    .setParameter(1, orderId)
                    .getResultList();

            List<Map<String, Object>> items = new java.util.ArrayList<>();
            for (Object[] itemRow : itemsResult) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", ((Number) itemRow[0]).longValue());
                item.put("productId", ((Number) itemRow[1]).longValue());
                item.put("quantity", ((Number) itemRow[2]).intValue());
                item.put("price", ((Number) itemRow[3]).doubleValue());
                item.put("productName", itemRow[4]);
                item.put("productDescription", itemRow[5]);
                item.put("productSku", itemRow[6]);
                // item.put("productImageUrl", itemRow[7]);
                items.add(item);
            }

            Map<String, Object> order = new HashMap<>();
            order.put("id", ((Number) orderRow[0]).longValue());
            order.put("userId", ((Number) orderRow[1]).longValue());
            order.put("total", ((Number) orderRow[2]).doubleValue());
            order.put("status", orderRow[3]);
            order.put("createdAt", orderRow[4]);
            order.put("updatedAt", orderRow[5]);
            order.put("items", items);
            order.put("itemCount", items.size());

            orders.add(order);
        }

        return orders;
    }

    /**
     * Procesa la respuesta de callback de Niubiz
     * @param orderId ID de la orden
     * @param callbackData Datos del callback de Niubiz
     * @return Información de la orden actualizada
     */
    @Transactional
    @SuppressWarnings("unchecked")
    public Map<String, Object> processPaymentCallback(Long orderId, PaymentCallbackRequest callbackData) {
        // Verificar que la orden existe
        List<Object[]> orderResult = (List<Object[]>) entityManager.createNativeQuery(
                "SELECT id, user_id, total, status FROM orders WHERE id = ?")
                .setParameter(1, orderId)
                .getResultList();

        if (orderResult.isEmpty()) {
            throw new IllegalArgumentException("Orden no encontrada: " + orderId);
        }

        Object[] orderRow = orderResult.get(0);
        double orderTotal = ((Number) orderRow[2]).doubleValue();
        String currentStatus = (String) orderRow[3];

        String newStatus;
        String paymentMessage;
        boolean paymentSuccessful = false;
        String transactionCode = null;
        String authorizationCode = null;
        String actionCode = null;

        // Si tenemos un transactionToken, consultar a Niubiz por los detalles reales
        if (callbackData != null && callbackData.getTransactionToken() != null 
                && !callbackData.getTransactionToken().isEmpty()
                && !"null".equals(callbackData.getTransactionToken())) {
            
            System.out.println("=== Consulting Niubiz for transaction details ===");
            System.out.println("Transaction Token: " + callbackData.getTransactionToken());
            
            try {
                Map<String, Object> transactionDetails = niubizService.getTransactionStatus(
                    callbackData.getTransactionToken(),
                    orderId.toString(),
                    orderTotal
                );
                
                System.out.println("Transaction Details: " + transactionDetails);
                
                actionCode = (String) transactionDetails.get("actionCode");
                transactionCode = (String) transactionDetails.get("transactionId");
                authorizationCode = (String) transactionDetails.get("authorizationCode");
                String status = (String) transactionDetails.get("status");
                
                // Según documentación Niubiz:
                // - ActionCode "000" o "010" indica transacción autorizada
                // - STATUS "Authorized" confirma que la venta fue exitosa
                boolean isAuthorized = "Authorized".equals(status) || 
                                       "000".equals(actionCode) || 
                                       "010".equals(actionCode);
                
                if (isAuthorized) {
                    newStatus = "completed";
                    paymentMessage = (String) transactionDetails.get("actionDescription");
                    if (paymentMessage == null) {
                        paymentMessage = "Pago procesado exitosamente";
                    }
                    paymentSuccessful = true;
                } else {
                    newStatus = "payment_failed";
                    String description = (String) transactionDetails.get("actionDescription");
                    paymentMessage = description != null ? description : "Error en el pago: " + actionCode;
                }
                
                // Actualizar callbackData con la información real
                callbackData.setActionCode(actionCode);
                callbackData.setTransactionCode(transactionCode);
                callbackData.setAuthorizationCode(authorizationCode);
                callbackData.setCardBrand((String) transactionDetails.get("cardBrand"));
                callbackData.setCardNumber((String) transactionDetails.get("cardNumber"));
                
            } catch (Exception e) {
                System.err.println("Error consulting Niubiz: " + e.getMessage());
                newStatus = currentStatus;
                paymentMessage = "Error al consultar estado de pago: " + e.getMessage();
            }
        } else if (callbackData != null && callbackData.getActionCode() != null 
                && !callbackData.getActionCode().isEmpty()
                && !"null".equals(callbackData.getActionCode())) {
            // Si ya tenemos actionCode en los datos del callback
            actionCode = callbackData.getActionCode();
            transactionCode = callbackData.getTransactionCode();
            authorizationCode = callbackData.getAuthorizationCode();
            
            if ("000".equals(actionCode)) {
                newStatus = "completed";
                paymentMessage = "Pago procesado exitosamente";
                paymentSuccessful = true;
            } else {
                newStatus = "payment_failed";
                paymentMessage = callbackData.getActionDescription() != null 
                    ? callbackData.getActionDescription() 
                    : "Error en el pago: " + actionCode;
            }
        } else {
            // Si no hay datos de callback válidos, mantener el estado actual
            newStatus = currentStatus;
            paymentMessage = "Callback recibido sin datos de transacción válidos";
        }

        // Actualizar el estado de la orden
        entityManager.createNativeQuery(
                "UPDATE orders SET status = ?, updated_at = NOW() WHERE id = ?")
                .setParameter(1, newStatus)
                .setParameter(2, orderId)
                .executeUpdate();

        // Preparar respuesta
        Map<String, Object> result = new HashMap<>();
        result.put("orderId", orderId);
        result.put("previousStatus", currentStatus);
        result.put("newStatus", newStatus);
        result.put("status", newStatus);
        result.put("paymentSuccessful", paymentSuccessful);
        result.put("message", paymentMessage);
        result.put("transactionCode", transactionCode);
        result.put("authorizationCode", authorizationCode);
        result.put("actionCode", actionCode);
        result.put("amount", orderTotal);
        
        // Agregar datos de la tarjeta si están disponibles
        if (callbackData != null) {
            result.put("cardBrand", callbackData.getCardBrand());
            result.put("cardNumber", callbackData.getCardNumber());
        }

        // Guardar registro del pago (después de preparar el resultado)
        // Usamos una consulta separada y verificamos primero si la tabla existe
        if (transactionCode != null) {
            savePaymentLog(orderId, transactionCode, authorizationCode, actionCode, 
                          paymentMessage, orderTotal, 
                          callbackData != null ? callbackData.getCardBrand() : null,
                          callbackData != null ? callbackData.getCardNumber() : null);
        }

        return result;
    }

    /**
     * Guarda el log de pago de forma segura (no afecta la transacción principal si falla)
     */
    private void savePaymentLog(Long orderId, String transactionCode, String authorizationCode,
                                String actionCode, String actionDescription, Double amount,
                                String cardBrand, String cardNumber) {
        try {
            entityManager.createNativeQuery(
                "INSERT INTO payment_logs (order_id, transaction_code, authorization_code, action_code, " +
                "action_description, amount, card_brand, card_number, created_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW())")
                .setParameter(1, orderId)
                .setParameter(2, transactionCode)
                .setParameter(3, authorizationCode)
                .setParameter(4, actionCode)
                .setParameter(5, actionDescription)
                .setParameter(6, amount)
                .setParameter(7, cardBrand)
                .setParameter(8, cardNumber)
                .executeUpdate();
            System.out.println("Payment log saved successfully for order: " + orderId);
        } catch (Exception e) {
            // Solo logear el error, no afectar la transacción principal
            System.out.println("Warning: Could not save payment log (table may not exist): " + e.getMessage());
        }
    }
}
