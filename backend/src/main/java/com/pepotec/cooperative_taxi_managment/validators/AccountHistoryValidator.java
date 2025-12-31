package com.pepotec.cooperative_taxi_managment.validators;

import com.pepotec.cooperative_taxi_managment.exceptions.InvalidDataException;
import com.pepotec.cooperative_taxi_managment.models.dto.accounthistory.AccountHistoryCreateDTO;
import com.pepotec.cooperative_taxi_managment.models.entities.AccountHistoryEntity;
import com.pepotec.cooperative_taxi_managment.models.entities.MemberAccountEntity;
import com.pepotec.cooperative_taxi_managment.models.entities.SubscriberAccountEntity;
import com.pepotec.cooperative_taxi_managment.models.entities.VehicleAccountEntity;
import com.pepotec.cooperative_taxi_managment.repositories.AccountHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.YearMonth;

/**
 * Validator para historiales de cuentas.
 * Valida que solo haya una cuenta asociada y que los campos obligatorios estén presentes.
 */
@Component
public class AccountHistoryValidator {

    @Autowired
    private AccountHistoryRepository accountHistoryRepository;

    /**
     * Valida un DTO de creación de historial de cuenta.
     * @param dto DTO a validar
     */
    public void validateCreateFields(AccountHistoryCreateDTO dto) {
        // Validar que solo haya una cuenta
        validateOnlyOneAccount(dto.getMemberAccountId(), dto.getSubscriberAccountId(), dto.getVehicleAccountId());
        
        // Validar campos obligatorios
        if (dto.getYearMonth() == null) {
            throw new InvalidDataException("The period (yearMonth) cannot be null");
        }
        if (dto.getRegistrationDate() == null) {
            throw new InvalidDataException("The registration date cannot be null");
        }
        if (dto.getMonthEndBalance() == null) {
            throw new InvalidDataException("The month end balance cannot be null");
        }
        
        // Validar formato de período
        validatePeriodFormat(dto.getYearMonth());
    }

    /**
     * Valida una entidad de historial de cuenta.
     * @param accountHistory Entidad a validar
     */
    public void validate(AccountHistoryEntity accountHistory) {
        // Validar que solo haya una cuenta
        validateOnlyOneAccount(accountHistory.getMemberAccount(), 
                              accountHistory.getSubscriberAccount(), 
                              accountHistory.getVehicleAccount());
        
        // Validar formato de período
        if (accountHistory.getYearMonth() != null) {
            validatePeriodFormat(accountHistory.getYearMonth());
        }
    }

    /**
     * Valida que solo haya una cuenta asociada (o ninguna).
     */
    private void validateOnlyOneAccount(Long memberAccountId, Long subscriberAccountId, Long vehicleAccountId) {
        int accountCount = 0;
        if (memberAccountId != null) accountCount++;
        if (subscriberAccountId != null) accountCount++;
        if (vehicleAccountId != null) accountCount++;
        
        if (accountCount > 1) {
            throw new InvalidDataException("An account history can only have one account associated. Found " + accountCount + " accounts.");
        }
        if (accountCount == 0) {
            throw new InvalidDataException("An account history must have one account associated (memberAccount, subscriberAccount or vehicleAccount)");
        }
    }

    /**
     * Valida que solo haya una cuenta asociada (o ninguna) - versión con entidades.
     */
    private void validateOnlyOneAccount(MemberAccountEntity memberAccount, 
                                       SubscriberAccountEntity subscriberAccount, 
                                       VehicleAccountEntity vehicleAccount) {
        int accountCount = 0;
        if (memberAccount != null) accountCount++;
        if (subscriberAccount != null) accountCount++;
        if (vehicleAccount != null) accountCount++;
        
        if (accountCount > 1) {
            throw new InvalidDataException("An account history can only have one account associated. Found " + accountCount + " accounts.");
        }
        if (accountCount == 0) {
            throw new InvalidDataException("An account history must have one account associated (memberAccount, subscriberAccount or vehicleAccount)");
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
     * @param vehicleAccountId ID de la cuenta de vehículo (puede ser null)
     * @param period Período en formato String "YYYY-MM"
     * @param excludeId ID a excluir de la validación (para updates, null para creates)
     */
    public void validateUniqueAccountPeriod(Long memberAccountId, Long subscriberAccountId, Long vehicleAccountId, String period, Long excludeId) {
        if (memberAccountId != null) {
            accountHistoryRepository.findByMemberAccountIdAndPeriod(memberAccountId, period)
                    .ifPresent(history -> {
                        if (excludeId == null || !history.getId().equals(excludeId)) {
                            throw new InvalidDataException("An account history already exists for this member account and period");
                        }
                    });
        } else if (subscriberAccountId != null) {
            accountHistoryRepository.findBySubscriberAccountIdAndPeriod(subscriberAccountId, period)
                    .ifPresent(history -> {
                        if (excludeId == null || !history.getId().equals(excludeId)) {
                            throw new InvalidDataException("An account history already exists for this subscriber account and period");
                        }
                    });
        } else if (vehicleAccountId != null) {
            accountHistoryRepository.findByVehicleAccountIdAndPeriod(vehicleAccountId, period)
                    .ifPresent(history -> {
                        if (excludeId == null || !history.getId().equals(excludeId)) {
                            throw new InvalidDataException("An account history already exists for this vehicle account and period");
                        }
                    });
        }
    }
}

