package com.pepotec.cooperative_taxi_managment.services;

import com.pepotec.cooperative_taxi_managment.models.entities.*;
import com.pepotec.cooperative_taxi_managment.models.enums.MovementType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * Servicio que maneja la lógica de actualización y reversión de saldos
 * en cuentas y caja basándose en el campo isIncome del movimiento.
 */
@Service
public class BalanceUpdateService {
    
    @Autowired
    private MemberAccountService memberAccountService;
    
    @Autowired
    private SubscriberAccountService subscriberAccountService;
    
    @Autowired
    private VehicleAccountService vehicleAccountService;
    
    @Autowired
    private CashRegisterService cashRegisterService;
    
    /**
     * Aplica un movimiento a las cuentas y caja afectadas.
     * La lógica de suma/resta se basa en el boolean isIncome, NO en MovementType.
     * 
     * @param movement Movimiento a aplicar
     */
    @Transactional
    public void applyMovement(AbstractMovementEntity movement) {
        // Actualizar cuenta si existe y el tipo de movimiento lo permite
        AbstractAccountEntity account = movement.getAffectedAccount();
        if (account != null && shouldAffectAccount(movement.getMovementType())) {
            updateAccountBalance(account, movement.getAmount(), movement.getIsIncome());
        }
        
        // Actualizar caja si es movimiento en efectivo
        if (movement instanceof CashMovementEntity) {
            CashMovementEntity cashMovement = (CashMovementEntity) movement;
            updateCashRegisterBalance(cashMovement.getCashRegister(), 
                                    cashMovement.getAmount(), 
                                    cashMovement.getIsIncome());
        }
        
        // TODO: Si es ADVANCE, crear instancia de Advance (a implementar cuando se cree la entidad Advance)
        // if (movement.getMovementType() == MovementType.ADVANCE && account instanceof MemberAccountEntity) {
        //     advanceService.createAdvance((MemberAccountEntity) account, movement);
        // }
    }
    
    /**
     * Revierte un movimiento (para edición o eliminación).
     * Invierte la operación basándose en isIncome.
     * 
     * @param movement Movimiento a revertir
     */
    @Transactional
    public void revertMovement(AbstractMovementEntity movement) {
        // Revertir cuenta
        AbstractAccountEntity account = movement.getAffectedAccount();
        if (account != null && shouldAffectAccount(movement.getMovementType())) {
            // Invertir: si era ingreso, ahora es egreso y viceversa
            revertAccountBalance(account, movement.getAmount(), movement.getIsIncome());
        }
        
        // Revertir caja si es movimiento en efectivo
        if (movement instanceof CashMovementEntity) {
            CashMovementEntity cashMovement = (CashMovementEntity) movement;
            revertCashRegisterBalance(cashMovement.getCashRegister(), 
                                    cashMovement.getAmount(), 
                                    cashMovement.getIsIncome());
        }
    }
    
    /**
     * Actualiza el balance de la cuenta basándose en isIncome.
     * @param account Cuenta a actualizar
     * @param amount Monto del movimiento
     * @param isIncome true = suma, false = resta
     */
    private void updateAccountBalance(AbstractAccountEntity account, Double amount, Boolean isIncome) {
        if (isIncome) {
            account.setBalance(account.getBalance() + amount);
        } else {
            account.setBalance(account.getBalance() - amount);
        }
        account.setLastModified(LocalDate.now());
        saveAccount(account);
    }
    
    /**
     * Revierte el balance de la cuenta (invierte la operación).
     */
    private void revertAccountBalance(AbstractAccountEntity account, Double amount, Boolean isIncome) {
        // Invertir: si era ingreso (sumaba), ahora resta; si era egreso (restaba), ahora suma
        if (isIncome) {
            account.setBalance(account.getBalance() - amount);
        } else {
            account.setBalance(account.getBalance() + amount);
        }
        account.setLastModified(LocalDate.now());
        saveAccount(account);
    }
    
    /**
     * Actualiza el balance de la caja basándose en isIncome.
     */
    private void updateCashRegisterBalance(CashRegisterEntity cashRegister, Double amount, Boolean isIncome) {
        if (isIncome) {
            cashRegister.setAmount(cashRegister.getAmount() + amount);
        } else {
            cashRegister.setAmount(cashRegister.getAmount() - amount);
        }
        cashRegisterService.updateAmount(cashRegister.getAmount());
    }
    
    /**
     * Revierte el balance de la caja (invierte la operación).
     */
    private void revertCashRegisterBalance(CashRegisterEntity cashRegister, Double amount, Boolean isIncome) {
        // Invertir la operación
        if (isIncome) {
            cashRegister.setAmount(cashRegister.getAmount() - amount);
        } else {
            cashRegister.setAmount(cashRegister.getAmount() + amount);
        }
        cashRegisterService.updateAmount(cashRegister.getAmount());
    }
    
    /**
     * Determina si el movimiento debe afectar el balance de la cuenta.
     * ADVANCE no afecta el balance (independientemente de isIncome).
     */
    private boolean shouldAffectAccount(MovementType type) {
        return type != MovementType.ADVANCE;
    }
    
    /**
     * Guarda la cuenta actualizada según su tipo.
     */
    private void saveAccount(AbstractAccountEntity account) {
        if (account instanceof MemberAccountEntity) {
            // Actualizar directamente en el repository para evitar dependencias circulares
            memberAccountService.updateAccountEntity((MemberAccountEntity) account);
        } else if (account instanceof SubscriberAccountEntity) {
            subscriberAccountService.updateAccountEntity((SubscriberAccountEntity) account);
        } else if (account instanceof VehicleAccountEntity) {
            vehicleAccountService.updateAccountEntity((VehicleAccountEntity) account);
        }
    }
}

