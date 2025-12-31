package com.pepotec.cooperative_taxi_managment.validators;

import com.pepotec.cooperative_taxi_managment.exceptions.InvalidDataException;
import com.pepotec.cooperative_taxi_managment.models.dto.person.subscriber.account.SubscriberAccountDTO;
import com.pepotec.cooperative_taxi_managment.repositories.SubscriberAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class SubscriberAccountValidator {

    @Autowired
    private SubscriberAccountRepository subscriberAccountRepository;

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
            throw new InvalidDataException("The account ID cannot be null for update");
        }
    }

    /**
     * Valida que el subscriber ID no sea nulo.
     * @param subscriberId ID del abonado a validar
     */
    public void validateSubscriberIdNotNull(Long subscriberId) {
        if (subscriberId == null) {
            throw new InvalidDataException("The subscriber ID cannot be null");
        }
    }

    /**
     * Valida la unicidad: que el abonado no tenga ya una cuenta asociada.
     * @param subscriberId ID del abonado
     */
    public void validateUniqueSubscriberAccount(Long subscriberId) {
        subscriberAccountRepository.findBySubscriberId(subscriberId).ifPresent(existing -> {
            throw new InvalidDataException("The subscriber already has an associated account");
        });
    }
}


