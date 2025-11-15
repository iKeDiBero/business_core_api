package com.kedibero.business_core_api.repository;

import com.kedibero.business_core_api.entity.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModelRepository extends JpaRepository<Model, Long> {
    List<Model> findByIsActiveTrue();
    List<Model> findByBrandIdAndIsActiveTrue(Long brandId);
}

