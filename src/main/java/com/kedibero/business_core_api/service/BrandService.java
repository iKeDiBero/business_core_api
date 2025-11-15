package com.kedibero.business_core_api.service;

import com.kedibero.business_core_api.dto.BrandRequest;
import com.kedibero.business_core_api.dto.BrandResponse;
import com.kedibero.business_core_api.entity.Brand;
import com.kedibero.business_core_api.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BrandService {
    @Autowired
    private BrandRepository brandRepository;

    public BrandResponse createBrand(BrandRequest request) {
        Brand brand = new Brand();
        brand.setName(request.getName());
        brand.setDescription(request.getDescription());
        brand.setLogoUrl(request.getLogoUrl());
        brand.setIsActive(true);
        Brand saved = brandRepository.save(brand);
        return toResponse(saved);
    }

    public BrandResponse updateBrand(Long id, BrandRequest request) {
        Optional<Brand> optional = brandRepository.findById(id);
        if (optional.isEmpty() || !optional.get().getIsActive()) return null;
        Brand brand = optional.get();
        brand.setName(request.getName());
        brand.setDescription(request.getDescription());
        brand.setLogoUrl(request.getLogoUrl());
        Brand saved = brandRepository.save(brand);
        return toResponse(saved);
    }

    public boolean deleteBrand(Long id) {
        Optional<Brand> optional = brandRepository.findById(id);
        if (optional.isEmpty() || !optional.get().getIsActive()) return false;
        Brand brand = optional.get();
        brand.setIsActive(false);
        brandRepository.save(brand);
        return true;
    }

    public List<BrandResponse> listBrands() {
        return brandRepository.findByIsActiveTrue()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public BrandResponse getBrand(Long id) {
        Optional<Brand> optional = brandRepository.findById(id);
        if (optional.isEmpty() || !optional.get().getIsActive()) return null;
        return toResponse(optional.get());
    }

    private BrandResponse toResponse(Brand brand) {
        BrandResponse response = new BrandResponse();
        response.setId(brand.getId());
        response.setName(brand.getName());
        response.setDescription(brand.getDescription());
        response.setLogoUrl(brand.getLogoUrl());
        response.setIsActive(brand.getIsActive());
        response.setCreatedAt(brand.getCreatedAt());
        response.setUpdatedAt(brand.getUpdatedAt());
        return response;
    }
}

