package com.kedibero.business_core_api.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class OrderService {

    @PersistenceContext
    private EntityManager entityManager;

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
}
