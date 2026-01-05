package com.pepotec.cooperative_taxi_managment.repositories;

import com.pepotec.cooperative_taxi_managment.models.entities.SettlementAllocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository para asignaciones de pago (SettlementAllocation).
 */
@Repository
public interface SettlementAllocationRepository extends JpaRepository<SettlementAllocationEntity, Long> {
    
    /**
     * Busca asignaciones por movimiento de cuenta.
     * @param accountMovementId ID del movimiento de cuenta
     * @return Lista de asignaciones asociadas al movimiento
     */
    @Query("SELECT sa FROM SettlementAllocationEntity sa WHERE sa.accountMovement.id = :accountMovementId")
    List<SettlementAllocationEntity> findByAccountMovementId(@Param("accountMovementId") Long accountMovementId);
    
    /**
     * Busca asignaciones activas por movimiento de cuenta.
     * @param accountMovementId ID del movimiento de cuenta
     * @return Lista de asignaciones activas asociadas al movimiento
     */
    @Query("SELECT sa FROM SettlementAllocationEntity sa WHERE sa.accountMovement.id = :accountMovementId AND sa.active = true")
    List<SettlementAllocationEntity> findByAccountMovementIdAndActiveTrue(@Param("accountMovementId") Long accountMovementId);
    
    /**
     * Busca asignaciones por recibo.
     * @param receiptId ID del recibo
     * @return Lista de asignaciones asociadas al recibo
     */
    @Query("SELECT sa FROM SettlementAllocationEntity sa WHERE sa.receipt.id = :receiptId")
    List<SettlementAllocationEntity> findByReceiptId(@Param("receiptId") Long receiptId);
    
    /**
     * Busca asignaciones activas por recibo.
     * @param receiptId ID del recibo
     * @return Lista de asignaciones activas asociadas al recibo
     */
    @Query("SELECT sa FROM SettlementAllocationEntity sa WHERE sa.receipt.id = :receiptId AND sa.active = true")
    List<SettlementAllocationEntity> findByReceiptIdAndActiveTrue(@Param("receiptId") Long receiptId);
    
    /**
     * Busca asignaciones por liquidación de sueldo.
     * @param payrollSettlementId ID de la liquidación
     * @return Lista de asignaciones asociadas a la liquidación
     */
    @Query("SELECT sa FROM SettlementAllocationEntity sa WHERE sa.payrollSettlement.id = :payrollSettlementId")
    List<SettlementAllocationEntity> findByPayrollSettlementId(@Param("payrollSettlementId") Long payrollSettlementId);
    
    /**
     * Busca asignaciones activas por liquidación de sueldo.
     * @param payrollSettlementId ID de la liquidación
     * @return Lista de asignaciones activas asociadas a la liquidación
     */
    @Query("SELECT sa FROM SettlementAllocationEntity sa WHERE sa.payrollSettlement.id = :payrollSettlementId AND sa.active = true")
    List<SettlementAllocationEntity> findByPayrollSettlementIdAndActiveTrue(@Param("payrollSettlementId") Long payrollSettlementId);
    
    /**
     * Busca asignaciones por movimiento de dinero.
     * @param movementId ID del movimiento de dinero
     * @return Lista de asignaciones asociadas al movimiento de dinero
     */
    @Query("SELECT sa FROM SettlementAllocationEntity sa WHERE sa.movement.id = :movementId")
    List<SettlementAllocationEntity> findByMovementId(@Param("movementId") Long movementId);
    
    /**
     * Busca asignaciones activas por movimiento de dinero.
     * @param movementId ID del movimiento de dinero
     * @return Lista de asignaciones activas asociadas al movimiento de dinero
     */
    @Query("SELECT sa FROM SettlementAllocationEntity sa WHERE sa.movement.id = :movementId AND sa.active = true")
    List<SettlementAllocationEntity> findByMovementIdAndActiveTrue(@Param("movementId") Long movementId);
    
    /**
     * Calcula la suma de montos asignados activos para un movimiento de cuenta.
     * @param accountMovementId ID del movimiento de cuenta
     * @return Suma de montos asignados activos
     */
    @Query("SELECT COALESCE(SUM(sa.allocatedAmount), 0) FROM SettlementAllocationEntity sa WHERE sa.accountMovement.id = :accountMovementId AND sa.active = true")
    Double sumAllocatedAmountByAccountMovementIdAndActiveTrue(@Param("accountMovementId") Long accountMovementId);
    
    /**
     * Busca asignaciones por rango de fechas de asignación.
     * @param startDate Fecha inicial
     * @param endDate Fecha final
     * @return Lista de asignaciones en el rango de fechas
     */
    @Query("SELECT sa FROM SettlementAllocationEntity sa WHERE sa.allocationDate BETWEEN :startDate AND :endDate")
    List<SettlementAllocationEntity> findByAllocationDateBetween(
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
    
    /**
     * Busca asignaciones activas por rango de fechas de asignación.
     * @param startDate Fecha inicial
     * @param endDate Fecha final
     * @return Lista de asignaciones activas en el rango de fechas
     */
    @Query("SELECT sa FROM SettlementAllocationEntity sa WHERE sa.allocationDate BETWEEN :startDate AND :endDate AND sa.active = true")
    List<SettlementAllocationEntity> findByAllocationDateBetweenAndActiveTrue(
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
}

