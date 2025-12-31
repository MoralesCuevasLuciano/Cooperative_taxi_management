package com.pepotec.cooperative_taxi_managment.validators;

import com.pepotec.cooperative_taxi_managment.exceptions.DuplicateFieldException;
import com.pepotec.cooperative_taxi_managment.exceptions.InvalidDataException;
import com.pepotec.cooperative_taxi_managment.models.dto.model.ModelDTO;
import com.pepotec.cooperative_taxi_managment.repositories.ModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.time.Year;

@Component
public class ModelValidator {

    @Autowired
    private ModelRepository modelRepository;

    public void validateModelSpecificFields(ModelDTO model) {
        if (model.getName() == null || model.getName().trim().isEmpty()) {
            throw new InvalidDataException("The model name cannot be empty");
        }

        if (model.getBrand() == null || model.getBrand().getId() == null) {
            throw new InvalidDataException("The brand cannot be null");
        }

        validateYear(model);
    }

    private void validateYear(ModelDTO model) {
        if (model.getYear() == null) {
            throw new InvalidDataException("The year cannot be null");
        }

        int currentYear = Year.now().getValue();
        if (model.getYear() > currentYear) {
            throw new InvalidDataException("The year cannot be in the future. The maximum allowed year is " + currentYear);
        }
    }

    public void validateUniqueFields(ModelDTO model, Long excludeId) {
        modelRepository.findByNameAndBrandId(model.getName(), model.getBrand().getId())
            .ifPresent(existing -> {
                if (excludeId == null || !existing.getId().equals(excludeId)) {
                    throw new DuplicateFieldException(
                        "name",
                        model.getName(),
                        "A model with the name '" + model.getName() + "' already exists for the specified brand"
                    );
                }
            });
    }
}

