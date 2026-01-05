package com.pepotec.cooperative_taxi_managment.models.dto.accountmovement.income;

import com.pepotec.cooperative_taxi_managment.models.dto.incometype.IncomeTypeDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * DTO para representar un ingreso de cuenta completo.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountIncomeDTO {
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
    private Integer currentInstallment;
    private Integer finalInstallment;
    private Boolean active;
    
    private IncomeTypeDTO incomeType;
}

