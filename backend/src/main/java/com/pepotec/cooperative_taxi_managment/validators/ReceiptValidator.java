package com.pepotec.cooperative_taxi_managment.validators;

import com.pepotec.cooperative_taxi_managment.exceptions.InvalidDataException;
import com.pepotec.cooperative_taxi_managment.models.dto.receipt.ReceiptCreateDTO;
import com.pepotec.cooperative_taxi_managment.models.entities.MemberAccountEntity;
import com.pepotec.cooperative_taxi_managment.models.entities.ReceiptEntity;
import com.pepotec.cooperative_taxi_managment.models.entities.SubscriberAccountEntity;
import com.pepotec.cooperative_taxi_managment.models.enums.ReceiptType;
import com.pepotec.cooperative_taxi_managment.repositories.ReceiptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.YearMonth;

/**
 * Validator para recibos físicos.
 * Valida que solo haya una cuenta asociada, que el tipo coincida con la cuenta,
 * y que los campos obligatorios estén presentes.
 */
@Component
public class ReceiptValidator {

    @Autowired
    private ReceiptRepository receiptRepository;

    /**
     * Valida un DTO de creación de recibo.
     * @param dto DTO a validar
     */
    public void validateCreateFields(ReceiptCreateDTO dto) {
        // Validar que solo haya una cuenta
        validateOnlyOneAccount(dto.getMemberAccountId(), dto.getSubscriberAccountId());
        
        // Validar que el tipo de recibo coincida con la cuenta
        validateReceiptTypeMatchesAccount(dto.getReceiptType(), dto.getMemberAccountId(), dto.getSubscriberAccountId());
        
        // Validar campos obligatorios
        if (dto.getReceiptNumber() == null || dto.getReceiptNumber() <= 0) {
            throw new InvalidDataException("The receipt number must be positive");
        }
        if (dto.getBookletNumber() == null || dto.getBookletNumber() <= 0) {
            throw new InvalidDataException("The booklet number must be positive");
        }
        if (dto.getReceiptType() == null) {
            throw new InvalidDataException("The receipt type cannot be null");
        }
        if (dto.getYearMonth() == null) {
            throw new InvalidDataException("The period (yearMonth) cannot be null");
        }
        if (dto.getIssueDate() == null) {
            throw new InvalidDataException("The issue date cannot be null");
        }
        
        // Validar formato de período
        validatePeriodFormat(dto.getYearMonth());
    }

    /**
     * Valida una entidad de recibo.
     * @param receipt Entidad a validar
     */
    public void validate(ReceiptEntity receipt) {
        // Validar que solo haya una cuenta
        validateOnlyOneAccount(receipt.getMemberAccount(), receipt.getSubscriberAccount());
        
        // Validar que el tipo de recibo coincida con la cuenta
        validateReceiptTypeMatchesAccount(receipt.getReceiptType(), 
                                         receipt.getMemberAccount(), 
                                         receipt.getSubscriberAccount());
        
        // Validar formato de período
        if (receipt.getYearMonth() != null) {
            validatePeriodFormat(receipt.getYearMonth());
        }
    }

    /**
     * Valida que solo haya una cuenta asociada (o ninguna).
     */
    private void validateOnlyOneAccount(Long memberAccountId, Long subscriberAccountId) {
        int accountCount = 0;
        if (memberAccountId != null) accountCount++;
        if (subscriberAccountId != null) accountCount++;
        
        if (accountCount > 1) {
            throw new InvalidDataException("A receipt can only have one account associated. Found " + accountCount + " accounts.");
        }
        if (accountCount == 0) {
            throw new InvalidDataException("A receipt must have one account associated (memberAccount or subscriberAccount)");
        }
    }

    /**
     * Valida que solo haya una cuenta asociada (o ninguna) - versión con entidades.
     */
    private void validateOnlyOneAccount(MemberAccountEntity memberAccount, SubscriberAccountEntity subscriberAccount) {
        int accountCount = 0;
        if (memberAccount != null) accountCount++;
        if (subscriberAccount != null) accountCount++;
        
        if (accountCount > 1) {
            throw new InvalidDataException("A receipt can only have one account associated. Found " + accountCount + " accounts.");
        }
        if (accountCount == 0) {
            throw new InvalidDataException("A receipt must have one account associated (memberAccount or subscriberAccount)");
        }
    }

    /**
     * Valida que el tipo de recibo coincida con la cuenta asociada.
     */
    private void validateReceiptTypeMatchesAccount(ReceiptType receiptType, Long memberAccountId, Long subscriberAccountId) {
        if (receiptType == null) {
            return; // Ya se valida en otro lugar
        }
        
        if (receiptType == ReceiptType.MEMBER && memberAccountId == null) {
            throw new InvalidDataException("Receipt type MEMBER requires a memberAccount");
        }
        if (receiptType == ReceiptType.SUBSCRIBER && subscriberAccountId == null) {
            throw new InvalidDataException("Receipt type SUBSCRIBER requires a subscriberAccount");
        }
        if (receiptType == ReceiptType.MEMBER && subscriberAccountId != null) {
            throw new InvalidDataException("Receipt type MEMBER cannot have a subscriberAccount");
        }
        if (receiptType == ReceiptType.SUBSCRIBER && memberAccountId != null) {
            throw new InvalidDataException("Receipt type SUBSCRIBER cannot have a memberAccount");
        }
    }

    /**
     * Valida que el tipo de recibo coincida con la cuenta asociada - versión con entidades.
     */
    private void validateReceiptTypeMatchesAccount(ReceiptType receiptType, 
                                                  MemberAccountEntity memberAccount, 
                                                  SubscriberAccountEntity subscriberAccount) {
        if (receiptType == null) {
            return; // Ya se valida en otro lugar
        }
        
        if (receiptType == ReceiptType.MEMBER && memberAccount == null) {
            throw new InvalidDataException("Receipt type MEMBER requires a memberAccount");
        }
        if (receiptType == ReceiptType.SUBSCRIBER && subscriberAccount == null) {
            throw new InvalidDataException("Receipt type SUBSCRIBER requires a subscriberAccount");
        }
        if (receiptType == ReceiptType.MEMBER && subscriberAccount != null) {
            throw new InvalidDataException("Receipt type MEMBER cannot have a subscriberAccount");
        }
        if (receiptType == ReceiptType.SUBSCRIBER && memberAccount != null) {
            throw new InvalidDataException("Receipt type SUBSCRIBER cannot have a memberAccount");
        }
    }

    /**
     * Valida que el YearMonth tenga el formato correcto.
     * 
     * @param yearMonth YearMonth a validar
     * @throws InvalidDataException si el formato es inválido
     */
    public void validatePeriodFormat(YearMonth yearMonth) {
        if (yearMonth == null) {
            throw new InvalidDataException("The period (yearMonth) cannot be null");
        }
        // YearMonth ya valida el formato internamente, solo verificamos que no sea null
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

    /**
     * Valida la unicidad de cuenta + period.
     * @param memberAccountId ID de la cuenta de socio (puede ser null)
     * @param subscriberAccountId ID de la cuenta de abonado (puede ser null)
     * @param period Período en formato String "YYYY-MM"
     * @param excludeId ID a excluir de la validación (para updates, null para creates)
     */
    public void validateUniqueAccountPeriod(Long memberAccountId, Long subscriberAccountId, String period, Long excludeId) {
        if (memberAccountId != null) {
            receiptRepository.findByMemberAccountIdAndPeriod(memberAccountId, period)
                    .ifPresent(receipt -> {
                        if (excludeId == null || !receipt.getId().equals(excludeId)) {
                            throw new InvalidDataException("A receipt already exists for this member account and period");
                        }
                    });
        } else if (subscriberAccountId != null) {
            receiptRepository.findBySubscriberAccountIdAndPeriod(subscriberAccountId, period)
                    .ifPresent(receipt -> {
                        if (excludeId == null || !receipt.getId().equals(excludeId)) {
                            throw new InvalidDataException("A receipt already exists for this subscriber account and period");
                        }
                    });
        }
    }

    /**
     * Valida la unicidad de receiptNumber + bookletNumber + receiptType.
     * @param receiptNumber Número del recibo
     * @param bookletNumber Número del talonario
     * @param receiptType Tipo de recibo (MEMBER o SUBSCRIBER)
     * @param excludeId ID a excluir de la validación (para updates, null para creates)
     */
    public void validateUniqueReceiptBooklet(Integer receiptNumber, Integer bookletNumber, ReceiptType receiptType, Long excludeId) {
        receiptRepository.findByReceiptNumberAndBookletNumberAndReceiptType(receiptNumber, bookletNumber, receiptType)
                .ifPresent(receipt -> {
                    if (excludeId == null || !receipt.getId().equals(excludeId)) {
                        throw new InvalidDataException("A receipt with this receipt number, booklet number and receipt type already exists");
                    }
                });
    }
}

