package com.pepotec.cooperative_taxi_managment.models.dto.cashregister;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

/**
 * DTO para representar el historial diario de la caja.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CashRegisterHistoryDTO {
    private Long id;
    
    private CashRegisterDTO cashRegister;
    
    @NotNull(message = "The initial amount cannot be null")
    private Double initialAmount;
    
    // Puede ser null si el día aún no se cerró
    private Double finalAmount;
    
    @NotNull(message = "The date cannot be null")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate date;
}

