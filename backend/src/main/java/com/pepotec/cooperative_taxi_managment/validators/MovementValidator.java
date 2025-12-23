package com.pepotec.cooperative_taxi_managment.validators;

import com.pepotec.cooperative_taxi_managment.exceptions.InvalidDataException;
import com.pepotec.cooperative_taxi_managment.models.dto.movement.cash.CashMovementCreateDTO;
import com.pepotec.cooperative_taxi_managment.models.dto.movement.noncash.NonCashMovementCreateDTO;
import com.pepotec.cooperative_taxi_managment.models.entities.AbstractMovementEntity;
import com.pepotec.cooperative_taxi_managment.models.enums.MovementType;
import org.springframework.stereotype.Component;

/**
 * Validator para movimientos de dinero (efectivo y no efectivo).
 * Valida que solo haya una cuenta asociada y restricciones por tipo de movimiento.
 */
@Component
public class MovementValidator {
    
    /**
     * Valida un movimiento abstracto (usado para entidades).
     * @param movement Movimiento a validar
     */
    public void validate(AbstractMovementEntity movement) {
        // Validar que solo haya una cuenta (o ninguna)
        validateOnlyOneAccount(movement);
        
        // Validar restricciones por tipo de movimiento
        validateMovementTypeRestrictions(movement);
    }
    
    /**
     * Valida un DTO de creación de movimiento en efectivo.
     * @param dto DTO a validar
     */
    public void validateCashMovementCreate(CashMovementCreateDTO dto) {
        // Validar que solo haya una cuenta (o ninguna)
        int accountCount = 0;
        if (dto.getMemberAccountId() != null) accountCount++;
        if (dto.getSubscriberAccountId() != null) accountCount++;
        if (dto.getVehicleAccountId() != null) accountCount++;
        
        if (accountCount > 1) {
            throw new InvalidDataException("Un movimiento solo puede tener una cuenta asociada o ninguna. Se encontraron " + accountCount + " cuentas.");
        }
        
        // Validar restricciones por tipo de movimiento
        validateMovementTypeRestrictions(dto.getMovementType(), 
                                        dto.getMemberAccountId(), 
                                        dto.getSubscriberAccountId(), 
                                        dto.getVehicleAccountId());
    }
    
    /**
     * Valida un DTO de creación de movimiento sin efectivo.
     * @param dto DTO a validar
     */
    public void validateNonCashMovementCreate(NonCashMovementCreateDTO dto) {
        // Validar que solo haya una cuenta (o ninguna)
        int accountCount = 0;
        if (dto.getMemberAccountId() != null) accountCount++;
        if (dto.getSubscriberAccountId() != null) accountCount++;
        if (dto.getVehicleAccountId() != null) accountCount++;
        
        if (accountCount > 1) {
            throw new InvalidDataException("Un movimiento solo puede tener una cuenta asociada o ninguna. Se encontraron " + accountCount + " cuentas.");
        }
        
        // Validar restricciones por tipo de movimiento
        validateMovementTypeRestrictions(dto.getMovementType(), 
                                        dto.getMemberAccountId(), 
                                        dto.getSubscriberAccountId(), 
                                        dto.getVehicleAccountId());
    }
    
    /**
     * Valida que solo haya una cuenta asociada (o ninguna).
     */
    private void validateOnlyOneAccount(AbstractMovementEntity movement) {
        int accountCount = 0;
        if (movement.getMemberAccount() != null) accountCount++;
        if (movement.getSubscriberAccount() != null) accountCount++;
        if (movement.getVehicleAccount() != null) accountCount++;
        
        if (accountCount > 1) {
            throw new InvalidDataException("Un movimiento solo puede tener una cuenta asociada o ninguna. Se encontraron " + accountCount + " cuentas.");
        }
    }
    
    /**
     * Valida restricciones por tipo de movimiento para entidades.
     */
    private void validateMovementTypeRestrictions(AbstractMovementEntity movement) {
        MovementType type = movement.getMovementType();
        
        if (type == MovementType.ADVANCE) {
            if (movement.getMemberAccount() == null) {
                throw new InvalidDataException("ADVANCE movement requires a MemberAccount");
            }
            if (movement.getSubscriberAccount() != null || movement.getVehicleAccount() != null) {
                throw new InvalidDataException("ADVANCE movement can only be associated with MemberAccount");
            }
        } else if (type == MovementType.WORKSHOP_ORDER) {
            if (movement.getVehicleAccount() == null) {
                throw new InvalidDataException("WORKSHOP_ORDER movement requires a VehicleAccount");
            }
            if (movement.getMemberAccount() != null || movement.getSubscriberAccount() != null) {
                throw new InvalidDataException("WORKSHOP_ORDER movement can only be associated with VehicleAccount");
            }
        }
        // OTHER puede tener cualquier cuenta o ninguna
    }
    
    /**
     * Valida restricciones por tipo de movimiento para DTOs.
     */
    private void validateMovementTypeRestrictions(MovementType type, 
                                                 Long memberAccountId, 
                                                 Long subscriberAccountId, 
                                                 Long vehicleAccountId) {
        if (type == MovementType.ADVANCE) {
            if (memberAccountId == null) {
                throw new InvalidDataException("ADVANCE movement requires a MemberAccount");
            }
            if (subscriberAccountId != null || vehicleAccountId != null) {
                throw new InvalidDataException("ADVANCE movement can only be associated with MemberAccount");
            }
        } else if (type == MovementType.WORKSHOP_ORDER) {
            if (vehicleAccountId == null) {
                throw new InvalidDataException("WORKSHOP_ORDER movement requires a VehicleAccount");
            }
            if (memberAccountId != null || subscriberAccountId != null) {
                throw new InvalidDataException("WORKSHOP_ORDER movement can only be associated with VehicleAccount");
            }
        }
        // OTHER puede tener cualquier cuenta o ninguna
    }
}




