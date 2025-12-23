package com.pepotec.cooperative_taxi_managment.models.enums;

/**
 * Enum que representa los tipos de movimientos de dinero.
 * El campo isIncome determina si suma o resta, este enum es para categorización.
 */
public enum MovementType {
    DEPOSIT,          // Ingreso/Depósito
    WITHDRAWAL,       // Retiro/Egreso
    TRANSFER,         // Transferencia (solo para NonCashMovement)
    PAYMENT,          // Pago
    REFUND,           // Reembolso/Devolución
    ADVANCE,          // Advance (adelanto de sueldo) - Solo para Members, NO afecta cuenta, crea instancia de Advance
    WORKSHOP_ORDER,   // Workshop Order (orden de taller) - Solo para Vehicles
    OTHER             // Other - Para cualquier cuenta o ninguna, la descripción hace su magia
}




