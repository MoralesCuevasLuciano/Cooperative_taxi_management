package com.pepotec.cooperative_taxi_managment.models.dto.receipt;

import com.pepotec.cooperative_taxi_managment.models.enums.ReceiptType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.YearMonth;

/**
 * DTO para representar un recibo completo.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReceiptDTO {
    private Long id;
    
    // Solo uno de estos será no-null
    private Long memberAccountId;
    private Long subscriberAccountId;
    
    private Integer receiptNumber;
    private Integer bookletNumber;
    private ReceiptType receiptType;
    private YearMonth yearMonth; // Período del recibo
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate issueDate; // Fecha de emisión
    private Boolean active;
}

