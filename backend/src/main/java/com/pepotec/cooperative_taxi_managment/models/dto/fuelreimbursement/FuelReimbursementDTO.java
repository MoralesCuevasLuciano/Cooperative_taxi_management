package com.pepotec.cooperative_taxi_managment.models.dto.fuelreimbursement;

import com.pepotec.cooperative_taxi_managment.models.dto.person.member.account.MemberAccountDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FuelReimbursementDTO {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotNull(message = "The member account id cannot be null")
    private Long memberAccountId;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private MemberAccountDTO memberAccount;

    @NotNull(message = "The accumulated amount cannot be null")
    private Double accumulatedAmount;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate lastReimbursementDate;

    @NotNull(message = "The created date cannot be null")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate createdDate;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Boolean active;
}

