package com.pepotec.cooperative_taxi_managment.validators;

import com.pepotec.cooperative_taxi_managment.exceptions.InvalidDataException;
import com.pepotec.cooperative_taxi_managment.models.dto.dailyfuel.DailyFuelDTO;
import com.pepotec.cooperative_taxi_managment.models.dto.dailyfuel.DailyFuelCreateDTO;
import org.springframework.stereotype.Component;

@Component
public class DailyFuelValidator {

    public void validateDailyFuelSpecificFields(DailyFuelDTO dailyFuel) {
        if (dailyFuel.getDriver() == null || dailyFuel.getDriver().getId() == null) {
            throw new InvalidDataException("El chofer no puede ser nulo");
        }

        if (dailyFuel.getVehicle() == null || dailyFuel.getVehicle().getId() == null) {
            throw new InvalidDataException("El vehículo no puede ser nulo");
        }

        if (dailyFuel.getTicketIssueDate() == null) {
            throw new InvalidDataException("La fecha de emisión del ticket no puede ser nula");
        }

        if (dailyFuel.getSubmissionDate() == null) {
            throw new InvalidDataException("La fecha de entrega no puede ser nula");
        }

        if (dailyFuel.getAmount() == null) {
            throw new InvalidDataException("El monto no puede ser nulo");
        }

        if (dailyFuel.getAmount() <= 0) {
            throw new InvalidDataException("El monto debe ser positivo");
        }

        if (dailyFuel.getFuelType() == null) {
            throw new InvalidDataException("El tipo de combustible no puede ser nulo");
        }

        // Validar que submissionDate no sea anterior a ticketIssueDate
        if (dailyFuel.getSubmissionDate().isBefore(dailyFuel.getTicketIssueDate())) {
            throw new InvalidDataException(
                "La fecha de entrega no puede ser anterior a la fecha de emisión del ticket. " +
                "Fecha de emisión: " + dailyFuel.getTicketIssueDate() + 
                ", Fecha de entrega: " + dailyFuel.getSubmissionDate()
            );
        }
    }

    /**
     * Validaciones específicas para la creación.
     * Los IDs de driver/vehicle/settlement vienen por path, no se validan aquí.
     */
    public void validateDailyFuelCreateFields(DailyFuelCreateDTO dailyFuel) {
        if (dailyFuel == null) {
            throw new InvalidDataException("El registro de combustible diario no puede ser nulo");
        }

        if (dailyFuel.getTicketIssueDate() == null) {
            throw new InvalidDataException("La fecha de emisión del ticket no puede ser nula");
        }

        if (dailyFuel.getSubmissionDate() == null) {
            throw new InvalidDataException("La fecha de entrega no puede ser nula");
        }

        if (dailyFuel.getAmount() == null) {
            throw new InvalidDataException("El monto no puede ser nulo");
        }

        if (dailyFuel.getAmount() <= 0) {
            throw new InvalidDataException("El monto debe ser positivo");
        }

        if (dailyFuel.getFuelType() == null) {
            throw new InvalidDataException("El tipo de combustible no puede ser nulo");
        }

        if (dailyFuel.getSubmissionDate().isBefore(dailyFuel.getTicketIssueDate())) {
            throw new InvalidDataException(
                "La fecha de entrega no puede ser anterior a la fecha de emisión del ticket. " +
                "Fecha de emisión: " + dailyFuel.getTicketIssueDate() +
                ", Fecha de entrega: " + dailyFuel.getSubmissionDate()
            );
        }
    }
}

