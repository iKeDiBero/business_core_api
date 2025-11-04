package com.kedibero.business_core_api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
public class VersionController {

    @GetMapping("/version")
    public Map<String, Object> getVersion() {
        Map<String, Object> response = new HashMap<>();
        response.put("app_name", "Business Core API");
        response.put("version", "1.0.0");
        response.put("environment", "sandbox");
        response.put("developer", "Kevin Rodríguez");
        response.put("status", "running");
        return response;
    }
}

