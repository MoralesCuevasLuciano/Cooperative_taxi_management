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
public class DriverSettlementDTO {
    private Long id;

    @Valid
    @NotNull(message = "The driver cannot be null")
    private DriverDTO driver;

    @NotNull(message = "The ticket amount cannot be null")
    @PositiveOrZero(message = "The ticket amount must be greater than or equal to zero")
    private Double ticketAmount;

    @NotNull(message = "The voucher amount cannot be null")
    @PositiveOrZero(message = "The voucher amount must be greater than or equal to zero")
    private Double voucherAmount;

    @NotNull(message = "The voucher difference cannot be null")
    private Double voucherDifference;

    @NotNull(message = "The final balance cannot be null")
    private Double finalBalance;

    @NotNull(message = "The submission date cannot be null")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate submissionDate;
}

