package com.kedibero.business_core_api.service;

import com.kedibero.business_core_api.dto.ModelRequest;
import com.kedibero.business_core_api.dto.ModelResponse;
import com.kedibero.business_core_api.entity.Model;
import com.kedibero.business_core_api.repository.BrandRepository;
import com.kedibero.business_core_api.repository.ModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ModelService {
    @Autowired
    private ModelRepository modelRepository;
    @Autowired
    private BrandRepository brandRepository;

    public ModelResponse createModel(ModelRequest request) {
        Model model = new Model();
        model.setName(request.getName());
        model.setDescription(request.getDescription());
        model.setIsActive(true);
        if (request.getBrandId() != null) {
            brandRepository.findById(request.getBrandId()).ifPresent(model::setBrand);
        }
        Model saved = modelRepository.save(model);
        return toResponse(saved);
    }

    public ModelResponse updateModel(Long id, ModelRequest request) {
        Optional<Model> optional = modelRepository.findById(id);
        if (optional.isEmpty() || !optional.get().getIsActive()) return null;
        Model model = optional.get();
        model.setName(request.getName());
        model.setDescription(request.getDescription());
        if (request.getBrandId() != null) {
            brandRepository.findById(request.getBrandId()).ifPresent(model::setBrand);
        }
        Model saved = modelRepository.save(model);
        return toResponse(saved);
    }

    public boolean deleteModel(Long id) {
        Optional<Model> optional = modelRepository.findById(id);
        if (optional.isEmpty() || !optional.get().getIsActive()) return false;
        Model model = optional.get();
        model.setIsActive(false);
        modelRepository.save(model);
        return true;
    }

    public List<ModelResponse> listModels() {
        return modelRepository.findByIsActiveTrue()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<ModelResponse> listModelsByBrand(Long brandId) {
        return modelRepository.findByBrandIdAndIsActiveTrue(brandId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ModelResponse getModel(Long id) {
        Optional<Model> optional = modelRepository.findById(id);
        if (optional.isEmpty() || !optional.get().getIsActive()) return null;
        return toResponse(optional.get());
    }

    private ModelResponse toResponse(Model model) {
        ModelResponse response = new ModelResponse();
        response.setId(model.getId());
        response.setName(model.getName());
        response.setDescription(model.getDescription());
        response.setIsActive(model.getIsActive());
        response.setCreatedAt(model.getCreatedAt());
        response.setUpdatedAt(model.getUpdatedAt());
        if (model.getBrand() != null) {
            response.setBrandId(model.getBrand().getId());
            response.setBrandName(model.getBrand().getName());
        }
        return response;
    }
}

