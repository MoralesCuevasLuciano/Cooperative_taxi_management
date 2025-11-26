package com.pepotec.cooperative_taxi_managment.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketTaxiDTO {
    private Long id;

    @Valid
    @NotNull(message = "The vehicle cannot be null")
    private VehicleDTO vehicle;

    @Valid
    @NotNull(message = "The settlement cannot be null")
    private DriverSettlementDTO settlement;

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

