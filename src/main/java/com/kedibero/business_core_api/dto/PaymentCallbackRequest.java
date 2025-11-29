package com.kedibero.business_core_api.dto;

/**
 * DTO para recibir la respuesta de pago de Niubiz
 * Contiene los campos que Niubiz envía en el POST de callback
 */
public class PaymentCallbackRequest {
    
    private String transactionToken;
    private String transactionCode;
    private String merchantId;
    private String purchaseNumber;
    private String amount;
    private String currency;
    private String authorizationCode;
    private String actionCode;
    private String actionDescription;
    private String errorCode;
    private String errorMessage;
    private String traceNumber;
    private String transactionDate;
    private String transactionTime;
    private String cardType;
    private String cardBrand;
    private String cardNumber; // Masked card number
    private String installmentsNumber;
    private String signatureValue;
    
    // Constructor por defecto
    public PaymentCallbackRequest() {}
    
    // Getters y Setters
    public String getTransactionToken() {
        return transactionToken;
    }
    
    public void setTransactionToken(String transactionToken) {
        this.transactionToken = transactionToken;
    }
    
    public String getTransactionCode() {
        return transactionCode;
    }
    
    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }
    
    public String getMerchantId() {
        return merchantId;
    }
    
    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }
    
    public String getPurchaseNumber() {
        return purchaseNumber;
    }
    
    public void setPurchaseNumber(String purchaseNumber) {
        this.purchaseNumber = purchaseNumber;
    }
    
    public String getAmount() {
        return amount;
    }
    
    public void setAmount(String amount) {
        this.amount = amount;
    }
    
    public String getCurrency() {
        return currency;
    }
    
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    
    public String getAuthorizationCode() {
        return authorizationCode;
    }
    
    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }
    
    public String getActionCode() {
        return actionCode;
    }
    
    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }
    
    public String getActionDescription() {
        return actionDescription;
    }
    
    public void setActionDescription(String actionDescription) {
        this.actionDescription = actionDescription;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    public String getTraceNumber() {
        return traceNumber;
    }
    
    public void setTraceNumber(String traceNumber) {
        this.traceNumber = traceNumber;
    }
    
    public String getTransactionDate() {
        return transactionDate;
    }
    
    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }
    
    public String getTransactionTime() {
        return transactionTime;
    }
    
    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }
    
    public String getCardType() {
        return cardType;
    }
    
    public void setCardType(String cardType) {
        this.cardType = cardType;
    }
    
    public String getCardBrand() {
        return cardBrand;
    }
    
    public void setCardBrand(String cardBrand) {
        this.cardBrand = cardBrand;
    }
    
    public String getCardNumber() {
        return cardNumber;
    }
    
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
    
    public String getInstallmentsNumber() {
        return installmentsNumber;
    }
    
    public void setInstallmentsNumber(String installmentsNumber) {
        this.installmentsNumber = installmentsNumber;
    }
    
    public String getSignatureValue() {
        return signatureValue;
    }
    
    public void setSignatureValue(String signatureValue) {
        this.signatureValue = signatureValue;
    }
    
    @Override
    public String toString() {
        return "PaymentCallbackRequest{" +
                "transactionCode='" + transactionCode + '\'' +
                ", purchaseNumber='" + purchaseNumber + '\'' +
                ", amount='" + amount + '\'' +
                ", actionCode='" + actionCode + '\'' +
                ", actionDescription='" + actionDescription + '\'' +
                '}';
    }
}
