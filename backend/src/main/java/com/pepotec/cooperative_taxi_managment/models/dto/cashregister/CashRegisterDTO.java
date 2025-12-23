package com.pepotec.cooperative_taxi_managment.models.dto.cashregister;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para representar la caja física (singleton).
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CashRegisterDTO {
    private Long id;
    
    @NotNull(message = "The amount cannot be null")
    private Double amount;
    
    @NotNull(message = "The active status cannot be null")
    private Boolean active;
}




