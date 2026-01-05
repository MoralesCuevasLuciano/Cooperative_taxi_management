package com.pepotec.cooperative_taxi_managment.models.dto.expensetype;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para representar un tipo de gasto completo.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExpenseTypeDTO {
    private Long id;
    private String name;
    private Boolean monthlyRecurrence;
    private Boolean active;
}

