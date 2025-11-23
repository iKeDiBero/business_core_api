package com.kedibero.business_core_api.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class OrderService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private CartService cartService;

    @Transactional
    public Object createOrderFromCart(String username) {
        // Obtener el usuario
        List<?> userResult = entityManager.createNativeQuery(
                "SELECT id FROM users WHERE username = ?")
                .setParameter(1, username)
                .getResultList();

        if (userResult.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado.");
        }

        Long userId = ((Number) userResult.get(0)).longValue();

        // Obtener el carrito activo del usuario
        List<?> cartResult = entityManager.createNativeQuery(
                "SELECT id FROM carts WHERE user_id = ? AND status = TRUE LIMIT 1")
                .setParameter(1, userId)
                .getResultList();

        if (cartResult.isEmpty()) {
            throw new IllegalArgumentException("El usuario no tiene un carrito activo.");
        }

        Long cartId = ((Number) cartResult.get(0)).longValue();

        // Obtener los items del carrito
        List<Object[]> cartItems = entityManager.createNativeQuery(
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
}
