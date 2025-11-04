package com.kedibero.business_core_api.controller;

import com.kedibero.business_core_api.dto.MetricUnitRequest;
import com.kedibero.business_core_api.dto.MetricUnitResponse;
import com.kedibero.business_core_api.dto.ApiResponse;
import com.kedibero.business_core_api.service.MetricUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/metric-units")
public class MetricUnitController {
    @Autowired
    private MetricUnitService metricUnitService;

    @PostMapping
    public ResponseEntity<ApiResponse<MetricUnitResponse>> createMetricUnit(@RequestBody MetricUnitRequest request) {
        MetricUnitResponse response = metricUnitService.createMetricUnit(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MetricUnitResponse>> updateMetricUnit(@PathVariable Long id, @RequestBody MetricUnitRequest request) {
        MetricUnitResponse response = metricUnitService.updateMetricUnit(id, request);
        if (response == null) return ResponseEntity.status(404).body(ApiResponse.error("Metric unit not found or inactive"));
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMetricUnit(@PathVariable Long id) {
        boolean deleted = metricUnitService.deleteMetricUnit(id);
        if (!deleted) return ResponseEntity.status(404).body(ApiResponse.error("Metric unit not found or already inactive"));
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<MetricUnitResponse>>> listMetricUnits() {
        List<MetricUnitResponse> units = metricUnitService.listMetricUnits();
        return ResponseEntity.ok(ApiResponse.success(units));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MetricUnitResponse>> getMetricUnit(@PathVariable Long id) {
        MetricUnitResponse response = metricUnitService.getMetricUnit(id);
        if (response == null) return ResponseEntity.status(404).body(ApiResponse.error("Metric unit not found or inactive"));
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}

