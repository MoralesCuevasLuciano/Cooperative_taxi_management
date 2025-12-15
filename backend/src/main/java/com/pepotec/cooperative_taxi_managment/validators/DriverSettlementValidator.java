package com.pepotec.cooperative_taxi_managment.validators;

import com.pepotec.cooperative_taxi_managment.models.dto.driversettlement.DriverSettlementDTO;
import com.pepotec.cooperative_taxi_managment.models.dto.driversettlement.DriverSettlementCreateDTO;
import com.pepotec.cooperative_taxi_managment.exceptions.InvalidDataException;
import org.springframework.stereotype.Component;

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
}

