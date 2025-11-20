package com.pepotec.cooperative_taxi_managment.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.pepotec.cooperative_taxi_managment.models.dto.BrandDTO;
import com.pepotec.cooperative_taxi_managment.models.entities.BrandEntity;
import com.pepotec.cooperative_taxi_managment.repositories.BrandRepository;
import com.pepotec.cooperative_taxi_managment.exceptions.ResourceNotFoundException;
import com.pepotec.cooperative_taxi_managment.exceptions.InvalidDataException;
import com.pepotec.cooperative_taxi_managment.validators.BrandValidator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BrandService {

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private BrandValidator brandValidator;

    public BrandDTO createBrand(BrandDTO brand) {
        brandValidator.validateBrandSpecificFields(brand);
        brandValidator.validateUniqueFields(brand, null);

        BrandEntity brandEntity = convertToEntity(brand);
        return convertToDTO(brandRepository.save(brandEntity));
    }

    public BrandDTO getBrandById(Long id) {
        BrandEntity brand = brandRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(id, "Marca"));
        return convertToDTO(brand);
    }

    public List<BrandDTO> getAllBrands() {
        return brandRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public BrandDTO updateBrand(BrandDTO brand) {
        if (brand.getId() == null) {
            throw new InvalidDataException("El ID no puede ser nulo para actualizar");
        }

        BrandEntity brandEntity = brandRepository.findById(brand.getId())
            .orElseThrow(() -> new ResourceNotFoundException(brand.getId(), "Marca"));

        brandValidator.validateBrandSpecificFields(brand);
        brandValidator.validateUniqueFields(brand, brand.getId());

        brandEntity.setName(brand.getName());
        return convertToDTO(brandRepository.save(brandEntity));
    }

    public void deleteBrand(Long id) {
        if (id == null) {
            throw new InvalidDataException("El ID no puede ser nulo");
        }

        BrandEntity brand = brandRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(id, "Marca"));

        brandRepository.delete(brand);
    }

    private BrandEntity convertToEntity(BrandDTO brand) {
        return BrandEntity.builder()
            .id(brand.getId())
            .name(brand.getName())
            .build();
    }

    private BrandDTO convertToDTO(BrandEntity brand) {
        if (brand == null) {
            return null;
        }
        return BrandDTO.builder()
            .id(brand.getId())
            .name(brand.getName())
            .build();
    }

    /**
     * Obtiene la entidad BrandEntity por ID para uso interno de otros servicios.
     * Este método es package-private para mantener el encapsulamiento.
     */
    BrandEntity getBrandEntityById(Long id) {
        return brandRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(id, "Marca"));
    }
}

