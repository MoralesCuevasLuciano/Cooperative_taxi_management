package com.pepotec.cooperative_taxi_managment.validators;

import com.pepotec.cooperative_taxi_managment.exceptions.DuplicateFieldException;
import com.pepotec.cooperative_taxi_managment.exceptions.InvalidDataException;
import com.pepotec.cooperative_taxi_managment.models.dto.ModelDTO;
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
            throw new InvalidDataException("El nombre del modelo no puede estar vacío");
        }

        if (model.getBrand() == null || model.getBrand().getId() == null) {
            throw new InvalidDataException("La marca no puede ser nula");
        }

        validateYear(model);
    }

    private void validateYear(ModelDTO model) {
        if (model.getYear() == null) {
            throw new InvalidDataException("El año no puede ser nulo");
        }

        int currentYear = Year.now().getValue();
        if (model.getYear() > currentYear) {
            throw new InvalidDataException("El año no puede ser futuro. El año máximo permitido es " + currentYear);
        }
    }

    public void validateUniqueFields(ModelDTO model, Long excludeId) {
        modelRepository.findByNameAndBrandId(model.getName(), model.getBrand().getId())
            .ifPresent(existing -> {
                if (excludeId == null || !existing.getId().equals(excludeId)) {
                    throw new DuplicateFieldException(
                        "name",
                        model.getName(),
                        "Ya existe un modelo con el nombre '" + model.getName() + "' para la marca especificada"
                    );
                }
            });
    }
}

