package com.pepotec.cooperative_taxi_managment.models.dto.person.member.account;

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
public class MemberAccountCreateDTO {

    @NotNull(message = "The balance cannot be null")
    private Double balance;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate lastModified;
}


