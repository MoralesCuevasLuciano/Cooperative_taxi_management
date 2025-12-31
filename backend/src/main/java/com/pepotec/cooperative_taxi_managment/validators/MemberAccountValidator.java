package com.pepotec.cooperative_taxi_managment.validators;

import com.pepotec.cooperative_taxi_managment.exceptions.InvalidDataException;
import com.pepotec.cooperative_taxi_managment.models.dto.person.member.account.MemberAccountDTO;
import com.pepotec.cooperative_taxi_managment.repositories.MemberAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class MemberAccountValidator {

    @Autowired
    private MemberAccountRepository memberAccountRepository;

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

    /**
     * Valida que el ID no sea nulo (para updates y deletes).
     * @param id ID a validar
     */
    public void validateIdNotNull(Long id) {
        if (id == null) {
            throw new InvalidDataException("The ID cannot be null");
        }
    }

    /**
     * Valida que el ID no sea nulo para actualizar.
     * @param id ID a validar
     */
    public void validateIdNotNullForUpdate(Long id) {
        if (id == null) {
            throw new InvalidDataException("The account ID cannot be null for update");
        }
    }

    /**
     * Valida que el member ID no sea nulo.
     * @param memberId ID del miembro a validar
     */
    public void validateMemberIdNotNull(Long memberId) {
        if (memberId == null) {
            throw new InvalidDataException("The member ID cannot be null");
        }
    }

    /**
     * Valida la unicidad: que el miembro no tenga ya una cuenta asociada.
     * @param memberId ID del miembro
     */
    public void validateUniqueMemberAccount(Long memberId) {
        memberAccountRepository.findByMemberId(memberId).ifPresent(existing -> {
            throw new InvalidDataException("The member already has an associated account");
        });
    }
}


