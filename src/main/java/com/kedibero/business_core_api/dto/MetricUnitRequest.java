package com.kedibero.business_core_api.dto;

public class MetricUnitRequest {
    private String name;
    private String symbol;
    private String description;
    // Getters y Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}

