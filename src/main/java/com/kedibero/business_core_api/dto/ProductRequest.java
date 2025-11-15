package com.kedibero.business_core_api.dto;

public class ProductRequest {
    private String name;
    private String description;
    private Double weight;
    private Double price;
    private Integer stock;
    private Long categoryId;
    private String barcode;
    private String imageBase64;
    private Long metricUnitId;
    private String sku;
    private Long brandId;
    private Long modelId;
    private String productCondition;
    private SpecsDTO specs;
    private Double pricePerMonth;
    private String deviceId;

    // Getters y Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public Long getMetricUnitId() { return metricUnitId; }
    public void setMetricUnitId(Long metricUnitId) { this.metricUnitId = metricUnitId; }
    public String getBarcode() { return barcode; }
    public void setBarcode(String barcode) { this.barcode = barcode; }
    public String getImageBase64() { return imageBase64; }
    public void setImageBase64(String imageBase64) { this.imageBase64 = imageBase64; }
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    public Long getBrandId() { return brandId; }
    public void setBrandId(Long brandId) { this.brandId = brandId; }
    public Long getModelId() { return modelId; }
    public void setModelId(Long modelId) { this.modelId = modelId; }
    public String getProductCondition() { return productCondition; }
    public void setProductCondition(String productCondition) { this.productCondition = productCondition; }
    public SpecsDTO getSpecs() { return specs; }
    public void setSpecs(SpecsDTO specs) { this.specs = specs; }
    public Double getPricePerMonth() { return pricePerMonth; }
    public void setPricePerMonth(Double pricePerMonth) { this.pricePerMonth = pricePerMonth; }
    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }
}

