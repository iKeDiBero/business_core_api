package com.kedibero.business_core_api.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class NiubizService {

    @Value("${niubiz.merchant.id:456879852}")
    private String merchantId;

    @Value("${niubiz.api.username:integraciones@niubiz.com.pe}")
    private String apiUsername;

    @Value("${niubiz.api.password:_7z3@8fF}")
    private String apiPassword;

    @Value("${niubiz.api.url:https://apisandbox.vnforappstest.com}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Obtiene el token de autorización de Niubiz
     * @return Token de autorización
     */
    private String getAuthorizationToken() {
        try {
            String url = apiUrl + "/api.security/v1/security";

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Basic Z2lhbmNhZ2FsbGFyZG9AZ21haWwuY29tOkF2MyR0cnV6");

            HttpEntity<String> request = new HttpEntity<>(headers);

            // La API retorna el token directamente como text/plain
            ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                String.class
            );

            if (response.getBody() != null && !response.getBody().isEmpty()) {
                // La respuesta es directamente el token, no un JSON
                return response.getBody().trim();
            }

            throw new RuntimeException("No se pudo obtener el token de autorización");

        } catch (Exception e) {
            throw new RuntimeException("Error al obtener token de autorización: " + e.getMessage(), e);
        }
    }

    /**
     * Genera un token de sesión para Niubiz
     * @param amount Monto de la transacción
     * @return Token de sesión
     */
    public String generateSessionToken(double amount) {
        try {
            // Paso 1: Obtener token de autorización
            String authToken = getAuthorizationToken();

            // Paso 2: Generar token de sesión
            String url = apiUrl + "/api.ecommerce/v2/ecommerce/token/session/" + merchantId;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", authToken);
            headers.set("Host", "vnforappstest.com");

            // Crear body del request
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("channel", "web");
            requestBody.put("amount", amount);

            // Antifraud data
            Map<String, Object> antifraud = new HashMap<>();
            antifraud.put("clientIp", "24.252.107.29");
            
            Map<String, Object> merchantDefineData = new HashMap<>();
            merchantDefineData.put("MDD4", "integraciones@niubiz.com.pe");
            merchantDefineData.put("MDD32", "JD1892639123");
            merchantDefineData.put("MDD75", "Registrado");
            merchantDefineData.put("MDD77", 458);
            antifraud.put("merchantDefineData", merchantDefineData);
            
            requestBody.put("antifraud", antifraud);

            // DataMap
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("cardholderCity", "Lima");
            dataMap.put("cardholderCountry", "PE");
            dataMap.put("cardholderAddress", "Av Jose Pardo 831");
            dataMap.put("cardholderPostalCode", "12345");
            dataMap.put("cardholderState", "LIM");
            dataMap.put("cardholderPhoneNumber", "987654321");
            requestBody.put("dataMap", dataMap);

            String jsonBody = objectMapper.writeValueAsString(requestBody);
            HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);

            // Hacer la petición
            ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                Map.class
            );

            if (response.getBody() != null && response.getBody().containsKey("sessionKey")) {
                return (String) response.getBody().get("sessionKey");
            }

            throw new RuntimeException("No se pudo obtener el token de sesión");

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Error al generar token de sesión: " + e.getMessage(), e);
        }
    }

    public String getMerchantId() {
        return merchantId;
    }

    /**
     * Solicita la autorización de una transacción usando el transactionToken
     * Este método debe llamarse después de que el usuario completa el pago en el formulario
     * 
     * Endpoint: POST /api.authorization/v3/authorization/ecommerce/{merchantId}
     * 
     * Request ejemplo:
     * {
     *   "channel": "web",
     *   "captureType": "manual",
     *   "countable": true,
     *   "order": {
     *     "tokenId": "99E9BF92C69A4799A9BF92C69AF79979",
     *     "purchaseNumber": 2020100901,
     *     "amount": 10.5,
     *     "currency": "PEN"
     *   }
     * }
     * 
     * @param transactionToken Token de la transacción devuelto por el formulario de Niubiz (tokenId)
     * @param purchaseNumber Número de compra (orderId)
     * @param amount Monto de la transacción
     * @return Mapa con los detalles de la transacción
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getTransactionStatus(String transactionToken, String purchaseNumber, double amount) {
        try {
            // Obtener token de autorización (Access Token)
            String authToken = getAuthorizationToken();

            // URL para solicitar la autorización de la transacción
            String url = apiUrl + "/api.authorization/v3/authorization/ecommerce/" + merchantId;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", authToken);

            // Crear body del request según ejemplo de documentación de Niubiz
            // Solo channel, captureType, countable y order son necesarios
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("channel", "web");
            requestBody.put("captureType", "manual");
            requestBody.put("countable", true); // true = Liquidación automática
            
            // Objeto order (obligatorio)
            Map<String, Object> order = new HashMap<>();
            order.put("tokenId", transactionToken);
            order.put("purchaseNumber", purchaseNumber);
            order.put("amount", amount);
            order.put("currency", "PEN");
            requestBody.put("order", order);

            String jsonBody = objectMapper.writeValueAsString(requestBody);
            System.out.println("=== Niubiz Authorization Request ===");
            System.out.println("URL: " + url);
            System.out.println("Authorization: " + authToken.substring(0, Math.min(50, authToken.length())) + "...");
            System.out.println("Body: " + jsonBody);

            HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                Map.class
            );

            System.out.println("=== Niubiz Authorization Response ===");
            System.out.println("Status: " + response.getStatusCode());
            System.out.println("Response: " + response.getBody());

            if (response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                Map<String, Object> result = new HashMap<>();
                
                // Extraer datos del dataMap de la respuesta
                Map<String, Object> responseDataMap = (Map<String, Object>) responseBody.get("dataMap");
                if (responseDataMap != null) {
                    // Campos importantes para determinar éxito/fallo
                    result.put("actionCode", responseDataMap.get("ACTION_CODE"));
                    result.put("status", responseDataMap.get("STATUS")); // "Authorized" = éxito
                    result.put("actionDescription", responseDataMap.get("ACTION_DESCRIPTION"));
                    result.put("authorizationCode", responseDataMap.get("AUTHORIZATION_CODE"));
                    result.put("transactionId", responseDataMap.get("TRANSACTION_ID"));
                    result.put("traceNumber", responseDataMap.get("TRACE_NUMBER"));
                    result.put("cardBrand", responseDataMap.get("BRAND"));
                    result.put("cardNumber", responseDataMap.get("CARD"));
                    result.put("cardType", responseDataMap.get("CARD_TYPE"));
                    result.put("eciDescription", responseDataMap.get("ECI_DESCRIPTION"));
                    result.put("signature", responseDataMap.get("SIGNATURE"));
                    result.put("amount", responseDataMap.get("AMOUNT"));
                    result.put("currency", responseDataMap.get("CURRENCY"));
                }
                
                // Extraer datos del objeto order de la respuesta
                Map<String, Object> orderResponse = (Map<String, Object>) responseBody.get("order");
                if (orderResponse != null) {
                    result.put("authorizedAmount", orderResponse.get("authorizedAmount"));
                    result.put("installment", orderResponse.get("installment"));
                    // Si no hay actionCode en dataMap, intentar obtenerlo de order
                    if (result.get("actionCode") == null) {
                        result.put("actionCode", orderResponse.get("actionCode"));
                    }
                    if (result.get("authorizationCode") == null) {
                        result.put("authorizationCode", orderResponse.get("authorizationCode"));
                    }
                }
                
                // Extraer datos del fulfillment
                Map<String, Object> fulfillment = (Map<String, Object>) responseBody.get("fulfillment");
                if (fulfillment != null && result.get("signature") == null) {
                    result.put("signature", fulfillment.get("signature"));
                }
                
                result.put("transactionToken", transactionToken);
                result.put("purchaseNumber", purchaseNumber);
                result.put("requestedAmount", amount);
                
                return result;
            }

            throw new RuntimeException("No se pudo obtener el estado de la transacción - respuesta vacía");

        } catch (org.springframework.web.client.HttpClientErrorException e) {
            System.err.println("=== Niubiz Authorization Error ===");
            System.err.println("HTTP Status: " + e.getStatusCode());
            System.err.println("Response body: " + e.getResponseBodyAsString());
            
            // Intentar parsear el error de Niubiz
            Map<String, Object> errorResult = new HashMap<>();
            try {
                Map<String, Object> errorBody = objectMapper.readValue(e.getResponseBodyAsString(), Map.class);
                errorResult.put("actionCode", "ERROR");
                errorResult.put("status", "Not Authorized");
                
                // Para errores 400, el mensaje está en errorMessage
                if (errorBody.containsKey("errorMessage")) {
                    errorResult.put("actionDescription", errorBody.get("errorMessage"));
                    errorResult.put("errorCode", errorBody.get("errorCode"));
                } 
                // Para errores 401, 406, 500 el mensaje está en description
                else if (errorBody.containsKey("description")) {
                    errorResult.put("actionDescription", errorBody.get("description"));
                }
                
            } catch (Exception parseEx) {
                errorResult.put("actionCode", "ERROR");
                errorResult.put("status", "Not Authorized");
                errorResult.put("actionDescription", "Error HTTP " + e.getStatusCode() + ": " + e.getMessage());
            }
            errorResult.put("transactionToken", transactionToken);
            return errorResult;
            
        } catch (Exception e) {
            System.err.println("=== Niubiz Authorization Exception ===");
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            
            // Retornar un resultado de error
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("actionCode", "ERROR");
            errorResult.put("status", "Not Authorized");
            errorResult.put("actionDescription", "Error al autorizar transacción: " + e.getMessage());
            errorResult.put("transactionToken", transactionToken);
            return errorResult;
        }
    }
}
