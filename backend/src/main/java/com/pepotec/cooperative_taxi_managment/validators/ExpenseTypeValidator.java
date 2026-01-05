package com.pepotec.cooperative_taxi_managment.validators;

import com.pepotec.cooperative_taxi_managment.exceptions.DuplicateFieldException;
import com.pepotec.cooperative_taxi_managment.exceptions.InvalidDataException;
import com.pepotec.cooperative_taxi_managment.models.dto.expensetype.ExpenseTypeCreateDTO;
import com.pepotec.cooperative_taxi_managment.models.dto.expensetype.ExpenseTypeDTO;
import com.pepotec.cooperative_taxi_managment.models.entities.ExpenseTypeEntity;
import com.pepotec.cooperative_taxi_managment.repositories.ExpenseTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Validator para tipos de gasto.
 */
@Component
public class ExpenseTypeValidator {

    @Autowired
    private ExpenseTypeRepository expenseTypeRepository;

    /**
     * Valida los campos específicos de un tipo de gasto.
     * @param dto DTO a validar
     */
    public void validateExpenseTypeSpecificFields(ExpenseTypeCreateDTO dto) {
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new InvalidDataException("The name cannot be empty");
        }
        if (dto.getName().length() < 3 || dto.getName().length() > 100) {
            throw new InvalidDataException("The name must be between 3 and 100 characters");
        }
    }

    /**
     * Valida los campos específicos de un tipo de gasto (versión DTO completo).
     * @param dto DTO a validar
     */
    public void validateExpenseTypeSpecificFields(ExpenseTypeDTO dto) {
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new InvalidDataException("The name cannot be empty");
        }
        if (dto.getName().length() < 3 || dto.getName().length() > 100) {
            throw new InvalidDataException("The name must be between 3 and 100 characters");
        }
    }

    /**
     * Valida la unicidad del nombre del tipo de gasto.
     * @param dto DTO a validar
     * @param excludeId ID a excluir de la validación (para updates, null para creates)
     */
    public void validateUniqueFields(ExpenseTypeDTO dto, Long excludeId) {
        expenseTypeRepository.findByName(dto.getName())
            .ifPresent(existing -> {
                if (excludeId == null || !existing.getId().equals(excludeId)) {
                    throw new DuplicateFieldException(
                        "name",
                        dto.getName(),
                        "An expense type with the name '" + dto.getName() + "' already exists"
                    );
                }
            });
    }

    /**
     * Valida la unicidad del nombre del tipo de gasto (versión CreateDTO).
     * @param dto DTO a validar
     * @param excludeId ID a excluir de la validación (para updates, null para creates)
     */
    public void validateUniqueFields(ExpenseTypeCreateDTO dto, Long excludeId) {
        expenseTypeRepository.findByName(dto.getName())
            .ifPresent(existing -> {
                if (excludeId == null || !existing.getId().equals(excludeId)) {
                    throw new DuplicateFieldException(
                        "name",
                        dto.getName(),
                        "An expense type with the name '" + dto.getName() + "' already exists"
                    );
                }
            });
    }

    /**
     * Valida que el tipo de gasto no tenga movimientos asociados antes de eliminarlo.
     * @param expenseType Tipo de gasto a validar
     */
    public void validateCanDelete(ExpenseTypeEntity expenseType) {
        if (expenseType.getMonthlyExpenses() != null && !expenseType.getMonthlyExpenses().isEmpty()) {
            throw new InvalidDataException("Cannot delete expense type with associated monthly expenses. Deactivate it instead.");
        }
    }
}

