package com.pepotec.cooperative_taxi_managment.validators;

import com.pepotec.cooperative_taxi_managment.exceptions.InvalidDataException;
import com.pepotec.cooperative_taxi_managment.models.dto.tickettaxi.TicketTaxiDTO;
import com.pepotec.cooperative_taxi_managment.models.dto.tickettaxi.TicketTaxiCreateDTO;
import org.springframework.stereotype.Component;

@Component
public class TicketTaxiValidator {

    public void validateTicketTaxiSpecificFields(TicketTaxiDTO ticketTaxi) {
        if (ticketTaxi.getVehicle() == null || ticketTaxi.getVehicle().getId() == null) {
            throw new InvalidDataException("El vehículo no puede ser nulo");
        }

        if (ticketTaxi.getSettlement() == null || ticketTaxi.getSettlement().getId() == null) {
            throw new InvalidDataException("La rendición no puede ser nula");
        }

        if (ticketTaxi.getAmount() == null) {
            throw new InvalidDataException("El monto no puede ser nulo");
        }

        if (ticketTaxi.getAmount() < 0) {
            throw new InvalidDataException("El monto debe ser mayor o igual a cero");
        }

        // Validar campos opcionales que, si están presentes, deben ser >= 0
        if (ticketTaxi.getFreeKilometers() != null && ticketTaxi.getFreeKilometers() < 0) {
            throw new InvalidDataException("Los kilómetros libres deben ser mayores o iguales a cero");
        }

        if (ticketTaxi.getOccupiedKilometers() != null && ticketTaxi.getOccupiedKilometers() < 0) {
            throw new InvalidDataException("Los kilómetros ocupados deben ser mayores o iguales a cero");
        }

        if (ticketTaxi.getTrips() != null && ticketTaxi.getTrips() < 0) {
            throw new InvalidDataException("Los viajes deben ser mayores o iguales a cero");
        }

        // Validar que si ambas fechas están presentes, cutDate >= startDate
        if (ticketTaxi.getStartDate() != null && ticketTaxi.getCutDate() != null) {
            if (ticketTaxi.getCutDate().isBefore(ticketTaxi.getStartDate())) {
                throw new InvalidDataException(
                    "La fecha de corte no puede ser anterior a la fecha de inicio. " +
                    "Fecha de inicio: " + ticketTaxi.getStartDate() + 
                    ", Fecha de corte: " + ticketTaxi.getCutDate()
                );
            }
        }
    }

    /**
     * Validaciones para creación (IDs vienen por path).
     */
    public void validateTicketTaxiCreateFields(TicketTaxiCreateDTO ticketTaxi) {
        if (ticketTaxi == null) {
            throw new InvalidDataException("El ticket no puede ser nulo");
        }

        if (ticketTaxi.getAmount() == null) {
            throw new InvalidDataException("El monto no puede ser nulo");
        }

        if (ticketTaxi.getAmount() < 0) {
            throw new InvalidDataException("El monto debe ser mayor o igual a cero");
        }

        if (ticketTaxi.getFreeKilometers() != null && ticketTaxi.getFreeKilometers() < 0) {
            throw new InvalidDataException("Los kilómetros libres deben ser mayores o iguales a cero");
        }

        if (ticketTaxi.getOccupiedKilometers() != null && ticketTaxi.getOccupiedKilometers() < 0) {
            throw new InvalidDataException("Los kilómetros ocupados deben ser mayores o iguales a cero");
        }

        if (ticketTaxi.getTrips() != null && ticketTaxi.getTrips() < 0) {
            throw new InvalidDataException("Los viajes deben ser mayores o iguales a cero");
        }

        if (ticketTaxi.getStartDate() != null && ticketTaxi.getCutDate() != null) {
            if (ticketTaxi.getCutDate().isBefore(ticketTaxi.getStartDate())) {
                throw new InvalidDataException(
                    "La fecha de corte no puede ser anterior a la fecha de inicio. " +
                    "Fecha de inicio: " + ticketTaxi.getStartDate() +
                    ", Fecha de corte: " + ticketTaxi.getCutDate()
                );
            }
        }
    }
}

