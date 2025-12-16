package com.pepotec.cooperative_taxi_managment.models.dto.dailyfuel;

import com.pepotec.cooperative_taxi_managment.models.dto.driversettlement.DriverSettlementDTO;
import com.pepotec.cooperative_taxi_managment.models.dto.person.member.driver.DriverDTO;
import com.pepotec.cooperative_taxi_managment.models.dto.vehicle.VehicleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import com.pepotec.cooperative_taxi_managment.models.enums.FuelType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DailyFuelDTO {
    private Long id;

    @Valid
    @NotNull(message = "The driver cannot be null")
    private DriverDTO driver;

    @Valid
    @NotNull(message = "The vehicle cannot be null")
    private VehicleDTO vehicle;

    @Valid
    private DriverSettlementDTO settlement;

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

    private Double cooperativePercentage;

    private Double driverPercentage;
}


