package com.pepotec.cooperative_taxi_managment.validators;

import com.pepotec.cooperative_taxi_managment.exceptions.InvalidDataException;
import com.pepotec.cooperative_taxi_managment.models.dto.accountmovement.expense.workshop.WorkshopRepairCreateDTO;
import com.pepotec.cooperative_taxi_managment.models.entities.WorkshopRepairEntity;
import org.springframework.stereotype.Component;

import java.time.YearMonth;

/**
 * Validator para arreglos de taller.
 */
@Component
public class WorkshopRepairValidator {

    /**
     * Valida los campos de creación de un arreglo de taller.
     * @param dto DTO a validar
     */
    public void validateCreateFields(WorkshopRepairCreateDTO dto) {
        // Validar que solo haya una cuenta
        validateOnlyOneAccount(dto.getMemberAccountId(), dto.getSubscriberAccountId(), dto.getVehicleAccountId());
        
        // Validar campos obligatorios
        if (dto.getAmount() == null || dto.getAmount() <= 0) {
            throw new InvalidDataException("The amount must be positive");
        }
        
        if (dto.getRepairType() == null) {
            throw new InvalidDataException("The repair type cannot be null");
        }
        
        if (dto.getRemainingBalance() == null) {
            throw new InvalidDataException("The remaining balance cannot be null");
        }
        
        if (dto.getRemainingBalance() < 0) {
            throw new InvalidDataException("The remaining balance cannot be negative");
        }
        
        // Validar formato de período
        validatePeriodFormat(dto.getYearMonth());
        
        // Nota: WorkshopRepair no puede tener cuotas, pero el DTO no incluye esos campos
        // La validación de cuotas se hace en la entidad después de la creación
    }

    /**
     * Valida una entidad de arreglo de taller.
     * @param workshopRepair Entidad a validar
     */
    public void validate(WorkshopRepairEntity workshopRepair) {
        // Validar que solo haya una cuenta
        validateOnlyOneAccount(
            workshopRepair.getMemberAccount() != null ? workshopRepair.getMemberAccount().getId() : null,
            workshopRepair.getSubscriberAccount() != null ? workshopRepair.getSubscriberAccount().getId() : null,
            workshopRepair.getVehicleAccount() != null ? workshopRepair.getVehicleAccount().getId() : null
        );
        
        // Validar formato de período
        if (workshopRepair.getYearMonth() != null) {
            validatePeriodFormat(workshopRepair.getYearMonth());
        }
        
        // Validar remainingBalance
        if (workshopRepair.getRemainingBalance() != null && workshopRepair.getRemainingBalance() < 0) {
            throw new InvalidDataException("The remaining balance cannot be negative");
        }
        
        // Validar que NO tenga cuotas (WorkshopRepair no puede tener cuotas)
        if (workshopRepair.getCurrentInstallment() != null || workshopRepair.getFinalInstallment() != null) {
            throw new InvalidDataException("WorkshopRepair cannot have installments (currentInstallment and finalInstallment must be null)");
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
            throw new InvalidDataException("A workshop repair can only have one account associated. Found " + accountCount + " accounts.");
        }
        if (accountCount == 0) {
            throw new InvalidDataException("A workshop repair must have one account associated (memberAccount, subscriberAccount or vehicleAccount)");
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
}

