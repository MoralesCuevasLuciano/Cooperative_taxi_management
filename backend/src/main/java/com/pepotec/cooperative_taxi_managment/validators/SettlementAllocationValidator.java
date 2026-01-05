package com.pepotec.cooperative_taxi_managment.validators;

import com.pepotec.cooperative_taxi_managment.exceptions.InvalidDataException;
import com.pepotec.cooperative_taxi_managment.models.dto.settlementallocation.SettlementAllocationCreateDTO;
import com.pepotec.cooperative_taxi_managment.models.entities.AbstractAccountMovementEntity;
import com.pepotec.cooperative_taxi_managment.models.entities.AbstractMovementEntity;
import com.pepotec.cooperative_taxi_managment.models.entities.ReceiptEntity;
import com.pepotec.cooperative_taxi_managment.models.entities.SettlementAllocationEntity;
import com.pepotec.cooperative_taxi_managment.repositories.ReceiptRepository;
import com.pepotec.cooperative_taxi_managment.repositories.SettlementAllocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Validator para asignaciones de pago (SettlementAllocation).
 */
@Component
public class SettlementAllocationValidator {

    @Autowired
    private SettlementAllocationRepository settlementAllocationRepository;

    @Autowired
    private ReceiptRepository receiptRepository;

    /**
     * Valida los campos de creación de una asignación de pago.
     * @param dto DTO a validar
     * @param accountMovement Movimiento de cuenta asociado
     */
    public void validateCreateFields(SettlementAllocationCreateDTO dto, AbstractAccountMovementEntity accountMovement) {
        if (dto.getAccountMovementId() == null) {
            throw new InvalidDataException("The account movement ID cannot be null");
        }
        
        if (accountMovement == null) {
            throw new InvalidDataException("Account movement not found with id: " + dto.getAccountMovementId());
        }
        
        // Validar XOR: solo uno de los tres métodos de pago puede estar presente
        validateXorPaymentMethod(dto.getReceiptId(), dto.getPayrollSettlementId(), dto.getMovementId());
        
        // Validar allocatedAmount
        if (dto.getAllocatedAmount() == null || dto.getAllocatedAmount() <= 0) {
            throw new InvalidDataException("The allocated amount must be positive");
        }
        
        // Validar que el monto asignado no exceda el saldo pendiente
        validateAllocatedAmount(dto.getAllocatedAmount(), accountMovement);
        
        // Validar que la cuenta del método de pago coincida con la cuenta del movimiento
        validateAccountMatch(dto, accountMovement);
    }

    /**
     * Valida que solo uno de los tres métodos de pago esté presente (XOR).
     */
    private void validateXorPaymentMethod(Long receiptId, Long payrollSettlementId, Long movementId) {
        int methodCount = 0;
        if (receiptId != null) methodCount++;
        if (payrollSettlementId != null) methodCount++;
        if (movementId != null) methodCount++;
        
        if (methodCount == 0) {
            throw new InvalidDataException("A settlement allocation must have exactly one payment method (receipt, payrollSettlement or movement)");
        }
        if (methodCount > 1) {
            throw new InvalidDataException("A settlement allocation can only have one payment method. Found " + methodCount + " methods.");
        }
    }

    /**
     * Valida que el monto asignado no exceda el saldo pendiente del movimiento.
     */
    private void validateAllocatedAmount(Double allocatedAmount, AbstractAccountMovementEntity accountMovement) {
        // Calcular saldo pendiente
        Double totalAllocated = settlementAllocationRepository
            .sumAllocatedAmountByAccountMovementIdAndActiveTrue(accountMovement.getId());
        
        Double pendingBalance = accountMovement.getAmount() - totalAllocated;
        
        if (allocatedAmount > pendingBalance) {
            throw new InvalidDataException(
                "The allocated amount (" + allocatedAmount + ") exceeds the pending balance (" + pendingBalance + ") " +
                "for account movement " + accountMovement.getId()
            );
        }
    }

    /**
     * Valida que la cuenta del método de pago coincida con la cuenta del movimiento.
     */
    private void validateAccountMatch(SettlementAllocationCreateDTO dto, AbstractAccountMovementEntity accountMovement) {
        // Obtener la cuenta del movimiento
        Long movementAccountId = getAccountId(accountMovement);
        String movementAccountType = getAccountType(accountMovement);
        
        if (dto.getReceiptId() != null) {
            ReceiptEntity receipt = receiptRepository.findById(dto.getReceiptId())
                .orElseThrow(() -> new InvalidDataException("Receipt not found with id: " + dto.getReceiptId()));
            
            Long receiptAccountId = getReceiptAccountId(receipt);
            String receiptAccountType = getReceiptAccountType(receipt);
            
            if (!movementAccountId.equals(receiptAccountId) || !movementAccountType.equals(receiptAccountType)) {
                throw new InvalidDataException("The receipt account does not match the account movement account");
            }
        }
        
        // Para payrollSettlement y movement, la validación se hace en el servicio
        // porque necesitan acceso a las entidades completas
    }

    /**
     * Obtiene el ID de la cuenta del movimiento.
     */
    private Long getAccountId(AbstractAccountMovementEntity accountMovement) {
        if (accountMovement.getMemberAccount() != null) {
            return accountMovement.getMemberAccount().getId();
        }
        if (accountMovement.getSubscriberAccount() != null) {
            return accountMovement.getSubscriberAccount().getId();
        }
        if (accountMovement.getVehicleAccount() != null) {
            return accountMovement.getVehicleAccount().getId();
        }
        throw new InvalidDataException("Account movement must have one account associated");
    }
    
    /**
     * Valida que el movimiento de dinero tenga la cuenta correcta.
     * Este método debe ser llamado desde el servicio con la entidad completa.
     */
    public void validateMovementAccountMatch(AbstractMovementEntity movement, AbstractAccountMovementEntity accountMovement) {
        Long movementAccountId = getMovementAccountId(movement);
        String movementAccountType = getMovementAccountType(movement);
        
        Long accountMovementAccountId = getAccountId(accountMovement);
        String accountMovementAccountType = getAccountType(accountMovement);
        
        if (!movementAccountId.equals(accountMovementAccountId) || !movementAccountType.equals(accountMovementAccountType)) {
            throw new InvalidDataException("The movement account does not match the account movement account");
        }
    }
    
    /**
     * Obtiene el ID de la cuenta del movimiento de dinero.
     */
    private Long getMovementAccountId(AbstractMovementEntity movement) {
        if (movement.getMemberAccount() != null) {
            return movement.getMemberAccount().getId();
        }
        if (movement.getSubscriberAccount() != null) {
            return movement.getSubscriberAccount().getId();
        }
        if (movement.getVehicleAccount() != null) {
            return movement.getVehicleAccount().getId();
        }
        throw new InvalidDataException("Movement must have one account associated");
    }
    
    /**
     * Obtiene el tipo de cuenta del movimiento de dinero.
     */
    private String getMovementAccountType(AbstractMovementEntity movement) {
        if (movement.getMemberAccount() != null) return "MEMBER";
        if (movement.getSubscriberAccount() != null) return "SUBSCRIBER";
        if (movement.getVehicleAccount() != null) return "VEHICLE";
        throw new InvalidDataException("Movement must have one account associated");
    }

    /**
     * Obtiene el tipo de cuenta del movimiento.
     */
    private String getAccountType(AbstractAccountMovementEntity accountMovement) {
        if (accountMovement.getMemberAccount() != null) return "MEMBER";
        if (accountMovement.getSubscriberAccount() != null) return "SUBSCRIBER";
        if (accountMovement.getVehicleAccount() != null) return "VEHICLE";
        throw new InvalidDataException("Account movement must have one account associated");
    }

    /**
     * Obtiene el ID de la cuenta del recibo.
     */
    private Long getReceiptAccountId(ReceiptEntity receipt) {
        if (receipt.getMemberAccount() != null) {
            return receipt.getMemberAccount().getId();
        }
        if (receipt.getSubscriberAccount() != null) {
            return receipt.getSubscriberAccount().getId();
        }
        throw new InvalidDataException("Receipt must have one account associated");
    }

    /**
     * Obtiene el tipo de cuenta del recibo.
     */
    private String getReceiptAccountType(ReceiptEntity receipt) {
        if (receipt.getMemberAccount() != null) return "MEMBER";
        if (receipt.getSubscriberAccount() != null) return "SUBSCRIBER";
        throw new InvalidDataException("Receipt must have one account associated");
    }

    /**
     * Valida que una asignación con recibo o liquidación no pueda modificarse.
     * @param settlementAllocation Asignación a validar
     */
    public void validateCanModify(SettlementAllocationEntity settlementAllocation) {
        if (settlementAllocation.getReceipt() != null || settlementAllocation.getPayrollSettlement() != null) {
            throw new InvalidDataException("Settlement allocation with receipt or payroll settlement cannot be modified");
        }
    }

    /**
     * Valida que una asignación con recibo o liquidación no pueda eliminarse.
     * @param settlementAllocation Asignación a validar
     */
    public void validateCanDelete(SettlementAllocationEntity settlementAllocation) {
        if (settlementAllocation.getReceipt() != null || settlementAllocation.getPayrollSettlement() != null) {
            throw new InvalidDataException("Settlement allocation with receipt or payroll settlement cannot be deleted");
        }
    }

    /**
     * Valida que solo el campo 'note' pueda modificarse en asignaciones con recibo/liquidación.
     * @param settlementAllocation Asignación existente
     * @param newAllocatedAmount Nuevo monto asignado (debe ser igual al anterior)
     * @param newAccountMovementId Nuevo ID de movimiento (debe ser igual al anterior)
     * @param newReceiptId Nuevo ID de recibo (debe ser igual al anterior)
     * @param newPayrollSettlementId Nuevo ID de liquidación (debe ser igual al anterior)
     * @param newMovementId Nuevo ID de movimiento de dinero (debe ser igual al anterior)
     */
    public void validatePartialUpdate(SettlementAllocationEntity settlementAllocation, 
                                      Double newAllocatedAmount, Long newAccountMovementId,
                                      Long newReceiptId, Long newPayrollSettlementId, Long newMovementId) {
        if (settlementAllocation.getReceipt() != null || settlementAllocation.getPayrollSettlement() != null) {
            // Solo se puede modificar 'note', todos los demás campos deben ser iguales
            if (newAllocatedAmount != null && !newAllocatedAmount.equals(settlementAllocation.getAllocatedAmount())) {
                throw new InvalidDataException("Cannot modify allocatedAmount in settlement allocation with receipt or payroll settlement");
            }
            if (newAccountMovementId != null && !newAccountMovementId.equals(settlementAllocation.getAccountMovement().getId())) {
                throw new InvalidDataException("Cannot modify accountMovement in settlement allocation with receipt or payroll settlement");
            }
            if (newReceiptId != null && !newReceiptId.equals(settlementAllocation.getReceipt() != null ? settlementAllocation.getReceipt().getId() : null)) {
                throw new InvalidDataException("Cannot modify receipt in settlement allocation with receipt or payroll settlement");
            }
            if (newPayrollSettlementId != null && !newPayrollSettlementId.equals(settlementAllocation.getPayrollSettlement() != null ? settlementAllocation.getPayrollSettlement().getId() : null)) {
                throw new InvalidDataException("Cannot modify payrollSettlement in settlement allocation with receipt or payroll settlement");
            }
            if (newMovementId != null && !newMovementId.equals(settlementAllocation.getMovement() != null ? settlementAllocation.getMovement().getId() : null)) {
                throw new InvalidDataException("Cannot modify movement in settlement allocation with receipt or payroll settlement");
            }
        }
    }
}

