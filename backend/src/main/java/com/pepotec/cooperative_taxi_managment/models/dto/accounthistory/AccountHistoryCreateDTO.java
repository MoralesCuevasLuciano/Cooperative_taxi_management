package com.pepotec.cooperative_taxi_managment.models.dto.accounthistory;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.YearMonth;

/**
 * DTO para crear un nuevo historial de cuenta.
 * Solo uno de memberAccountId, subscriberAccountId o vehicleAccountId debe estar presente.
 * 
 * NOTA: Normalmente el historial se genera automáticamente al inicio de cada mes,
 * pero este DTO permite crearlo manualmente si es necesario.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountHistoryCreateDTO {
    
    // Solo uno de estos debe estar presente (validación en el validator)
    private Long memberAccountId;
    private Long subscriberAccountId;
    private Long vehicleAccountId;
    
    @NotNull(message = "The period (yearMonth) cannot be null")
    private YearMonth yearMonth; // Período al que corresponde el cierre
    
    @NotNull(message = "The registration date cannot be null")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate registrationDate; // Fecha exacta de registro
    
    @NotNull(message = "The month end balance cannot be null")
    private Double monthEndBalance; // Saldo de cierre del mes (puede ser negativo)
}

