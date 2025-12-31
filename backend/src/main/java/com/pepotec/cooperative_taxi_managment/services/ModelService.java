package com.pepotec.cooperative_taxi_managment.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.pepotec.cooperative_taxi_managment.models.dto.model.ModelDTO;
import com.pepotec.cooperative_taxi_managment.models.entities.ModelEntity;
import com.pepotec.cooperative_taxi_managment.models.entities.BrandEntity;
import com.pepotec.cooperative_taxi_managment.repositories.ModelRepository;
import com.pepotec.cooperative_taxi_managment.exceptions.ResourceNotFoundException;
import com.pepotec.cooperative_taxi_managment.exceptions.InvalidDataException;
import com.pepotec.cooperative_taxi_managment.validators.ModelValidator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ModelService {

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private BrandService brandService;

    @Autowired
    private ModelValidator modelValidator;

    @Transactional
    public ModelDTO createModel(ModelDTO model) {
        modelValidator.validateModelSpecificFields(model);
        modelValidator.validateUniqueFields(model, null);

        BrandEntity brand = brandService.getBrandEntityById(model.getBrand().getId());
        
        if (brand == null || brand.getId() == null) {
            throw new ResourceNotFoundException(model.getBrand().getId(), "Marca");
        }

        ModelEntity modelEntity = new ModelEntity();
        modelEntity.setName(model.getName());
        modelEntity.setYear(model.getYear());
        modelEntity.setBrand(brand);
        
        ModelEntity saved = modelRepository.save(modelEntity);
        return convertToDTO(saved);
    }

    public ModelDTO getModelById(Long id) {
        ModelEntity model = modelRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(id, "Modelo"));
        return convertToDTO(model);
    }

    public List<ModelDTO> getAllModels() {
        return modelRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public List<ModelDTO> getModelsByBrand(Long brandId) {
        return modelRepository.findByBrandId(brandId).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public ModelDTO updateModel(ModelDTO model) {
        if (model.getId() == null) {
            throw new InvalidDataException("The ID cannot be null for update");
        }

        ModelEntity modelEntity = modelRepository.findById(model.getId())
            .orElseThrow(() -> new ResourceNotFoundException(model.getId(), "Modelo"));

        modelValidator.validateModelSpecificFields(model);
        modelValidator.validateUniqueFields(model, model.getId());

        BrandEntity brand = brandService.getBrandEntityById(model.getBrand().getId());

        modelEntity.setName(model.getName());
        modelEntity.setYear(model.getYear());
        modelEntity.setBrand(brand);

        return convertToDTO(modelRepository.save(modelEntity));
    }

    public void deleteModel(Long id) {
        if (id == null) {
            throw new InvalidDataException("The ID cannot be null");
        }

        ModelEntity model = modelRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(id, "Modelo"));

        modelRepository.delete(model);
    }

    private ModelDTO convertToDTO(ModelEntity model) {
        if (model == null) {
            return null;
        }
        return ModelDTO.builder()
            .id(model.getId())
            .name(model.getName())
            .year(model.getYear())
            .brand(brandService.getBrandById(model.getBrand().getId()))
            .build();
    }

    /**
     * Obtiene la entidad ModelEntity por ID para uso interno de otros servicios.
     * Este método es package-private para mantener el encapsulamiento.
     */
    ModelEntity getModelEntityById(Long id) {
        return modelRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(id, "Modelo"));
    }
}

