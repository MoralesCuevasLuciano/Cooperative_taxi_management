package com.pepotec.cooperative_taxi_managment.validators;

import com.pepotec.cooperative_taxi_managment.exceptions.InvalidDataException;
import com.pepotec.cooperative_taxi_managment.models.dto.accountmovement.expense.monthly.MonthlyExpenseCreateDTO;
import com.pepotec.cooperative_taxi_managment.models.entities.ExpenseTypeEntity;
import com.pepotec.cooperative_taxi_managment.models.entities.MonthlyExpenseEntity;
import com.pepotec.cooperative_taxi_managment.repositories.ExpenseTypeRepository;
import com.pepotec.cooperative_taxi_managment.repositories.MonthlyExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.YearMonth;

/**
 * Validator para gastos mensuales.
 */
@Component
public class MonthlyExpenseValidator {

    @Autowired
    private MonthlyExpenseRepository monthlyExpenseRepository;

    @Autowired
    private ExpenseTypeRepository expenseTypeRepository;

    /**
     * Valida los campos de creación de un gasto mensual.
     * @param dto DTO a validar
     */
    public void validateCreateFields(MonthlyExpenseCreateDTO dto) {
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
     * Valida una entidad de gasto mensual.
     * @param monthlyExpense Entidad a validar
     */
    public void validate(MonthlyExpenseEntity monthlyExpense) {
        // Validar que solo haya una cuenta
        validateOnlyOneAccount(
            monthlyExpense.getMemberAccount() != null ? monthlyExpense.getMemberAccount().getId() : null,
            monthlyExpense.getSubscriberAccount() != null ? monthlyExpense.getSubscriberAccount().getId() : null,
            monthlyExpense.getVehicleAccount() != null ? monthlyExpense.getVehicleAccount().getId() : null
        );
        
        // Validar formato de período
        if (monthlyExpense.getYearMonth() != null) {
            validatePeriodFormat(monthlyExpense.getYearMonth());
        }
        
        // Validar cuotas si están presentes
        validateInstallments(monthlyExpense.getCurrentInstallment(), monthlyExpense.getFinalInstallment());
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
            throw new InvalidDataException("A monthly expense can only have one account associated. Found " + accountCount + " accounts.");
        }
        if (accountCount == 0) {
            throw new InvalidDataException("A monthly expense must have one account associated (memberAccount, subscriberAccount or vehicleAccount)");
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
     * Valida la unicidad de account + yearMonth + expenseType.
     * Solo aplica cuando el tipo tiene monthlyRecurrence = true.
     * @param memberAccountId ID de la cuenta de socio (puede ser null)
     * @param subscriberAccountId ID de la cuenta de abonado (puede ser null)
     * @param vehicleAccountId ID de la cuenta de vehículo (puede ser null)
     * @param period Período en formato String "YYYY-MM"
     * @param expenseTypeId ID del tipo de gasto
     * @param excludeId ID a excluir de la validación (para updates, null para creates)
     */
    public void validateUniqueAccountPeriodExpenseType(Long memberAccountId, Long subscriberAccountId, Long vehicleAccountId, 
                                                       String period, Long expenseTypeId, Long excludeId) {
        if (expenseTypeId == null) {
            return; // No hay tipo, no se valida unicidad
        }
        
        ExpenseTypeEntity expenseType = expenseTypeRepository.findById(expenseTypeId)
            .orElseThrow(() -> new InvalidDataException("Expense type not found with id: " + expenseTypeId));
        
        // Solo validar unicidad si monthlyRecurrence = true
        if (!expenseType.getMonthlyRecurrence()) {
            return;
        }
        
        if (memberAccountId != null) {
            monthlyExpenseRepository.findByMemberAccountIdAndPeriodAndExpenseTypeId(memberAccountId, period, expenseTypeId)
                .ifPresent(existing -> {
                    if (excludeId == null || !existing.getId().equals(excludeId)) {
                        throw new InvalidDataException("A monthly expense already exists for this member account, period and expense type (with monthly recurrence)");
                    }
                });
        } else if (subscriberAccountId != null) {
            monthlyExpenseRepository.findBySubscriberAccountIdAndPeriodAndExpenseTypeId(subscriberAccountId, period, expenseTypeId)
                .ifPresent(existing -> {
                    if (excludeId == null || !existing.getId().equals(excludeId)) {
                        throw new InvalidDataException("A monthly expense already exists for this subscriber account, period and expense type (with monthly recurrence)");
                    }
                });
        } else if (vehicleAccountId != null) {
            monthlyExpenseRepository.findByVehicleAccountIdAndPeriodAndExpenseTypeId(vehicleAccountId, period, expenseTypeId)
                .ifPresent(existing -> {
                    if (excludeId == null || !existing.getId().equals(excludeId)) {
                        throw new InvalidDataException("A monthly expense already exists for this vehicle account, period and expense type (with monthly recurrence)");
                    }
                });
        }
    }
}

