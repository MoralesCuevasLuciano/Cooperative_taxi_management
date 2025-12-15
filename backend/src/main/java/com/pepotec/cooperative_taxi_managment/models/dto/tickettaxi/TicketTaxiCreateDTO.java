package com.pepotec.cooperative_taxi_managment.models.dto.tickettaxi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

/**
 * DTO usado para crear tickets de taxi.
 * El vehicleId y el settlementId se reciben por path.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketTaxiCreateDTO {

    private String ticketNumber;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate cutDate;

    @NotNull(message = "The amount cannot be null")
    @PositiveOrZero(message = "The amount must be greater than or equal to zero")
    private Double amount;

    @PositiveOrZero(message = "The free kilometers must be greater than or equal to zero")
    private Double freeKilometers;

    @PositiveOrZero(message = "The occupied kilometers must be greater than or equal to zero")
    private Double occupiedKilometers;

    @PositiveOrZero(message = "The trips must be greater than or equal to zero")
    private Integer trips;
}



