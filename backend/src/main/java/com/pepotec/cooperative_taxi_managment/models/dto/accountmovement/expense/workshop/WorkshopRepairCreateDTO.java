package com.pepotec.cooperative_taxi_managment.models.dto.accountmovement.expense.workshop;

import com.pepotec.cooperative_taxi_managment.models.enums.RepairType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para crear un nuevo arreglo de taller.
 * Solo uno de memberAccountId, subscriberAccountId o vehicleAccountId debe estar presente.
 * NO puede tener cuotas (currentInstallment y finalInstallment deben ser null).
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkshopRepairCreateDTO {
    
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
    
    @NotNull(message = "The repair type cannot be null")
    private RepairType repairType;
    
    // Opcional: si no se proporciona, se inicializa con amount en el servicio
    @Min(value = 0, message = "The remaining balance cannot be negative")
    private Double remainingBalance; // Se inicializa automáticamente con amount si es null
}

