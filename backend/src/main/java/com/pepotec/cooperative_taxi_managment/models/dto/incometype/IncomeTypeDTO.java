package com.pepotec.cooperative_taxi_managment.models.dto.incometype;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para representar un tipo de ingreso completo.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IncomeTypeDTO {
    private Long id;
    private String name;
    private Boolean monthlyRecurrence;
    private Boolean active;
}

