package com.pepotec.cooperative_taxi_managment.validators;

import com.pepotec.cooperative_taxi_managment.exceptions.DuplicateFieldException;
import com.pepotec.cooperative_taxi_managment.exceptions.InvalidDataException;
import com.pepotec.cooperative_taxi_managment.models.dto.brand.BrandDTO;
import com.pepotec.cooperative_taxi_managment.repositories.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BrandValidator {

    @Autowired
    private BrandRepository brandRepository;

    public void validateBrandSpecificFields(BrandDTO brand) {
        if (brand.getName() == null || brand.getName().trim().isEmpty()) {
            throw new InvalidDataException("The brand name cannot be empty");
        }
    }

    public void validateUniqueFields(BrandDTO brand, Long excludeId) {
        brandRepository.findByName(brand.getName())
            .ifPresent(existing -> {
                if (excludeId == null || !existing.getId().equals(excludeId)) {
                    throw new DuplicateFieldException(
                        "name",
                        brand.getName(),
                        "A brand with the name '" + brand.getName() + "' already exists"
                    );
                }
            });
    }
}

