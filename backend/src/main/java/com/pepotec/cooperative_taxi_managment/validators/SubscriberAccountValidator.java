package com.pepotec.cooperative_taxi_managment.validators;

import com.pepotec.cooperative_taxi_managment.exceptions.InvalidDataException;
import com.pepotec.cooperative_taxi_managment.models.dto.person.subscriber.account.SubscriberAccountDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class SubscriberAccountValidator {

    public void validateSubscriberAccountFields(SubscriberAccountDTO account) {
        if (account == null) {
            throw new InvalidDataException("The account cannot be null");
        }

        if (account.getSubscriberId() == null) {
            throw new InvalidDataException("The subscriber id cannot be null");
        }

        if (account.getBalance() == null) {
            throw new InvalidDataException("The balance cannot be null");
        }

        if (account.getLastModified() != null && account.getLastModified().isAfter(LocalDate.now())) {
            throw new InvalidDataException("The last modified date cannot be in the future");
        }
    }

    /**
     * Validaciones para creación de cuentas (ID del abonado viene por path).
     */
    public void validateSubscriberAccountCreateFields(Double balance, LocalDate lastModified) {
        if (balance == null) {
            throw new InvalidDataException("The balance cannot be null");
        }

        if (lastModified != null && lastModified.isAfter(LocalDate.now())) {
            throw new InvalidDataException("The last modified date cannot be in the future");
        }
    }
}


