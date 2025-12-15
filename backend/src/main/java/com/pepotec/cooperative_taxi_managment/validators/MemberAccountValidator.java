package com.pepotec.cooperative_taxi_managment.validators;

import com.pepotec.cooperative_taxi_managment.exceptions.InvalidDataException;
import com.pepotec.cooperative_taxi_managment.models.dto.person.member.account.MemberAccountDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class MemberAccountValidator {

    public void validateMemberAccountFields(MemberAccountDTO account) {
        if (account == null) {
            throw new InvalidDataException("The account cannot be null");
        }

        if (account.getMemberId() == null) {
            throw new InvalidDataException("The member id cannot be null");
        }

        if (account.getBalance() == null) {
            throw new InvalidDataException("The balance cannot be null");
        }

        if (account.getLastModified() != null && account.getLastModified().isAfter(LocalDate.now())) {
            throw new InvalidDataException("The last modified date cannot be in the future");
        }
    }

    /**
     * Validaciones para creación de cuentas (ID del miembro viene por path).
     */
    public void validateMemberAccountCreateFields(Double balance, LocalDate lastModified) {
        if (balance == null) {
            throw new InvalidDataException("The balance cannot be null");
        }

        if (lastModified != null && lastModified.isAfter(LocalDate.now())) {
            throw new InvalidDataException("The last modified date cannot be in the future");
        }
    }
}


