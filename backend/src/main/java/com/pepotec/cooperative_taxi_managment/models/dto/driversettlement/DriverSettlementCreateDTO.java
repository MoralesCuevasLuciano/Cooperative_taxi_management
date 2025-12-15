package com.pepotec.cooperative_taxi_managment.models.dto.driversettlement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

/**
 * DTO usado exclusivamente para la creación de rendiciones de chofer.
 * El ID del chofer se obtiene del path (/drivers/{driverId}/settlements).
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DriverSettlementCreateDTO {

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



