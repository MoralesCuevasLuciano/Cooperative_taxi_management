package com.pepotec.cooperative_taxi_managment.validators;

import com.pepotec.cooperative_taxi_managment.exceptions.InvalidDataException;
import com.pepotec.cooperative_taxi_managment.models.dto.vehicle.account.VehicleAccountDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class VehicleAccountValidator {

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
}


