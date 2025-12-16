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

        // Validar porcentajes si están presentes
        validatePercentages(dailyFuel.getCooperativePercentage(), dailyFuel.getDriverPercentage());
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
                "Los porcentajes deben estar ambos presentes o ambos ausentes. " +
                "Si se especifica uno, debe especificarse el otro."
            );
        }

        // Validar que estén en rango válido (0-100)
        if (cooperativePercentage < 0 || cooperativePercentage > 100) {
            throw new InvalidDataException(
                "El porcentaje de la cooperativa debe estar entre 0 y 100. Valor recibido: " + cooperativePercentage
            );
        }

        if (driverPercentage < 0 || driverPercentage > 100) {
            throw new InvalidDataException(
                "El porcentaje del chofer debe estar entre 0 y 100. Valor recibido: " + driverPercentage
            );
        }

        // Validar que sumen 100 (con tolerancia de 0.01 para errores de punto flotante)
        double sum = cooperativePercentage + driverPercentage;
        if (Math.abs(sum - 100.0) > 0.01) {
            throw new InvalidDataException(
                "La suma de los porcentajes debe ser 100. " +
                "Porcentaje cooperativa: " + cooperativePercentage + 
                ", Porcentaje chofer: " + driverPercentage + 
                ", Suma: " + sum
            );
        }
    }
}

