package com.pepotec.cooperative_taxi_managment.validators;

import com.pepotec.cooperative_taxi_managment.exceptions.InvalidDataException;
import com.pepotec.cooperative_taxi_managment.models.dto.fuelreimbursement.FuelReimbursementDTO;
import com.pepotec.cooperative_taxi_managment.models.dto.fuelreimbursement.FuelReimbursementCreateDTO;
import com.pepotec.cooperative_taxi_managment.repositories.FuelReimbursementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FuelReimbursementValidator {

    @Autowired
    private FuelReimbursementRepository fuelReimbursementRepository;

    public void validateFuelReimbursementFields(FuelReimbursementDTO fuelReimbursement) {
        if (fuelReimbursement == null) {
            throw new InvalidDataException("The fuel reimbursement cannot be null");
        }

        if (fuelReimbursement.getMemberAccountId() == null) {
            throw new InvalidDataException("The member account ID cannot be null");
        }

        if (fuelReimbursement.getAccumulatedAmount() == null) {
            throw new InvalidDataException("The accumulated amount cannot be null");
        }

        if (fuelReimbursement.getAccumulatedAmount() < 0) {
            throw new InvalidDataException("The accumulated amount cannot be negative");
        }

        if (fuelReimbursement.getCreatedDate() == null) {
            throw new InvalidDataException("The creation date cannot be null");
        }
    }

    public void validateFuelReimbursementCreateFields(FuelReimbursementCreateDTO fuelReimbursement) {
        if (fuelReimbursement == null) {
            throw new InvalidDataException("The fuel reimbursement cannot be null");
        }

        // El accumulatedAmount puede ser null o 0 al crear (se inicializa en 0)
        if (fuelReimbursement.getAccumulatedAmount() != null && fuelReimbursement.getAccumulatedAmount() < 0) {
            throw new InvalidDataException("The accumulated amount cannot be negative");
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
            throw new InvalidDataException("The ID cannot be null for update");
        }
    }

    /**
     * Valida que el member account ID no sea nulo.
     * @param memberAccountId ID de la cuenta de socio a validar
     */
    public void validateMemberAccountIdNotNull(Long memberAccountId) {
        if (memberAccountId == null) {
            throw new InvalidDataException("The member account ID cannot be null");
        }
    }

    /**
     * Valida que el amount sea positivo.
     * @param amount Monto a validar
     */
    public void validateAmountPositive(Double amount) {
        if (amount == null || amount <= 0) {
            throw new InvalidDataException("The amount to accumulate must be positive");
        }
    }

    /**
     * Valida la unicidad: que la cuenta no tenga ya un reintegro asociado.
     * @param memberAccountId ID de la cuenta de socio
     */
    public void validateUniqueFuelReimbursement(Long memberAccountId) {
        fuelReimbursementRepository.findByMemberAccountId(memberAccountId).ifPresent(existing -> {
            throw new InvalidDataException("A fuel reimbursement record already exists for this account");
        });
    }
}

