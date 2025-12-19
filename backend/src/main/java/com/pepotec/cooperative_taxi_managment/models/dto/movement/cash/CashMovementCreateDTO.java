package com.pepotec.cooperative_taxi_managment.models.dto.movement.cash;

import com.pepotec.cooperative_taxi_managment.models.enums.MovementType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

/**
 * DTO usado para crear movimientos de dinero en efectivo.
 * El cashRegisterId se obtiene automáticamente del servicio (singleton).
 * Solo una de las cuentas puede estar presente (o todas null).
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CashMovementCreateDTO {
    
    // IDs opcionales de cuentas (solo una puede estar presente)
    private Long memberAccountId;
    private Long subscriberAccountId;
    private Long vehicleAccountId;
    
    @NotBlank(message = "The description cannot be empty")
    @Size(min = 3, max = 255, message = "The description must be between 3 and 255 characters")
    private String description;
    
    @NotNull(message = "The amount cannot be null")
    private Double amount;
    
    @NotNull(message = "The date cannot be null")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate date;
    
    @NotNull(message = "The movement type cannot be null")
    private MovementType movementType;
    
    @NotNull(message = "The isIncome flag cannot be null")
    private Boolean isIncome;
}

