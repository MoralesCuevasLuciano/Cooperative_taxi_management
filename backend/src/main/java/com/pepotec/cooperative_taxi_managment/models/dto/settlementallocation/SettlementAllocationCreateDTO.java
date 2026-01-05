package com.pepotec.cooperative_taxi_managment.models.dto.settlementallocation;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * DTO para crear una nueva asignación de pago.
 * 
 * Solo uno de receiptId, payrollSettlementId o movementId debe estar presente (XOR).
 * La validación de XOR se hace en el validator.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SettlementAllocationCreateDTO {
    
    @NotNull(message = "The account movement ID cannot be null")
    private Long accountMovementId; // ID del movimiento de cuenta que se está saldando
    
    // XOR: Solo uno de estos debe estar presente (validación en el validator)
    private Long receiptId; // ID del recibo que salda (parcial o total)
    private Long payrollSettlementId; // ID de la liquidación que salda (parcial o total)
    private Long movementId; // ID del movimiento de dinero que salda (parcial o total)
    
    @NotNull(message = "The allocated amount cannot be null")
    @Positive(message = "The allocated amount must be positive")
    private Double allocatedAmount;
    
    @NotNull(message = "The allocation date cannot be null")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate allocationDate;
    
    private String note;
}

