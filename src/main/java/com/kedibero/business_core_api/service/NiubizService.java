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
}
