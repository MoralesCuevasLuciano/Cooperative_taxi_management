package com.pepotec.cooperative_taxi_managment.validators;

import com.pepotec.cooperative_taxi_managment.exceptions.InvalidDataException;
import com.pepotec.cooperative_taxi_managment.models.dto.person.member.driver.DriverDTO;
import com.pepotec.cooperative_taxi_managment.repositories.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Validador específico para campos de Driver
 */
@Component
public class DriverValidator {

    @Autowired
    private DriverRepository driverRepository;
    
    @Autowired
    private PersonValidator personValidator;

    /**
     * Valida SOLO los campos específicos de Driver
     */
    public void validateDriverSpecificFields(DriverDTO driver) {
        // Validar fecha de vencimiento del registro
        if (driver.getExpirationRegistrationDate() == null) {
            throw new InvalidDataException("The registration expiration date cannot be empty");
        }
    }

        /**
     * Valida que DNI, CUIT y Email no estén duplicados en la base de datos
     */
    public void validateUniqueFields(DriverDTO driver, Long excludeId) {
        personValidator.validatePersonUniqueFields(
            driver,
            excludeId,
            driverRepository::findByDniAndLeaveDateIsNull,
            driverRepository::findByCuitAndLeaveDateIsNull,
            driverRepository::findByEmailAndLeaveDateIsNull,
            "conductor"
        );
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
     * Valida que el DNI no esté vacío.
     * @param dni DNI a validar
     */
    public void validateDniNotEmpty(String dni) {
        if (dni == null || dni.trim().isEmpty()) {
            throw new InvalidDataException("The DNI cannot be empty");
        }
    }
}
