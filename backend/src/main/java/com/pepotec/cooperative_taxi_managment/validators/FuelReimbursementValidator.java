package com.pepotec.cooperative_taxi_managment.validators;

import com.pepotec.cooperative_taxi_managment.exceptions.InvalidDataException;
import com.pepotec.cooperative_taxi_managment.models.dto.fuelreimbursement.FuelReimbursementDTO;
import com.pepotec.cooperative_taxi_managment.models.dto.fuelreimbursement.FuelReimbursementCreateDTO;
import org.springframework.stereotype.Component;

@Component
public class FuelReimbursementValidator {

    public void validateFuelReimbursementFields(FuelReimbursementDTO fuelReimbursement) {
        if (fuelReimbursement == null) {
            throw new InvalidDataException("El reintegro de combustible no puede ser nulo");
        }

        if (fuelReimbursement.getMemberAccountId() == null) {
            throw new InvalidDataException("El ID de la cuenta de socio no puede ser nulo");
        }

        if (fuelReimbursement.getAccumulatedAmount() == null) {
            throw new InvalidDataException("El monto acumulado no puede ser nulo");
        }

        if (fuelReimbursement.getAccumulatedAmount() < 0) {
            throw new InvalidDataException("El monto acumulado no puede ser negativo");
        }

        if (fuelReimbursement.getCreatedDate() == null) {
            throw new InvalidDataException("La fecha de creación no puede ser nula");
        }
    }

    public void validateFuelReimbursementCreateFields(FuelReimbursementCreateDTO fuelReimbursement) {
        if (fuelReimbursement == null) {
            throw new InvalidDataException("El reintegro de combustible no puede ser nulo");
        }

        // El accumulatedAmount puede ser null o 0 al crear (se inicializa en 0)
        if (fuelReimbursement.getAccumulatedAmount() != null && fuelReimbursement.getAccumulatedAmount() < 0) {
            throw new InvalidDataException("El monto acumulado no puede ser negativo");
        }
    }
}

