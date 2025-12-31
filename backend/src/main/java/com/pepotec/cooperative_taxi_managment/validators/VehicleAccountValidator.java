package com.pepotec.cooperative_taxi_managment.validators;

import com.pepotec.cooperative_taxi_managment.exceptions.InvalidDataException;
import com.pepotec.cooperative_taxi_managment.models.dto.vehicle.account.VehicleAccountDTO;
import com.pepotec.cooperative_taxi_managment.repositories.VehicleAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class VehicleAccountValidator {

    @Autowired
    private VehicleAccountRepository vehicleAccountRepository;

    public void validateVehicleAccountFields(VehicleAccountDTO account) {
        if (account == null) {
            throw new InvalidDataException("The account cannot be null");
        }

        if (account.getVehicleId() == null) {
            throw new InvalidDataException("The vehicle id cannot be null");
        }

        if (account.getBalance() == null) {
            throw new InvalidDataException("The balance cannot be null");
        }

        if (account.getLastModified() != null && account.getLastModified().isAfter(LocalDate.now())) {
            throw new InvalidDataException("The last modified date cannot be in the future");
        }
    }

    /**
     * Validaciones para creación de cuentas (ID del vehículo viene por path).
     */
    public void validateVehicleAccountCreateFields(Double balance, LocalDate lastModified) {
        if (balance == null) {
            throw new InvalidDataException("The balance cannot be null");
        }

        if (lastModified != null && lastModified.isAfter(LocalDate.now())) {
            throw new InvalidDataException("The last modified date cannot be in the future");
        }
    }

    /**
     * Valida que el ID no sea nulo (para updates y deletes).
     * @param id ID a validar
     */
    public void validateIdNotNull(Long id) {
        if (id == null) {
            throw new InvalidDataException("The ID cannot be null");
        }
    }

    /**
     * Valida que el ID no sea nulo para actualizar.
     * @param id ID a validar
     */
    public void validateIdNotNullForUpdate(Long id) {
        if (id == null) {
            throw new InvalidDataException("The account ID cannot be null for update");
        }
    }

    /**
     * Valida que el vehicle ID no sea nulo.
     * @param vehicleId ID del vehículo a validar
     */
    public void validateVehicleIdNotNull(Long vehicleId) {
        if (vehicleId == null) {
            throw new InvalidDataException("The vehicle ID cannot be null");
        }
    }

    /**
     * Valida la unicidad: que el vehículo no tenga ya una cuenta asociada.
     * @param vehicleId ID del vehículo
     */
    public void validateUniqueVehicleAccount(Long vehicleId) {
        vehicleAccountRepository.findByVehicleId(vehicleId).ifPresent(existing -> {
            throw new InvalidDataException("The vehicle already has an associated account");
        });
    }
}


