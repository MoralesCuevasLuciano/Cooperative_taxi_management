package com.pepotec.cooperative_taxi_managment.validators;

import com.pepotec.cooperative_taxi_managment.exceptions.InvalidDataException;
import com.pepotec.cooperative_taxi_managment.models.dto.dailyfuel.DailyFuelDTO;
import com.pepotec.cooperative_taxi_managment.models.dto.dailyfuel.DailyFuelCreateDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DailyFuelValidator {

    public void validateDailyFuelSpecificFields(DailyFuelDTO dailyFuel) {
        if (dailyFuel.getDriver() == null || dailyFuel.getDriver().getId() == null) {
            throw new InvalidDataException("The driver cannot be null");
        }

        if (dailyFuel.getVehicle() == null || dailyFuel.getVehicle().getId() == null) {
            throw new InvalidDataException("The vehicle cannot be null");
        }

        if (dailyFuel.getTicketIssueDate() == null) {
            throw new InvalidDataException("The ticket issue date cannot be null");
        }

        if (dailyFuel.getSubmissionDate() == null) {
            throw new InvalidDataException("The submission date cannot be null");
        }

        if (dailyFuel.getAmount() == null) {
            throw new InvalidDataException("The amount cannot be null");
        }

        if (dailyFuel.getAmount() <= 0) {
            throw new InvalidDataException("The amount must be positive");
        }

        if (dailyFuel.getFuelType() == null) {
            throw new InvalidDataException("The fuel type cannot be null");
        }

        // Validar que submissionDate no sea anterior a ticketIssueDate
        if (dailyFuel.getSubmissionDate().isBefore(dailyFuel.getTicketIssueDate())) {
            throw new InvalidDataException(
                "The submission date cannot be before the ticket issue date. " +
                "Ticket issue date: " + dailyFuel.getTicketIssueDate() + 
                ", Submission date: " + dailyFuel.getSubmissionDate()
            );
        }

        // Validar porcentajes si están presentes
        validatePercentages(dailyFuel.getCooperativePercentage(), dailyFuel.getDriverPercentage());
    }

    /**
     * Validaciones específicas para la creación.
     * Los IDs de driver/vehicle/settlement vienen por path, no se validan aquí.
     */
    public void validateDailyFuelCreateFields(DailyFuelCreateDTO dailyFuel) {
        if (dailyFuel == null) {
            throw new InvalidDataException("The daily fuel record cannot be null");
        }

        if (dailyFuel.getTicketIssueDate() == null) {
            throw new InvalidDataException("The ticket issue date cannot be null");
        }

        if (dailyFuel.getSubmissionDate() == null) {
            throw new InvalidDataException("The submission date cannot be null");
        }

        if (dailyFuel.getAmount() == null) {
            throw new InvalidDataException("The amount cannot be null");
        }

        if (dailyFuel.getAmount() <= 0) {
            throw new InvalidDataException("The amount must be positive");
        }

        if (dailyFuel.getFuelType() == null) {
            throw new InvalidDataException("The fuel type cannot be null");
        }

        if (dailyFuel.getSubmissionDate().isBefore(dailyFuel.getTicketIssueDate())) {
            throw new InvalidDataException(
                "The submission date cannot be before the ticket issue date. " +
                "Ticket issue date: " + dailyFuel.getTicketIssueDate() +
                ", Submission date: " + dailyFuel.getSubmissionDate()
            );
        }

        // Validar porcentajes si están presentes
        validatePercentages(dailyFuel.getCooperativePercentage(), dailyFuel.getDriverPercentage());
    }

    /**
     * Valida que los porcentajes sumen 100 si ambos están presentes.
     * Si solo uno está presente, lanza excepción.
     * Si ninguno está presente, es válido (se asignarán por defecto en el servicio).
     */
    private void validatePercentages(Double cooperativePercentage, Double driverPercentage) {
        // Si ambos son null, es válido (se asignarán por defecto)
        if (cooperativePercentage == null && driverPercentage == null) {
            return;
        }

        // Si solo uno está presente, es inválido
        if (cooperativePercentage == null || driverPercentage == null) {
            throw new InvalidDataException(
                "Percentages must be both present or both absent. " +
                "If one is specified, the other must be specified as well."
            );
        }

        // Validar que estén en rango válido (0-100)
        if (cooperativePercentage < 0 || cooperativePercentage > 100) {
            throw new InvalidDataException(
                "The cooperative percentage must be between 0 and 100. Received value: " + cooperativePercentage
            );
        }

        if (driverPercentage < 0 || driverPercentage > 100) {
            throw new InvalidDataException(
                "The driver percentage must be between 0 and 100. Received value: " + driverPercentage
            );
        }

        // Validar que sumen 100 (con tolerancia de 0.01 para errores de punto flotante)
        double sum = cooperativePercentage + driverPercentage;
        if (Math.abs(sum - 100.0) > 0.01) {
            throw new InvalidDataException(
                "The sum of percentages must be 100. " +
                "Cooperative percentage: " + cooperativePercentage + 
                ", Driver percentage: " + driverPercentage + 
                ", Sum: " + sum
            );
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
            throw new InvalidDataException("The ID cannot be null for update");
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
     * Valida que el driver ID no sea nulo.
     * @param driverId ID del chofer a validar
     */
    public void validateDriverIdNotNull(Long driverId) {
        if (driverId == null) {
            throw new InvalidDataException("The driver ID cannot be null");
        }
    }

    /**
     * Valida que el settlement ID no sea nulo.
     * @param settlementId ID de la rendición a validar
     */
    public void validateSettlementIdNotNull(Long settlementId) {
        if (settlementId == null) {
            throw new InvalidDataException("The settlement ID cannot be null");
        }
    }

    /**
     * Valida que el fuel type no sea nulo.
     * @param fuelType Tipo de combustible a validar
     */
    public void validateFuelTypeNotNull(com.pepotec.cooperative_taxi_managment.models.enums.FuelType fuelType) {
        if (fuelType == null) {
            throw new InvalidDataException("The fuel type cannot be null");
        }
    }

    /**
     * Valida un rango de fechas (startDate y endDate no nulos, y startDate <= endDate).
     * @param startDate Fecha inicial
     * @param endDate Fecha final
     */
    public void validateDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new InvalidDataException("The start and end dates cannot be null");
        }
        if (startDate.isAfter(endDate)) {
            throw new InvalidDataException("The start date cannot be after the end date");
        }
    }
}

