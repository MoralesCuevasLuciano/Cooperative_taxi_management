package com.pepotec.cooperative_taxi_managment.models.dto.dailyfuel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import com.pepotec.cooperative_taxi_managment.models.enums.FuelType;

/**
 * DTO usado para crear registros de combustible diario.
 * Los IDs de driver, vehicle y settlement se reciben por path/param.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DailyFuelCreateDTO {

    @NotNull(message = "The ticket issue date cannot be null")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate ticketIssueDate;

    @NotNull(message = "The submission date cannot be null")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate submissionDate;

    @NotNull(message = "The amount cannot be null")
    @Positive(message = "The amount must be positive")
    private Double amount;

    @NotNull(message = "The fuel type cannot be null")
    private FuelType fuelType;
}



