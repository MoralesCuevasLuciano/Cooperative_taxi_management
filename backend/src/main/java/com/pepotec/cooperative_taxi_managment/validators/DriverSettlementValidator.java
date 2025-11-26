package com.pepotec.cooperative_taxi_managment.validators;

import com.pepotec.cooperative_taxi_managment.models.dto.DriverSettlementDTO;
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

        if (settlement.getDriver() == null || settlement.getDriver().getId() == null) {
            throw new InvalidDataException("The driver cannot be null");
        }
    }
}

