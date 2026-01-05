package com.pepotec.cooperative_taxi_managment.models.dto.accountmovement.expense.monthly;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para crear un nuevo gasto mensual.
 * Solo uno de memberAccountId, subscriberAccountId o vehicleAccountId debe estar presente.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MonthlyExpenseCreateDTO {
    
    // Solo uno de estos debe estar presente (validación en el validator)
    private Long memberAccountId;
    private Long subscriberAccountId;
    private Long vehicleAccountId;
    
    @NotNull(message = "The amount cannot be null")
    @Positive(message = "The amount must be positive")
    private Double amount;
    
    @NotNull(message = "The period (yearMonth) cannot be null")
    @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])$", message = "The period must be in format YYYY-MM (e.g., 2024-12)")
    private String yearMonth; // Formato YYYY-MM
    
    private String note;
    
    private Integer currentInstallment;
    private Integer finalInstallment;
    
    private Long expenseTypeId; // ID del tipo de gasto (nullable)
}

