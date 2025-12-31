package com.pepotec.cooperative_taxi_managment.models.dto.receipt;

import com.pepotec.cooperative_taxi_managment.models.enums.ReceiptType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.YearMonth;

/**
 * DTO para crear un nuevo recibo.
 * Solo uno de memberAccountId o subscriberAccountId debe estar presente.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReceiptCreateDTO {
    
    // Solo uno de estos debe estar presente (validación en el validator)
    private Long memberAccountId;
    private Long subscriberAccountId;
    
    @NotNull(message = "The receipt number cannot be null")
    @Positive(message = "The receipt number must be positive")
    private Integer receiptNumber;
    
    @NotNull(message = "The booklet number cannot be null")
    @Positive(message = "The booklet number must be positive")
    private Integer bookletNumber;
    
    @NotNull(message = "The receipt type cannot be null")
    private ReceiptType receiptType;
    
    @NotNull(message = "The period (yearMonth) cannot be null")
    private YearMonth yearMonth; // Período del recibo
    
    @NotNull(message = "The issue date cannot be null")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate issueDate; // Fecha de emisión
}

