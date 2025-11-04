package com.kedibero.business_core_api.service;

import com.kedibero.business_core_api.dto.MetricUnitRequest;
import com.kedibero.business_core_api.dto.MetricUnitResponse;
import com.kedibero.business_core_api.entity.MetricUnit;
import com.kedibero.business_core_api.repository.MetricUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MetricUnitService {
    @Autowired
    private MetricUnitRepository metricUnitRepository;

    public MetricUnitResponse createMetricUnit(MetricUnitRequest request) {
        MetricUnit unit = new MetricUnit();
        unit.setName(request.getName());
        unit.setSymbol(request.getSymbol());
        unit.setDescription(request.getDescription());
        unit.setIsActive(true);
        MetricUnit saved = metricUnitRepository.save(unit);
        return toResponse(saved);
    }

    public MetricUnitResponse updateMetricUnit(Long id, MetricUnitRequest request) {
        Optional<MetricUnit> optional = metricUnitRepository.findById(id);
        if (optional.isEmpty() || !optional.get().getIsActive()) return null;
        MetricUnit unit = optional.get();
        unit.setName(request.getName());
        unit.setSymbol(request.getSymbol());
        unit.setDescription(request.getDescription());
        MetricUnit saved = metricUnitRepository.save(unit);
        return toResponse(saved);
    }

    public boolean deleteMetricUnit(Long id) {
        Optional<MetricUnit> optional = metricUnitRepository.findById(id);
        if (optional.isEmpty() || !optional.get().getIsActive()) return false;
        MetricUnit unit = optional.get();
        unit.setIsActive(false);
        metricUnitRepository.save(unit);
        return true;
    }

    public List<MetricUnitResponse> listMetricUnits() {
        return metricUnitRepository.findByIsActiveTrue()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public MetricUnitResponse getMetricUnit(Long id) {
        Optional<MetricUnit> optional = metricUnitRepository.findById(id);
        if (optional.isEmpty() || !optional.get().getIsActive()) return null;
        return toResponse(optional.get());
    }

    private MetricUnitResponse toResponse(MetricUnit unit) {
        MetricUnitResponse response = new MetricUnitResponse();
        response.setId(unit.getId());
        response.setName(unit.getName());
        response.setSymbol(unit.getSymbol());
        response.setDescription(unit.getDescription());
        response.setIsActive(unit.getIsActive());
        response.setCreatedAt(unit.getCreatedAt());
        response.setUpdatedAt(unit.getUpdatedAt());
        return response;
    }
}

