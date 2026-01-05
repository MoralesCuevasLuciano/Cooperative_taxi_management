package com.pepotec.cooperative_taxi_managment.models.dto.accountmovement.expense.workshop;

import com.pepotec.cooperative_taxi_managment.models.enums.RepairType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * DTO para representar un arreglo de taller completo.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkshopRepairDTO {
    private Long id;
    
    // Solo uno de estos será no-null
    private Long memberAccountId;
    private Long subscriberAccountId;
    private Long vehicleAccountId;
    
    private Double amount;
    private String yearMonth; // Formato YYYY-MM
    private Boolean added;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate addedDate;
    private String note;
    private Boolean active;
    
    private RepairType repairType;
    private Double remainingBalance;
}

