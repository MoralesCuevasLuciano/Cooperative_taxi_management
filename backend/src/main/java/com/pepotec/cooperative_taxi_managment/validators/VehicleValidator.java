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
            throw new InvalidDataException("La patente no puede estar vacía");
        }

        if (vehicle.getLicenseNumber() == null || vehicle.getLicenseNumber().trim().isEmpty()) {
            throw new InvalidDataException("El número de licencia no puede estar vacío");
        }

        if (vehicle.getEngineNumber() == null || vehicle.getEngineNumber().trim().isEmpty()) {
            throw new InvalidDataException("El número de motor no puede estar vacío");
        }

        if (vehicle.getChassisNumber() == null || vehicle.getChassisNumber().trim().isEmpty()) {
            throw new InvalidDataException("El número de chasis no puede estar vacío");
        }

        if (vehicle.getVtvExpirationDate() == null) {
            throw new InvalidDataException("La fecha de vencimiento de VTV no puede ser nula");
        }

        if (vehicle.getModel() == null || vehicle.getModel().getId() == null) {
            throw new InvalidDataException("El modelo no puede ser nulo");
        }
    }

    public void validateVehicleCreateFields(VehicleCreateDTO vehicle) {
        if (vehicle.getLicensePlate() == null || vehicle.getLicensePlate().trim().isEmpty()) {
            throw new InvalidDataException("La patente no puede estar vacía");
        }

        if (vehicle.getLicenseNumber() == null || vehicle.getLicenseNumber().trim().isEmpty()) {
            throw new InvalidDataException("El número de licencia no puede estar vacío");
        }

        if (vehicle.getEngineNumber() == null || vehicle.getEngineNumber().trim().isEmpty()) {
            throw new InvalidDataException("El número de motor no puede estar vacío");
        }

        if (vehicle.getChassisNumber() == null || vehicle.getChassisNumber().trim().isEmpty()) {
            throw new InvalidDataException("El número de chasis no puede estar vacío");
        }

        if (vehicle.getVtvExpirationDate() == null) {
            throw new InvalidDataException("La fecha de vencimiento de VTV no puede ser nula");
        }

        if (vehicle.getModelId() == null) {
            throw new InvalidDataException("El ID de modelo no puede ser nulo");
        }
    }

    public void validateUniqueFields(VehicleDTO vehicle, Long excludeId) {
        validateUniqueField(
            vehicle.getLicensePlate(),
            vehicleRepository::findByLicensePlate,
            excludeId,
            "patente",
            vehicle.getLicensePlate()
        );

        validateUniqueField(
            vehicle.getLicenseNumber(),
            vehicleRepository::findByLicenseNumber,
            excludeId,
            "número de licencia",
            vehicle.getLicenseNumber()
        );

        validateUniqueField(
            vehicle.getEngineNumber(),
            vehicleRepository::findByEngineNumber,
            excludeId,
            "número de motor",
            vehicle.getEngineNumber()
        );

        validateUniqueField(
            vehicle.getChassisNumber(),
            vehicleRepository::findByChassisNumber,
            excludeId,
            "número de chasis",
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
                            "Ya existe un vehículo con " + fieldName + ": " + displayValue
                        );
                    }
                });
        }
    }
}

