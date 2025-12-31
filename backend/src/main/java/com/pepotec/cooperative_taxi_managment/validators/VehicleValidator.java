package com.pepotec.cooperative_taxi_managment.validators;

import com.pepotec.cooperative_taxi_managment.exceptions.DuplicateFieldException;
import com.pepotec.cooperative_taxi_managment.exceptions.InvalidDataException;
import com.pepotec.cooperative_taxi_managment.models.dto.vehicle.VehicleDTO;
import com.pepotec.cooperative_taxi_managment.models.dto.vehicle.VehicleCreateDTO;
import com.pepotec.cooperative_taxi_managment.repositories.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VehicleValidator {

    @Autowired
    private VehicleRepository vehicleRepository;

    public void validateVehicleSpecificFields(VehicleDTO vehicle) {
        if (vehicle.getLicensePlate() == null || vehicle.getLicensePlate().trim().isEmpty()) {
            throw new InvalidDataException("The license plate cannot be empty");
        }

        if (vehicle.getLicenseNumber() == null || vehicle.getLicenseNumber().trim().isEmpty()) {
            throw new InvalidDataException("The license number cannot be empty");
        }

        if (vehicle.getEngineNumber() == null || vehicle.getEngineNumber().trim().isEmpty()) {
            throw new InvalidDataException("The engine number cannot be empty");
        }

        if (vehicle.getChassisNumber() == null || vehicle.getChassisNumber().trim().isEmpty()) {
            throw new InvalidDataException("The chassis number cannot be empty");
        }

        if (vehicle.getVtvExpirationDate() == null) {
            throw new InvalidDataException("The VTV expiration date cannot be null");
        }

        if (vehicle.getModel() == null || vehicle.getModel().getId() == null) {
            throw new InvalidDataException("The model cannot be null");
        }
    }

    public void validateVehicleCreateFields(VehicleCreateDTO vehicle) {
        if (vehicle.getLicensePlate() == null || vehicle.getLicensePlate().trim().isEmpty()) {
            throw new InvalidDataException("The license plate cannot be empty");
        }

        if (vehicle.getLicenseNumber() == null || vehicle.getLicenseNumber().trim().isEmpty()) {
            throw new InvalidDataException("The license number cannot be empty");
        }

        if (vehicle.getEngineNumber() == null || vehicle.getEngineNumber().trim().isEmpty()) {
            throw new InvalidDataException("The engine number cannot be empty");
        }

        if (vehicle.getChassisNumber() == null || vehicle.getChassisNumber().trim().isEmpty()) {
            throw new InvalidDataException("The chassis number cannot be empty");
        }

        if (vehicle.getVtvExpirationDate() == null) {
            throw new InvalidDataException("The VTV expiration date cannot be null");
        }

        if (vehicle.getModelId() == null) {
            throw new InvalidDataException("The model ID cannot be null");
        }
    }

    public void validateUniqueFields(VehicleDTO vehicle, Long excludeId) {
        validateUniqueField(
            vehicle.getLicensePlate(),
            vehicleRepository::findByLicensePlate,
            excludeId,
            "license plate",
            vehicle.getLicensePlate()
        );

        validateUniqueField(
            vehicle.getLicenseNumber(),
            vehicleRepository::findByLicenseNumber,
            excludeId,
            "license number",
            vehicle.getLicenseNumber()
        );

        validateUniqueField(
            vehicle.getEngineNumber(),
            vehicleRepository::findByEngineNumber,
            excludeId,
            "engine number",
            vehicle.getEngineNumber()
        );

        validateUniqueField(
            vehicle.getChassisNumber(),
            vehicleRepository::findByChassisNumber,
            excludeId,
            "chassis number",
            vehicle.getChassisNumber()
        );
    }

    private void validateUniqueField(
            String fieldValue,
            java.util.function.Function<String, java.util.Optional<com.pepotec.cooperative_taxi_managment.models.entities.VehicleEntity>> finder,
            Long excludeId,
            String fieldName,
            String displayValue) {
        if (fieldValue != null) {
            finder.apply(fieldValue)
                .ifPresent(existing -> {
                    if (excludeId == null || !existing.getId().equals(excludeId)) {
                        throw new DuplicateFieldException(
                            fieldName,
                            displayValue,
                            "A vehicle with " + fieldName + ": " + displayValue + " already exists"
                        );
                    }
                });
        }
    }
}

