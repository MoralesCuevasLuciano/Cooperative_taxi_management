package com.pepotec.cooperative_taxi_managment.validators;

import com.pepotec.cooperative_taxi_managment.exceptions.DuplicateFieldException;
import com.pepotec.cooperative_taxi_managment.exceptions.InvalidDataException;
import com.pepotec.cooperative_taxi_managment.models.dto.incometype.IncomeTypeCreateDTO;
import com.pepotec.cooperative_taxi_managment.models.dto.incometype.IncomeTypeDTO;
import com.pepotec.cooperative_taxi_managment.models.entities.IncomeTypeEntity;
import com.pepotec.cooperative_taxi_managment.repositories.IncomeTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Validator para tipos de ingreso.
 */
@Component
public class IncomeTypeValidator {

    @Autowired
    private IncomeTypeRepository incomeTypeRepository;

    /**
     * Valida los campos específicos de un tipo de ingreso.
     * @param dto DTO a validar
     */
    public void validateIncomeTypeSpecificFields(IncomeTypeCreateDTO dto) {
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new InvalidDataException("The name cannot be empty");
        }
        if (dto.getName().length() < 3 || dto.getName().length() > 100) {
            throw new InvalidDataException("The name must be between 3 and 100 characters");
        }
    }

    /**
     * Valida los campos específicos de un tipo de ingreso (versión DTO completo).
     * @param dto DTO a validar
     */
    public void validateIncomeTypeSpecificFields(IncomeTypeDTO dto) {
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new InvalidDataException("The name cannot be empty");
        }
        if (dto.getName().length() < 3 || dto.getName().length() > 100) {
            throw new InvalidDataException("The name must be between 3 and 100 characters");
        }
    }

    /**
     * Valida la unicidad del nombre del tipo de ingreso.
     * @param dto DTO a validar
     * @param excludeId ID a excluir de la validación (para updates, null para creates)
     */
    public void validateUniqueFields(IncomeTypeDTO dto, Long excludeId) {
        incomeTypeRepository.findByName(dto.getName())
            .ifPresent(existing -> {
                if (excludeId == null || !existing.getId().equals(excludeId)) {
                    throw new DuplicateFieldException(
                        "name",
                        dto.getName(),
                        "An income type with the name '" + dto.getName() + "' already exists"
                    );
                }
            });
    }

    /**
     * Valida la unicidad del nombre del tipo de ingreso (versión CreateDTO).
     * @param dto DTO a validar
     * @param excludeId ID a excluir de la validación (para updates, null para creates)
     */
    public void validateUniqueFields(IncomeTypeCreateDTO dto, Long excludeId) {
        incomeTypeRepository.findByName(dto.getName())
            .ifPresent(existing -> {
                if (excludeId == null || !existing.getId().equals(excludeId)) {
                    throw new DuplicateFieldException(
                        "name",
                        dto.getName(),
                        "An income type with the name '" + dto.getName() + "' already exists"
                    );
                }
            });
    }

    /**
     * Valida que el tipo de ingreso no tenga movimientos asociados antes de eliminarlo.
     * @param incomeType Tipo de ingreso a validar
     */
    public void validateCanDelete(IncomeTypeEntity incomeType) {
        if (incomeType.getAccountIncomes() != null && !incomeType.getAccountIncomes().isEmpty()) {
            throw new InvalidDataException("Cannot delete income type with associated account incomes. Deactivate it instead.");
        }
    }
}

