package com.pepotec.cooperative_taxi_managment.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import com.pepotec.cooperative_taxi_managment.models.enums.FuelType;

@Entity
@Table(name = "daily_fuel")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyFuelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_daily_fuel", unique = true, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_driver", nullable = false)
    @NotNull(message = "The driver cannot be null")
    private DriverEntity driver;

    @ManyToOne
    @JoinColumn(name = "id_vehicle", nullable = false)
    @NotNull(message = "The vehicle cannot be null")
    private VehicleEntity vehicle;

    @Column(name = "id_settlement")
    private Long rendicionId;

    @Column(name = "ticket_issue_date", nullable = false)
    @NotNull(message = "The ticket issue date cannot be null")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate ticketIssueDate;

    @Column(name = "submission_date", nullable = false)
    @NotNull(message = "The submission date cannot be null")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate submissionDate;

    @Column(name = "amount", nullable = false)
    @NotNull(message = "The amount cannot be null")
    @Positive(message = "The amount must be positive")
    private Double amount;

    @Column(name = "fuel_type", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "The fuel type cannot be null")
    private FuelType fuelType;
}

