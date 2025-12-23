package com.pepotec.cooperative_taxi_managment.models.dto.advance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdvanceCreateDTO {
    @NotNull(message = "The member account ID cannot be null")
    private Long memberAccountId;

    @NotNull(message = "The date cannot be null")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate date;

    @NotNull(message = "The amount cannot be null")
    @Positive(message = "The amount must be greater than 0")
    private Double amount;

    private String notes;

    // Opcional: asociar a una liquidación existente
    private Long payrollSettlementId;
}


