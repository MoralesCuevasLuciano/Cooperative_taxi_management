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

    public void validateCreateFields(Double grossSalary, Double netSalary, YearMonth yearMonth) {
        if (grossSalary == null || grossSalary < 0) {
            throw new InvalidDataException("The gross salary must be >= 0");
        }
        if (netSalary == null || netSalary < 0) {
            throw new InvalidDataException("The net salary must be >= 0");
        }
        if (yearMonth == null) {
            throw new InvalidDataException("The period (yearMonth) cannot be null");
        }
    }

    public void validatePaymentDate(LocalDate paymentDate) {
        // paymentDate can be null; no extra validation for now.
    }
}


