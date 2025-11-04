package com.kedibero.business_core_api.controller;

import com.kedibero.business_core_api.repository.DbConnectionTestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
public class DbStatusController {
    @Autowired
    private DbConnectionTestRepository dbConnectionTestRepository;

    @GetMapping("/db-connection-status")
    public Map<String, Object> dbConnectionStatus() {
        Map<String, Object> result = new HashMap<>();
        try {
            long count = dbConnectionTestRepository.count();
            result.put("status", "OK");
            result.put("message", "Conexión a MySQL establecida correctamente");
            result.put("records_count", count);
        } catch (Exception e) {
            result.put("status", "ERROR");
            result.put("message", "Error al conectar a MySQL");
            result.put("error", e.getMessage());
        }
        return result;
    }
}

