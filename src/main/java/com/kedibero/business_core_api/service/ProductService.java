package com.kedibero.business_core_api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kedibero.business_core_api.dto.ProductRequest;
import com.kedibero.business_core_api.dto.ProductResponse;
import com.kedibero.business_core_api.dto.SpecsDTO;
import com.kedibero.business_core_api.entity.Product;
import com.kedibero.business_core_api.repository.CategoryRepository;
import com.kedibero.business_core_api.repository.MetricUnitRepository;
import com.kedibero.business_core_api.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private MetricUnitRepository metricUnitRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    public ProductResponse createProduct(ProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setWeight(request.getWeight());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setBarcode(request.getBarcode());
        product.setImageBase64(request.getImageBase64());
        product.setIsActive(true);
        product.setSku(request.getSku());
        product.setBrand(request.getBrand());
        product.setModel(request.getModel());
        product.setProductCondition(request.getProductCondition());
        product.setImageUrl(request.getImageUrl());
        product.setPricePerMonth(request.getPricePerMonth());
        product.setDeviceId(request.getDeviceId());

        // Convertir SpecsDTO a JSON string
        if (request.getSpecs() != null) {
            try {
                product.setSpecs(objectMapper.writeValueAsString(request.getSpecs().getSpecs()));
            } catch (Exception e) {
                System.err.println("Error al serializar specs: " + e.getMessage());
            }
        }

        if (request.getCategoryId() != null) {
            categoryRepository.findById(request.getCategoryId()).ifPresent(product::setCategory);
        }
        if (request.getMetricUnitId() != null) {
            metricUnitRepository.findById(request.getMetricUnitId()).ifPresent(product::setMetricUnit);
        }
        Product saved = productRepository.save(product);
        return toResponse(saved);
    }

    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Optional<Product> optional = productRepository.findById(id);
        if (optional.isEmpty() || !optional.get().getIsActive()) return null;
        Product product = optional.get();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setWeight(request.getWeight());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setBarcode(request.getBarcode());
        product.setImageBase64(request.getImageBase64());
        product.setSku(request.getSku());
        product.setBrand(request.getBrand());
        product.setModel(request.getModel());
        product.setProductCondition(request.getProductCondition());
        product.setImageUrl(request.getImageUrl());
        product.setPricePerMonth(request.getPricePerMonth());
        product.setDeviceId(request.getDeviceId());

        // Convertir SpecsDTO a JSON string
        if (request.getSpecs() != null) {
            try {
                product.setSpecs(objectMapper.writeValueAsString(request.getSpecs().getSpecs()));
            } catch (Exception e) {
                System.err.println("Error al serializar specs: " + e.getMessage());
            }
        }

        if (request.getCategoryId() != null) {
            categoryRepository.findById(request.getCategoryId()).ifPresent(product::setCategory);
        }
        if (request.getMetricUnitId() != null) {
            metricUnitRepository.findById(request.getMetricUnitId()).ifPresent(product::setMetricUnit);
        }
        Product saved = productRepository.save(product);
        return toResponse(saved);
    }

    public boolean deleteProduct(Long id) {
        Optional<Product> optional = productRepository.findById(id);
        if (optional.isEmpty() || !optional.get().getIsActive()) return false;
        Product product = optional.get();
        product.setIsActive(false);
        productRepository.save(product);
        return true;
    }

    public List<ProductResponse> listProducts() {
        return productRepository.findByIsActiveTrue()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ProductResponse getProduct(Long id) {
        Optional<Product> optional = productRepository.findById(id);
        if (optional.isEmpty() || !optional.get().getIsActive()) return null;
        return toResponse(optional.get());
    }

    private ProductResponse toResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setWeight(product.getWeight());
        response.setPrice(product.getPrice());
        response.setStock(product.getStock());
        response.setBarcode(product.getBarcode());
        response.setImageBase64(product.getImageBase64());
        response.setIsActive(product.getIsActive());
        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());
        response.setSku(product.getSku());
        response.setBrand(product.getBrand());
        response.setModel(product.getModel());
        response.setProductCondition(product.getProductCondition());
        response.setImageUrl(product.getImageUrl());
        response.setPricePerMonth(product.getPricePerMonth());
        response.setDeviceId(product.getDeviceId());

        // Convertir JSON string a SpecsDTO
        if (product.getSpecs() != null) {
            try {
                @SuppressWarnings("unchecked")
                java.util.Map<String, Object> specsMap = objectMapper.readValue(product.getSpecs(), java.util.Map.class);
                SpecsDTO specsDTOwrapper = new SpecsDTO();
                specsDTOwrapper.setSpecs(specsMap);
                response.setSpecs(specsDTOwrapper);
            } catch (Exception e) {
                System.err.println("Error al deserializar specs: " + e.getMessage());
            }
        }

        if (product.getCategory() != null) {
            response.setCategoryId(product.getCategory().getId());
            response.setCategoryName(product.getCategory().getName());
        }
        if (product.getMetricUnit() != null) {
            response.setMetricUnitId(product.getMetricUnit().getId());
            response.setMetricUnitName(product.getMetricUnit().getName());
        }
        return response;
    }
}


