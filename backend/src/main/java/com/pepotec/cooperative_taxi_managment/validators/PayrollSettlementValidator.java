package com.pepotec.cooperative_taxi_managment.validators;

import com.pepotec.cooperative_taxi_managment.exceptions.InvalidDataException;
import com.pepotec.cooperative_taxi_managment.models.entities.MemberAccountEntity;
import com.pepotec.cooperative_taxi_managment.models.entities.MemberEntity;
import com.pepotec.cooperative_taxi_managment.models.enums.MemberRole;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.YearMonth;

@Component
public class PayrollSettlementValidator {

    public void validateMemberRole(MemberAccountEntity account) {
        MemberEntity member = account.getMember();
        if (member == null || member.getRole() == null) {
            throw new InvalidDataException("The member role cannot be null");
        }
        if (member.getRole() == MemberRole.DRIVER_1 || member.getRole() == MemberRole.DRIVER_2) {
            throw new InvalidDataException("Payroll settlement is only allowed for members that are not drivers");
        }
    }

    public void validateCreateFields(Double grossSalary, YearMonth yearMonth) {
        if (grossSalary == null || grossSalary < 0) {
            throw new InvalidDataException("The gross salary must be >= 0");
        }
        if (yearMonth == null) {
            throw new InvalidDataException("The period (yearMonth) cannot be null");
        }
    }

    /**
     * Valida que el String del período tenga el formato correcto YYYY-MM.
     * 
     * @param period String en formato YYYY-MM (ej: "2024-12")
     * @throws InvalidDataException si el formato es inválido
     */
    public void validatePeriodFormat(String period) {
        if (period == null || period.isBlank()) {
            throw new InvalidDataException("The period cannot be null or empty");
        }
        // Validar formato YYYY-MM
        if (!period.matches("^\\d{4}-(0[1-9]|1[0-2])$")) {
            throw new InvalidDataException("The period must be in format YYYY-MM (e.g., 2024-12). Received: " + period);
        }
        // Validar que sea un YearMonth válido
        try {
            YearMonth.parse(period);
        } catch (Exception e) {
            throw new InvalidDataException("The period must be a valid year-month. Received: " + period);
        }
    }

    public void validatePaymentDate(LocalDate paymentDate) {
        // paymentDate can be null; no extra validation for now.
    }
}


