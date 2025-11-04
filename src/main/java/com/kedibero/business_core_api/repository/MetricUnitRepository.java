package com.kedibero.business_core_api.repository;

import com.kedibero.business_core_api.entity.MetricUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MetricUnitRepository extends JpaRepository<MetricUnit, Long> {
    List<MetricUnit> findByIsActiveTrue();
}


