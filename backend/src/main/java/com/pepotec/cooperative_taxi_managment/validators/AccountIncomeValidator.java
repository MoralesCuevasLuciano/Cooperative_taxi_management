package com.pepotec.cooperative_taxi_managment.validators;

import com.pepotec.cooperative_taxi_managment.exceptions.InvalidDataException;
import com.pepotec.cooperative_taxi_managment.models.dto.accountmovement.income.AccountIncomeCreateDTO;
import com.pepotec.cooperative_taxi_managment.models.entities.AccountIncomeEntity;
import com.pepotec.cooperative_taxi_managment.models.entities.IncomeTypeEntity;
import com.pepotec.cooperative_taxi_managment.repositories.AccountIncomeRepository;
import com.pepotec.cooperative_taxi_managment.repositories.IncomeTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.YearMonth;

/**
 * Validator para ingresos de cuenta.
 */
@Component
public class AccountIncomeValidator {

    @Autowired
    private AccountIncomeRepository accountIncomeRepository;

    @Autowired
    private IncomeTypeRepository incomeTypeRepository;

    /**
     * Valida los campos de creación de un ingreso de cuenta.
     * @param dto DTO a validar
     */
    public void validateCreateFields(AccountIncomeCreateDTO dto) {
        // Validar que solo haya una cuenta
        validateOnlyOneAccount(dto.getMemberAccountId(), dto.getSubscriberAccountId(), dto.getVehicleAccountId());
        
        // Validar campos obligatorios
        if (dto.getAmount() == null || dto.getAmount() <= 0) {
            throw new InvalidDataException("The amount must be positive");
        }
        
        // Validar formato de período
        validatePeriodFormat(dto.getYearMonth());
        
        // Validar cuotas si están presentes
        validateInstallments(dto.getCurrentInstallment(), dto.getFinalInstallment());
    }

    /**
     * Valida una entidad de ingreso de cuenta.
     * @param accountIncome Entidad a validar
     */
    public void validate(AccountIncomeEntity accountIncome) {
        // Validar que solo haya una cuenta
        validateOnlyOneAccount(
            accountIncome.getMemberAccount() != null ? accountIncome.getMemberAccount().getId() : null,
            accountIncome.getSubscriberAccount() != null ? accountIncome.getSubscriberAccount().getId() : null,
            accountIncome.getVehicleAccount() != null ? accountIncome.getVehicleAccount().getId() : null
        );
        
        // Validar formato de período
        if (accountIncome.getYearMonth() != null) {
            validatePeriodFormat(accountIncome.getYearMonth());
        }
        
        // Validar cuotas si están presentes
        validateInstallments(accountIncome.getCurrentInstallment(), accountIncome.getFinalInstallment());
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
            throw new InvalidDataException("An account income can only have one account associated. Found " + accountCount + " accounts.");
        }
        if (accountCount == 0) {
            throw new InvalidDataException("An account income must have one account associated (memberAccount, subscriberAccount or vehicleAccount)");
        }
    }

    /**
     * Valida el formato del período YYYY-MM.
     */
    public void validatePeriodFormat(String period) {
        if (period == null || period.isBlank()) {
            throw new InvalidDataException("The period cannot be null or empty");
        }
        if (!period.matches("^\\d{4}-(0[1-9]|1[0-2])$")) {
            throw new InvalidDataException("The period must be in format YYYY-MM (e.g., 2024-12). Received: " + period);
        }
        try {
            YearMonth.parse(period);
        } catch (Exception e) {
            throw new InvalidDataException("The period must be a valid year-month. Received: " + period);
        }
    }

    /**
     * Valida las cuotas (currentInstallment y finalInstallment).
     */
    private void validateInstallments(Integer currentInstallment, Integer finalInstallment) {
        if (currentInstallment != null && finalInstallment == null) {
            throw new InvalidDataException("If currentInstallment is provided, finalInstallment must also be provided");
        }
        if (currentInstallment == null && finalInstallment != null) {
            throw new InvalidDataException("If finalInstallment is provided, currentInstallment must also be provided");
        }
        if (currentInstallment != null && finalInstallment != null) {
            if (currentInstallment > finalInstallment) {
                throw new InvalidDataException("currentInstallment (" + currentInstallment + ") cannot be greater than finalInstallment (" + finalInstallment + ")");
            }
            if (currentInstallment < 1) {
                throw new InvalidDataException("currentInstallment must be >= 1");
            }
            if (finalInstallment < 1) {
                throw new InvalidDataException("finalInstallment must be >= 1");
            }
        }
    }

    /**
     * Valida la unicidad de account + yearMonth + incomeType.
     * Solo aplica cuando el tipo tiene monthlyRecurrence = true.
     * @param memberAccountId ID de la cuenta de socio (puede ser null)
     * @param subscriberAccountId ID de la cuenta de abonado (puede ser null)
     * @param vehicleAccountId ID de la cuenta de vehículo (puede ser null)
     * @param period Período en formato String "YYYY-MM"
     * @param incomeTypeId ID del tipo de ingreso
     * @param excludeId ID a excluir de la validación (para updates, null para creates)
     */
    public void validateUniqueAccountPeriodIncomeType(Long memberAccountId, Long subscriberAccountId, Long vehicleAccountId, 
                                                      String period, Long incomeTypeId, Long excludeId) {
        if (incomeTypeId == null) {
            return; // No hay tipo, no se valida unicidad
        }
        
        IncomeTypeEntity incomeType = incomeTypeRepository.findById(incomeTypeId)
            .orElseThrow(() -> new InvalidDataException("Income type not found with id: " + incomeTypeId));
        
        // Solo validar unicidad si monthlyRecurrence = true
        if (!incomeType.getMonthlyRecurrence()) {
            return;
        }
        
        if (memberAccountId != null) {
            accountIncomeRepository.findByMemberAccountIdAndPeriodAndIncomeTypeId(memberAccountId, period, incomeTypeId)
                .ifPresent(existing -> {
                    if (excludeId == null || !existing.getId().equals(excludeId)) {
                        throw new InvalidDataException("An account income already exists for this member account, period and income type (with monthly recurrence)");
                    }
                });
        } else if (subscriberAccountId != null) {
            accountIncomeRepository.findBySubscriberAccountIdAndPeriodAndIncomeTypeId(subscriberAccountId, period, incomeTypeId)
                .ifPresent(existing -> {
                    if (excludeId == null || !existing.getId().equals(excludeId)) {
                        throw new InvalidDataException("An account income already exists for this subscriber account, period and income type (with monthly recurrence)");
                    }
                });
        } else if (vehicleAccountId != null) {
            accountIncomeRepository.findByVehicleAccountIdAndPeriodAndIncomeTypeId(vehicleAccountId, period, incomeTypeId)
                .ifPresent(existing -> {
                    if (excludeId == null || !existing.getId().equals(excludeId)) {
                        throw new InvalidDataException("An account income already exists for this vehicle account, period and income type (with monthly recurrence)");
                    }
                });
        }
    }
}

