package com.pepotec.cooperative_taxi_managment.models.dto.accounthistory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.YearMonth;

/**
 * DTO para representar un historial de cuenta completo.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountHistoryDTO {
    private Long id;
    
    // Solo uno de estos será no-null
    private Long memberAccountId;
    private Long subscriberAccountId;
    private Long vehicleAccountId;
    
    private YearMonth yearMonth; // Período al que corresponde el cierre
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate registrationDate; // Fecha exacta de registro
    private Double monthEndBalance; // Saldo de cierre del mes (puede ser negativo)
    private Boolean active;
}

