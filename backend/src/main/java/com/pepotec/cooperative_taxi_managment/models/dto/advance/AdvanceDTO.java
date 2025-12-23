package com.pepotec.cooperative_taxi_managment.models.dto.advance;

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
public class AdvanceDTO {
    private Long id;
    private Long memberAccountId;
    private Long payrollSettlementId; // puede ser null
    private Long movementId; // ID del movimiento (cash/non-cash) que lo originó

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate date;
    private Double amount;
    private String notes;
    private Boolean active;
}


