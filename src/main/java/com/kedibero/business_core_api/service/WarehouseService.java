package com.kedibero.business_core_api.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class WarehouseService {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Obtiene el resumen del almacén del usuario (productos de órdenes completadas)
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getWarehouseSummary(String username) {
        // Obtener el usuario
        List<Object> userResult = entityManager.createNativeQuery(
                "SELECT id FROM users WHERE username = ?")
                .setParameter(1, username)
                .getResultList();

        if (userResult.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado.");
        }

        Long userId = ((Number) userResult.get(0)).longValue();

        // Contar productos totales en el almacén (de órdenes completadas)
        Object totalProductsResult = entityManager.createNativeQuery(
                "SELECT COALESCE(SUM(oi.quantity), 0) " +
                "FROM order_items oi " +
                "INNER JOIN orders o ON oi.order_id = o.id " +
                "WHERE o.user_id = ? AND o.status = 'completed'")
                .setParameter(1, userId)
                .getSingleResult();
        
        long totalProducts = ((Number) totalProductsResult).longValue();

        // Contar productos únicos
        Object uniqueProductsResult = entityManager.createNativeQuery(
                "SELECT COUNT(DISTINCT oi.product_id) " +
                "FROM order_items oi " +
                "INNER JOIN orders o ON oi.order_id = o.id " +
                "WHERE o.user_id = ? AND o.status = 'completed'")
                .setParameter(1, userId)
                .getSingleResult();
        
        long uniqueProducts = ((Number) uniqueProductsResult).longValue();

        // Valor total del inventario
        Object totalValueResult = entityManager.createNativeQuery(
                "SELECT COALESCE(SUM(oi.quantity * oi.price), 0) " +
                "FROM order_items oi " +
                "INNER JOIN orders o ON oi.order_id = o.id " +
                "WHERE o.user_id = ? AND o.status = 'completed'")
                .setParameter(1, userId)
                .getSingleResult();
        
        double totalValue = ((Number) totalValueResult).doubleValue();

        // Contar órdenes completadas
        Object completedOrdersResult = entityManager.createNativeQuery(
                "SELECT COUNT(*) FROM orders WHERE user_id = ? AND status = 'completed'")
                .setParameter(1, userId)
                .getSingleResult();
        
        long completedOrders = ((Number) completedOrdersResult).longValue();

        // Productos agrupados por categoría
        List<Object[]> categoryStats = entityManager.createNativeQuery(
                "SELECT c.name, COUNT(DISTINCT oi.product_id), SUM(oi.quantity) " +
                "FROM order_items oi " +
                "INNER JOIN orders o ON oi.order_id = o.id " +
                "INNER JOIN products p ON oi.product_id = p.id " +
                "LEFT JOIN categories c ON p.category_id = c.id " +
                "WHERE o.user_id = ? AND o.status = 'completed' " +
                "GROUP BY c.id, c.name " +
                "ORDER BY SUM(oi.quantity) DESC")
                .setParameter(1, userId)
                .getResultList();

        List<Map<String, Object>> categorySummary = new ArrayList<>();
        for (Object[] row : categoryStats) {
            Map<String, Object> cat = new HashMap<>();
            cat.put("categoryName", row[0] != null ? row[0] : "Sin categoría");
            cat.put("uniqueProducts", ((Number) row[1]).longValue());
            cat.put("totalQuantity", ((Number) row[2]).longValue());
            categorySummary.add(cat);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("totalProducts", totalProducts);
        response.put("uniqueProducts", uniqueProducts);
        response.put("totalValue", totalValue);
        response.put("completedOrders", completedOrders);
        response.put("categorySummary", categorySummary);

        return response;
    }

    /**
     * Obtiene todos los productos del almacén del usuario con detalles
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getWarehouseProducts(String username) {
        // Obtener el usuario
        List<Object> userResult = entityManager.createNativeQuery(
                "SELECT id FROM users WHERE username = ?")
                .setParameter(1, username)
                .getResultList();

        if (userResult.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado.");
        }

        Long userId = ((Number) userResult.get(0)).longValue();

        // Obtener productos agrupados con información detallada
        List<Object[]> productsResult = entityManager.createNativeQuery(
                "SELECT " +
                "  p.id, " +
                "  p.name, " +
                "  p.description, " +
                "  p.sku, " +
                "  p.image_base64, " +
                "  c.name AS category_name, " +
                "  b.name AS brand_name, " +
                "  m.name AS model_name, " +
                "  SUM(oi.quantity) AS total_quantity, " +
                "  AVG(oi.price) AS avg_price, " +
                "  SUM(oi.quantity * oi.price) AS total_value, " +
                "  MIN(o.created_at) AS first_purchase, " +
                "  MAX(o.created_at) AS last_purchase " +
                "FROM order_items oi " +
                "INNER JOIN orders o ON oi.order_id = o.id " +
                "INNER JOIN products p ON oi.product_id = p.id " +
                "LEFT JOIN categories c ON p.category_id = c.id " +
                "LEFT JOIN brands b ON p.brand_id = b.id " +
                "LEFT JOIN models m ON p.model_id = m.id " +
                "WHERE o.user_id = ? AND o.status = 'completed' " +
                "GROUP BY p.id, p.name, p.description, p.sku, p.image_base64, c.name, b.name, m.name " +
                "ORDER BY total_quantity DESC")
                .setParameter(1, userId)
                .getResultList();

        List<Map<String, Object>> products = new ArrayList<>();
        for (Object[] row : productsResult) {
            Map<String, Object> product = new HashMap<>();
            product.put("productId", ((Number) row[0]).longValue());
            product.put("name", row[1]);
            product.put("description", row[2]);
            product.put("sku", row[3]);
            product.put("imageBase64", row[4]);
            product.put("categoryName", row[5] != null ? row[5] : "Sin categoría");
            product.put("brandName", row[6] != null ? row[6] : "Sin marca");
            product.put("modelName", row[7] != null ? row[7] : "Sin modelo");
            product.put("quantity", ((Number) row[8]).longValue());
            product.put("avgPrice", ((Number) row[9]).doubleValue());
            product.put("totalValue", ((Number) row[10]).doubleValue());
            product.put("firstPurchase", row[11]);
            product.put("lastPurchase", row[12]);
            products.add(product);
        }

        return products;
    }

    /**
     * Obtiene el historial de compras de un producto específico
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getProductPurchaseHistory(String username, Long productId) {
        // Obtener el usuario
        List<Object> userResult = entityManager.createNativeQuery(
                "SELECT id FROM users WHERE username = ?")
                .setParameter(1, username)
                .getResultList();

        if (userResult.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado.");
        }

        Long userId = ((Number) userResult.get(0)).longValue();

        List<Object[]> historyResult = entityManager.createNativeQuery(
                "SELECT " +
                "  o.id AS order_id, " +
                "  oi.quantity, " +
                "  oi.price, " +
                "  o.created_at, " +
                "  o.status " +
                "FROM order_items oi " +
                "INNER JOIN orders o ON oi.order_id = o.id " +
                "WHERE o.user_id = ? AND oi.product_id = ? AND o.status = 'completed' " +
                "ORDER BY o.created_at DESC")
                .setParameter(1, userId)
                .setParameter(2, productId)
                .getResultList();

        List<Map<String, Object>> history = new ArrayList<>();
        for (Object[] row : historyResult) {
            Map<String, Object> entry = new HashMap<>();
            entry.put("orderId", ((Number) row[0]).longValue());
            entry.put("quantity", ((Number) row[1]).intValue());
            entry.put("price", ((Number) row[2]).doubleValue());
            entry.put("purchaseDate", row[3]);
            entry.put("status", row[4]);
            history.add(entry);
        }

        return history;
    }
}
