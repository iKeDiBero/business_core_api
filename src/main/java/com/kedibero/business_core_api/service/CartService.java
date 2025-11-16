package com.kedibero.business_core_api.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import com.kedibero.business_core_api.dto.CartRequest;

// import java.time.LocalDateTime;

@Service
public class CartService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Long createCart(CartRequest cartRequest, String username) {
        // Obtener el id del usuario a partir del username
        List<?> userResult = entityManager.createNativeQuery(
                "SELECT id FROM users WHERE username = ?")
                .setParameter(1, username)
                .getResultList();

        if (userResult.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado.");
        }

        Long userId = ((Number) userResult.get(0)).longValue();

        // Verificar si el usuario ya tiene un carrito activo
        List<?> existingCarts = entityManager.createNativeQuery(
                "SELECT id FROM carts WHERE user_id = ? AND status = TRUE LIMIT 1")
                .setParameter(1, userId)
                .getResultList();

        if (!existingCarts.isEmpty()) {
            throw new IllegalArgumentException("El usuario ya tiene un carrito activo.");
        }

        entityManager.createNativeQuery(
                "INSERT INTO carts (user_id, status, created_at, updated_at) VALUES (?, ?, NOW(), NOW())")
                .setParameter(1, userId)
                .setParameter(2, cartRequest.getStatus() != null ? cartRequest.getStatus() : Boolean.TRUE)
                .executeUpdate();

        Long cartId = ((Number) entityManager.createNativeQuery("SELECT LAST_INSERT_ID()").getSingleResult())
                .longValue();

        for (CartRequest.CartItemRequest item : cartRequest.getItems()) {
            List<?> priceResult = entityManager.createNativeQuery(
                    "SELECT price FROM products WHERE id = ?")
                    .setParameter(1, item.getProductId())
                    .getResultList();

            if (priceResult.isEmpty()) {
                throw new IllegalArgumentException("El producto con id " + item.getProductId() + " no existe.");
            }

            Double price = ((Number) priceResult.get(0)).doubleValue();

            entityManager.createNativeQuery(
                    "INSERT INTO cart_items (cart_id, product_id, quantity, price, created_at) VALUES (?, ?, ?, ?, NOW())")
                    .setParameter(1, cartId)
                    .setParameter(2, item.getProductId())
                    .setParameter(3, item.getQuantity())
                    .setParameter(4, price)
                    .executeUpdate();
        }

        return cartId;
    }

    @Transactional
    public Long updateCart(Long cartId, CartRequest cartRequest, String username) {
        // Validar que el carrito existe y pertenece al usuario autenticado
        List<?> cartOwner = entityManager.createNativeQuery(
                "SELECT c.id FROM carts c JOIN users u ON c.user_id = u.id WHERE c.id = ? AND u.username = ?")
                .setParameter(1, cartId)
                .setParameter(2, username)
                .getResultList();

        if (cartOwner.isEmpty()) {
            throw new IllegalArgumentException("El carrito no existe o no pertenece al usuario autenticado.");
        }

        entityManager.createNativeQuery(
                "UPDATE carts SET status = ?, updated_at = NOW() WHERE id = ?")
                .setParameter(1, cartRequest.getStatus())
                .setParameter(2, cartId)
                .executeUpdate();

        entityManager.createNativeQuery(
                "UPDATE cart_items SET status = FALSE WHERE cart_id = ? AND status = TRUE")
                .setParameter(1, cartId)
                .executeUpdate();

        for (CartRequest.CartItemRequest item : cartRequest.getItems()) {
            List<?> priceResult = entityManager.createNativeQuery(
                    "SELECT price FROM products WHERE id = ?")
                    .setParameter(1, item.getProductId())
                    .getResultList();

            if (priceResult.isEmpty()) {
                throw new IllegalArgumentException("El producto con id " + item.getProductId() + " no existe.");
            }

            Double price = ((Number) priceResult.get(0)).doubleValue();

            entityManager.createNativeQuery(
                    "INSERT INTO cart_items (cart_id, product_id, quantity, price, created_at, status) VALUES (?, ?, ?, ?, NOW(), TRUE)")
                    .setParameter(1, cartId)
                    .setParameter(2, item.getProductId())
                    .setParameter(3, item.getQuantity())
                    .setParameter(4, price)
                    .executeUpdate();
        }
        return cartId;
    }

    public Object getCartByUsername(String username) {
        try {
            List<?> userResult = entityManager.createNativeQuery(
                    "SELECT id FROM users WHERE username = ?")
                    .setParameter(1, username)
                    .getResultList();

            if (userResult.isEmpty()) {
                return null;
            }

            Long userId = ((Number) userResult.get(0)).longValue();
            return getCartByUserId(userId);
        } catch (Exception ex) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", true);
            error.put("message", ex.getMessage());
            return error;
        }
    }

    public Object getCartByUserId(Long userId) {
        try {
            List<Object[]> carts = entityManager.createNativeQuery(
                    "SELECT id, user_id, status, created_at, updated_at FROM carts WHERE user_id = ? AND status = TRUE ORDER BY created_at DESC LIMIT 1")
                    .setParameter(1, userId).getResultList();

            if (carts.isEmpty()) {
                return null;
            }

            Object[] cartRow = carts.get(0);
            Map<String, Object> cart = new HashMap<>();
            cart.put("id", cartRow[0]);
            cart.put("userId", cartRow[1]);
            cart.put("status", cartRow[2]);
            cart.put("createdAt", cartRow[3]);
            cart.put("updatedAt", cartRow[4]);

            List<Object[]> items = entityManager.createNativeQuery(
                    "SELECT product_id, quantity, price, created_at FROM cart_items WHERE cart_id = ? AND status = TRUE")
                    .setParameter(1, cartRow[0]).getResultList();

            List<Map<String, Object>> itemList = items.stream().map(row -> {
                Map<String, Object> item = new HashMap<>();
                item.put("productId", row[0]);
                item.put("quantity", row[1]);
                item.put("price", row[2]);
                item.put("createdAt", row[3]);
                return item;
            }).toList();

            cart.put("items", itemList);

            return cart;
        } catch (Exception ex) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", true);
            error.put("message", ex.getMessage());
            return error;
        }
    }
}