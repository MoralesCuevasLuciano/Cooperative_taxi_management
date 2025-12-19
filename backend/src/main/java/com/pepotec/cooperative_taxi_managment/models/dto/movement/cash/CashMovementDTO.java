package com.pepotec.cooperative_taxi_managment.models.dto.movement.cash;

import com.pepotec.cooperative_taxi_managment.models.dto.cashregister.CashRegisterDTO;
import com.pepotec.cooperative_taxi_managment.models.dto.person.member.account.MemberAccountDTO;
import com.pepotec.cooperative_taxi_managment.models.dto.person.subscriber.account.SubscriberAccountDTO;
import com.pepotec.cooperative_taxi_managment.models.dto.vehicle.account.VehicleAccountDTO;
import com.pepotec.cooperative_taxi_managment.models.enums.MovementType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

/**
 * DTO para representar un movimiento de dinero en efectivo.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CashMovementDTO {
    private Long id;
    
    // Solo una de estas cuentas puede estar presente (o todas null)
    private MemberAccountDTO memberAccount;
    private SubscriberAccountDTO subscriberAccount;
    private VehicleAccountDTO vehicleAccount;
    
    @NotNull(message = "The cash register cannot be null")
    private CashRegisterDTO cashRegister;
    
    @NotNull(message = "The description cannot be empty")
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
    
    @NotNull(message = "The active status cannot be null")
    private Boolean active;
}

