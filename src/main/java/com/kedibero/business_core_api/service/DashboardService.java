package com.kedibero.business_core_api.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.kedibero.business_core_api.repository.DashboardRepository;
import java.util.Map;

@Service
public class DashboardService {
    @Autowired
    private DashboardRepository dashboardRepository;

    public Map<String, Object> getDashboard() {
        return dashboardRepository.findDashboards();
    }
}