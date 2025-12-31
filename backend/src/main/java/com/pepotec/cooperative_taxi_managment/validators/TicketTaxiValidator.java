package com.pepotec.cooperative_taxi_managment.validators;

import com.pepotec.cooperative_taxi_managment.exceptions.InvalidDataException;
import com.pepotec.cooperative_taxi_managment.models.dto.tickettaxi.TicketTaxiDTO;
import com.pepotec.cooperative_taxi_managment.models.dto.tickettaxi.TicketTaxiCreateDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TicketTaxiValidator {

    public void validateTicketTaxiSpecificFields(TicketTaxiDTO ticketTaxi) {
        if (ticketTaxi.getVehicle() == null || ticketTaxi.getVehicle().getId() == null) {
            throw new InvalidDataException("The vehicle cannot be null");
        }

        if (ticketTaxi.getSettlement() == null || ticketTaxi.getSettlement().getId() == null) {
            throw new InvalidDataException("The settlement cannot be null");
        }

        if (ticketTaxi.getAmount() == null) {
            throw new InvalidDataException("The amount cannot be null");
        }

        if (ticketTaxi.getAmount() < 0) {
            throw new InvalidDataException("The amount must be greater than or equal to zero");
        }

        // Validar campos opcionales que, si están presentes, deben ser >= 0
        if (ticketTaxi.getFreeKilometers() != null && ticketTaxi.getFreeKilometers() < 0) {
            throw new InvalidDataException("Free kilometers must be greater than or equal to zero");
        }

        if (ticketTaxi.getOccupiedKilometers() != null && ticketTaxi.getOccupiedKilometers() < 0) {
            throw new InvalidDataException("Occupied kilometers must be greater than or equal to zero");
        }

        if (ticketTaxi.getTrips() != null && ticketTaxi.getTrips() < 0) {
            throw new InvalidDataException("Trips must be greater than or equal to zero");
        }

        // Validar que si ambas fechas están presentes, cutDate >= startDate
        if (ticketTaxi.getStartDate() != null && ticketTaxi.getCutDate() != null) {
            if (ticketTaxi.getCutDate().isBefore(ticketTaxi.getStartDate())) {
                throw new InvalidDataException(
                    "The cut date cannot be before the start date. " +
                    "Start date: " + ticketTaxi.getStartDate() + 
                    ", Cut date: " + ticketTaxi.getCutDate()
                );
            }
        }
    }

    /**
     * Validaciones para creación (IDs vienen por path).
     */
    public void validateTicketTaxiCreateFields(TicketTaxiCreateDTO ticketTaxi) {
        if (ticketTaxi == null) {
            throw new InvalidDataException("The ticket cannot be null");
        }

        if (ticketTaxi.getAmount() == null) {
            throw new InvalidDataException("The amount cannot be null");
        }

        if (ticketTaxi.getAmount() < 0) {
            throw new InvalidDataException("The amount must be greater than or equal to zero");
        }

        if (ticketTaxi.getFreeKilometers() != null && ticketTaxi.getFreeKilometers() < 0) {
            throw new InvalidDataException("Free kilometers must be greater than or equal to zero");
        }

        if (ticketTaxi.getOccupiedKilometers() != null && ticketTaxi.getOccupiedKilometers() < 0) {
            throw new InvalidDataException("Occupied kilometers must be greater than or equal to zero");
        }

        if (ticketTaxi.getTrips() != null && ticketTaxi.getTrips() < 0) {
            throw new InvalidDataException("Trips must be greater than or equal to zero");
        }

        if (ticketTaxi.getStartDate() != null && ticketTaxi.getCutDate() != null) {
            if (ticketTaxi.getCutDate().isBefore(ticketTaxi.getStartDate())) {
                throw new InvalidDataException(
                    "The cut date cannot be before the start date. " +
                    "Start date: " + ticketTaxi.getStartDate() +
                    ", Cut date: " + ticketTaxi.getCutDate()
                );
            }
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
     * Valida que el settlement ID no sea nulo.
     * @param settlementId ID de la rendición a validar
     */
    public void validateSettlementIdNotNull(Long settlementId) {
        if (settlementId == null) {
            throw new InvalidDataException("The settlement ID cannot be null");
        }
    }

    /**
     * Valida que el ticket number no esté vacío.
     * @param ticketNumber Número de ticket a validar
     */
    public void validateTicketNumberNotEmpty(String ticketNumber) {
        if (ticketNumber == null || ticketNumber.trim().isEmpty()) {
            throw new InvalidDataException("The ticket number cannot be empty");
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

