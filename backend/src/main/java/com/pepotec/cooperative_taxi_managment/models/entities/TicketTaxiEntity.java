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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Entity
@Table(name = "ticket_taxi")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketTaxiEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ticket_taxi", unique = true, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_vehicle", nullable = false)
    @NotNull(message = "The vehicle cannot be null")
    private VehicleEntity vehicle;

    @ManyToOne
    @JoinColumn(name = "id_settlement", nullable = false)
    @NotNull(message = "The settlement cannot be null")
    private DriverSettlementEntity settlement;

    @Column(name = "ticket_number")
    private String ticketNumber;

    @Column(name = "start_date")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate startDate;

    @Column(name = "cut_date")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate cutDate;

    @Column(name = "amount", nullable = false)
    @NotNull(message = "The amount cannot be null")
    @PositiveOrZero(message = "The amount must be greater than or equal to zero")
    private Double amount;

    @Column(name = "free_kilometers")
    @PositiveOrZero(message = "The free kilometers must be greater than or equal to zero")
    private Double freeKilometers;

    @Column(name = "occupied_kilometers")
    @PositiveOrZero(message = "The occupied kilometers must be greater than or equal to zero")
    private Double occupiedKilometers;

    @Column(name = "trips")
    @PositiveOrZero(message = "The trips must be greater than or equal to zero")
    private Integer trips;
}

