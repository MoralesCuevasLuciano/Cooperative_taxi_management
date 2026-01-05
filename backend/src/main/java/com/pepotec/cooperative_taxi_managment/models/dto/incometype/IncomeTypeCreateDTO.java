package com.pepotec.cooperative_taxi_managment.models.dto.incometype;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para crear un nuevo tipo de ingreso.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IncomeTypeCreateDTO {
    
    @NotBlank(message = "The name cannot be empty")
    @Size(min = 3, max = 100, message = "The name must be between 3 and 100 characters")
    private String name;
    
    @Builder.Default
    private Boolean monthlyRecurrence = false;
}

