package com.pepotec.cooperative_taxi_managment.models.dto.payrollsettlement;

import com.pepotec.cooperative_taxi_managment.models.dto.advance.AdvanceDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PayrollSettlementDTO {
    private Long id;
    private Long memberAccountId;
    private Double grossSalary;
    private Double netSalary;
    private YearMonth yearMonth;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate paymentDate;
    private Boolean active;
    private List<AdvanceDTO> advances;
}


