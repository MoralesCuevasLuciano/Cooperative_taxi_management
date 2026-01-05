package com.pepotec.cooperative_taxi_managment.models.dto.expensetype;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para crear un nuevo tipo de gasto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExpenseTypeCreateDTO {
    
    @NotBlank(message = "The name cannot be empty")
    @Size(min = 3, max = 100, message = "The name must be between 3 and 100 characters")
    private String name;
    
    @Builder.Default
    private Boolean monthlyRecurrence = false;
}

