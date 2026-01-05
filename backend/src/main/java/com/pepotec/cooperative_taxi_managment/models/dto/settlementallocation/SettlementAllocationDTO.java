package com.pepotec.cooperative_taxi_managment.models.dto.settlementallocation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * DTO para representar una asignación de pago completa.
 * 
 * Solo uno de receiptId, payrollSettlementId o movementId será no-null (XOR).
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SettlementAllocationDTO {
    private Long id;
    
    private Long accountMovementId; // ID del movimiento de cuenta que se está saldando
    
    // XOR: Solo uno de estos será no-null
    private Long receiptId; // ID del recibo que salda (parcial o total)
    private Long payrollSettlementId; // ID de la liquidación que salda (parcial o total)
    private Long movementId; // ID del movimiento de dinero que salda (parcial o total)
    
    private Double allocatedAmount;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate allocationDate;
    private String note;
    private Boolean active;
}

