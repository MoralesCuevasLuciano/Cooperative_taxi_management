package com.pepotec.cooperative_taxi_managment.validators;

import com.pepotec.cooperative_taxi_managment.models.dto.driversettlement.DriverSettlementDTO;
import com.pepotec.cooperative_taxi_managment.models.dto.driversettlement.DriverSettlementCreateDTO;
import com.pepotec.cooperative_taxi_managment.exceptions.InvalidDataException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DriverSettlementValidator {

    public void validateDriverSettlementSpecificFields(DriverSettlementDTO settlement) {
        if (settlement == null) {
            throw new InvalidDataException("The settlement cannot be null");
        }

        if (settlement.getTicketAmount() == null) {
            throw new InvalidDataException("The ticket amount cannot be null");
        }

        if (settlement.getTicketAmount() < 0) {
            throw new InvalidDataException("The ticket amount must be greater than or equal to zero");
        }

        if (settlement.getVoucherAmount() == null) {
            throw new InvalidDataException("The voucher amount cannot be null");
        }

        if (settlement.getVoucherAmount() < 0) {
            throw new InvalidDataException("The voucher amount must be greater than or equal to zero");
        }

        if (settlement.getVoucherDifference() == null) {
            throw new InvalidDataException("The voucher difference cannot be null");
        }

        if (settlement.getFinalBalance() == null) {
            throw new InvalidDataException("The final balance cannot be null");
        }

        if (settlement.getSubmissionDate() == null) {
            throw new InvalidDataException("The submission date cannot be null");
        }

        // En el contexto de DriverSettlementDTO, el driverId debe venir informado
        if (settlement.getDriverId() == null) {
            throw new InvalidDataException("The driver id cannot be null");
        }
    }

    /**
     * Validaciones específicas para la creación de rendiciones.
     * El driverId viene por path, por eso no se valida aquí.
     */
    public void validateDriverSettlementCreateFields(DriverSettlementCreateDTO settlement) {
        if (settlement == null) {
            throw new InvalidDataException("The settlement cannot be null");
        }

        if (settlement.getTicketAmount() == null) {
            throw new InvalidDataException("The ticket amount cannot be null");
        }

        if (settlement.getTicketAmount() < 0) {
            throw new InvalidDataException("The ticket amount must be greater than or equal to zero");
        }

        if (settlement.getVoucherAmount() == null) {
            throw new InvalidDataException("The voucher amount cannot be null");
        }

        if (settlement.getVoucherAmount() < 0) {
            throw new InvalidDataException("The voucher amount must be greater than or equal to zero");
        }

        if (settlement.getVoucherDifference() == null) {
            throw new InvalidDataException("The voucher difference cannot be null");
        }

        if (settlement.getFinalBalance() == null) {
            throw new InvalidDataException("The final balance cannot be null");
        }

        if (settlement.getSubmissionDate() == null) {
            throw new InvalidDataException("The submission date cannot be null");
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
     * Valida que el settlement ID no sea nulo.
     * @param settlementId ID de la rendición a validar
     */
    public void validateSettlementIdNotNullForCalculation(Long settlementId) {
        if (settlementId == null) {
            throw new InvalidDataException("The settlement ID cannot be null");
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
     * Valida que el driver ID no sea nulo.
     * @param driverId ID del chofer a validar
     */
    public void validateDriverIdNotNull(Long driverId) {
        if (driverId == null) {
            throw new InvalidDataException("The driver ID cannot be null");
        }
    }

    /**
     * Valida que el settlement ID no sea nulo.
     * @param settlementId ID de la rendición a validar
     */
    public void validateSettlementIdNotNull(Long settlementId) {
        if (settlementId == null) {
            throw new InvalidDataException("The settlement ID cannot be null");
        }
    }

    /**
     * Valida que la fecha de entrega no sea nula.
     * @param submissionDate Fecha de entrega a validar
     */
    public void validateSubmissionDateNotNull(LocalDate submissionDate) {
        if (submissionDate == null) {
            throw new InvalidDataException("The submission date cannot be null");
        }
    }

    /**
     * Valida un rango de fechas (startDate y endDate no nulos, y startDate <= endDate).
     * @param startDate Fecha inicial
     * @param endDate Fecha final
     */
    public void validateDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new InvalidDataException("The start and end dates cannot be null");
        }
        if (startDate.isAfter(endDate)) {
            throw new InvalidDataException("The start date cannot be after the end date");
        }
    }
}

