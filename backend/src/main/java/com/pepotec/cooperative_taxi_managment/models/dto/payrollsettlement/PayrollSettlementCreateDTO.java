package com.pepotec.cooperative_taxi_managment.models.dto.payrollsettlement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PayrollSettlementCreateDTO {

    @NotNull(message = "The member account ID cannot be null")
    private Long memberAccountId;

    @NotNull(message = "The gross salary cannot be null")
    @PositiveOrZero(message = "The gross salary must be >= 0")
    private Double grossSalary;

    @NotNull(message = "The net salary cannot be null")
    @PositiveOrZero(message = "The net salary must be >= 0")
    private Double netSalary;

    @NotNull(message = "The period (yearMonth) cannot be null")
    private YearMonth yearMonth;

    // Nullable: null => no pagado
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate paymentDate;

    // Opcional: IDs de vales a asociar (deben no tener liquidación previa)
    private List<Long> advanceIds;
}


