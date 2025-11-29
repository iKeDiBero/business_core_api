package com.kedibero.business_core_api.dto;

import java.time.LocalDateTime;

public class TicketResponse {
    
    private Long id;
    private Long productId;
    private String productName;
    private String productSku;
    private String subject;
    private String description;
    private String status;
    private String statusLabel;
    private LocalDateTime createdAt;
    private LocalDateTime resolvedAt;
    private LocalDateTime scheduledResolutionAt;
    private Long remainingSeconds;
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getProductId() {
        return productId;
    }
    
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    
    public String getProductName() {
        return productName;
    }
    
    public void setProductName(String productName) {
        this.productName = productName;
    }
    
    public String getProductSku() {
        return productSku;
    }
    
    public void setProductSku(String productSku) {
        this.productSku = productSku;
    }
    
    public String getSubject() {
        return subject;
    }
    
    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getStatusLabel() {
        return statusLabel;
    }
    
    public void setStatusLabel(String statusLabel) {
        this.statusLabel = statusLabel;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getResolvedAt() {
        return resolvedAt;
    }
    
    public void setResolvedAt(LocalDateTime resolvedAt) {
        this.resolvedAt = resolvedAt;
    }
    
    public LocalDateTime getScheduledResolutionAt() {
        return scheduledResolutionAt;
    }
    
    public void setScheduledResolutionAt(LocalDateTime scheduledResolutionAt) {
        this.scheduledResolutionAt = scheduledResolutionAt;
    }
    
    public Long getRemainingSeconds() {
        return remainingSeconds;
    }
    
    public void setRemainingSeconds(Long remainingSeconds) {
        this.remainingSeconds = remainingSeconds;
    }
}
