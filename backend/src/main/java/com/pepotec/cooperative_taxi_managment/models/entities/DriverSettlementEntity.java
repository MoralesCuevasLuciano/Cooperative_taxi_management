package com.pepotec.cooperative_taxi_managment.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "driver_settlements")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverSettlementEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_settlement", unique = true, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_driver", nullable = false)
    @NotNull(message = "The driver cannot be null")
    private DriverEntity driver;

    @Column(name = "ticket_amount", nullable = false)
    @NotNull(message = "The ticket amount cannot be null")
    @PositiveOrZero(message = "The ticket amount must be greater than or equal to zero")
    private Double ticketAmount;

    @Column(name = "voucher_amount", nullable = false)
    @NotNull(message = "The voucher amount cannot be null")
    @PositiveOrZero(message = "The voucher amount must be greater than or equal to zero")
    private Double voucherAmount;

    @Column(name = "voucher_difference", nullable = false)
    @NotNull(message = "The voucher difference cannot be null")
    private Double voucherDifference;

    @Column(name = "final_balance", nullable = false)
    @NotNull(message = "The final balance cannot be null")
    private Double finalBalance;

    @Column(name = "submission_date", nullable = false)
    @NotNull(message = "The submission date cannot be null")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate submissionDate;
}

