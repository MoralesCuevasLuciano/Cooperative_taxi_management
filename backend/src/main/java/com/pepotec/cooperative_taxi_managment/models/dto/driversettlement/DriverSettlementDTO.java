package com.pepotec.cooperative_taxi_managment.models.dto.driversettlement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pepotec.cooperative_taxi_managment.models.dto.person.member.driver.DriverDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DriverSettlementDTO {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    private Long driverId;

    /**
     * Información del chofer para respuestas.
     * No es requerida en las peticiones de creación/actualización.
     */
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @JsonInclude(JsonInclude.Include.NON_NULL)
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


