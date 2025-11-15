package com.kedibero.business_core_api.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DashboardRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Map<String, Object> findDashboards() {
        
        Object[] result = (Object[]) entityManager.createNativeQuery(
                "SELECT " +
                        "  (SELECT COUNT(*) FROM brands WHERE is_active = true) AS brands_count, " +
                        "  (SELECT COUNT(*) FROM products WHERE is_active = true) AS products_count, " +
                        "  (SELECT COUNT(*) FROM models WHERE is_active = true) AS models_count")
                .getSingleResult();

        long brandsCount = ((Number) result[0]).longValue();
        long productsCount = ((Number) result[1]).longValue();
        long modelsCount = ((Number) result[2]).longValue();

        // Consulta con JOINs y agrupación
        List<Object[]> productJoinResults = entityManager.createNativeQuery(
                "SELECT " +
                        "p.name AS product_name, " +
                        "b.name AS brand_name, " +
                        "m.name AS model_name, " +
                        "SUM(p.stock) AS total_stock " +
                        "FROM products p " +
                        "INNER JOIN brands b ON p.brand_id = b.id " +
                        "INNER JOIN models m ON p.model_id = m.id " +
                        "WHERE p.is_active = TRUE " +
                        "GROUP BY p.name, b.name, m.name")
                .getResultList();

        List<Map<String, Object>> productDetails = productJoinResults.stream()
                .map(row -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("productName", row[0]);
                    map.put("brandName", row[1]);
                    map.put("modelName", row[2]);
                    map.put("totalStock", row[3]);
                    return map;
                })
                .toList();

        Map<String, Object> response = new HashMap<>();
        response.put("brandsCount", brandsCount);
        response.put("productsCount", productsCount);
        response.put("modelsCount", modelsCount);
        response.put("productDetails", productDetails);

        return response;
    }
}