package com.pepotec.cooperative_taxi_managment.validators;

import com.pepotec.cooperative_taxi_managment.exceptions.InvalidDataException;
import com.pepotec.cooperative_taxi_managment.models.entities.MemberAccountEntity;
import com.pepotec.cooperative_taxi_managment.models.entities.MemberEntity;
import com.pepotec.cooperative_taxi_managment.models.enums.MemberRole;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class AdvanceValidator {

    public void validateMemberRole(MemberAccountEntity account) {
        MemberEntity member = account.getMember();
        if (member == null || member.getRole() == null) {
            throw new InvalidDataException("The member role cannot be null");
        }
        if (member.getRole() == MemberRole.DRIVER_1 || member.getRole() == MemberRole.DRIVER_2) {
            throw new InvalidDataException("ADVANCE is only allowed for members that are not drivers");
        }
    }

    public void validateCreateFields(Double amount, LocalDate date) {
        if (amount == null || amount <= 0) {
            throw new InvalidDataException("The amount must be greater than 0");
        }
        if (date == null) {
            throw new InvalidDataException("The date cannot be null");
        }
    }
}


