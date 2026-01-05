package com.pepotec.cooperative_taxi_managment.repositories;

import com.pepotec.cooperative_taxi_managment.models.entities.MonthlyExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository para gastos mensuales.
 */
@Repository
public interface MonthlyExpenseRepository extends JpaRepository<MonthlyExpenseEntity, Long> {
    
    /**
     * Busca gastos mensuales por cuenta de socio.
     * @param memberAccountId ID de la cuenta de socio
     * @return Lista de gastos mensuales asociados a la cuenta
     */
    @Query("SELECT me FROM MonthlyExpenseEntity me WHERE me.memberAccount.id = :memberAccountId")
    List<MonthlyExpenseEntity> findByMemberAccountId(@Param("memberAccountId") Long memberAccountId);
    
    /**
     * Busca gastos mensuales activos por cuenta de socio.
     * @param memberAccountId ID de la cuenta de socio
     * @return Lista de gastos mensuales activos asociados a la cuenta
     */
    @Query("SELECT me FROM MonthlyExpenseEntity me WHERE me.memberAccount.id = :memberAccountId AND me.active = true")
    List<MonthlyExpenseEntity> findByMemberAccountIdAndActiveTrue(@Param("memberAccountId") Long memberAccountId);
    
    /**
     * Busca gastos mensuales por cuenta de abonado.
     * @param subscriberAccountId ID de la cuenta de abonado
     * @return Lista de gastos mensuales asociados a la cuenta
     */
    @Query("SELECT me FROM MonthlyExpenseEntity me WHERE me.subscriberAccount.id = :subscriberAccountId")
    List<MonthlyExpenseEntity> findBySubscriberAccountId(@Param("subscriberAccountId") Long subscriberAccountId);
    
    /**
     * Busca gastos mensuales activos por cuenta de abonado.
     * @param subscriberAccountId ID de la cuenta de abonado
     * @return Lista de gastos mensuales activos asociados a la cuenta
     */
    @Query("SELECT me FROM MonthlyExpenseEntity me WHERE me.subscriberAccount.id = :subscriberAccountId AND me.active = true")
    List<MonthlyExpenseEntity> findBySubscriberAccountIdAndActiveTrue(@Param("subscriberAccountId") Long subscriberAccountId);
    
    /**
     * Busca gastos mensuales por cuenta de vehículo.
     * @param vehicleAccountId ID de la cuenta de vehículo
     * @return Lista de gastos mensuales asociados a la cuenta
     */
    @Query("SELECT me FROM MonthlyExpenseEntity me WHERE me.vehicleAccount.id = :vehicleAccountId")
    List<MonthlyExpenseEntity> findByVehicleAccountId(@Param("vehicleAccountId") Long vehicleAccountId);
    
    /**
     * Busca gastos mensuales activos por cuenta de vehículo.
     * @param vehicleAccountId ID de la cuenta de vehículo
     * @return Lista de gastos mensuales activos asociados a la cuenta
     */
    @Query("SELECT me FROM MonthlyExpenseEntity me WHERE me.vehicleAccount.id = :vehicleAccountId AND me.active = true")
    List<MonthlyExpenseEntity> findByVehicleAccountIdAndActiveTrue(@Param("vehicleAccountId") Long vehicleAccountId);
    
    /**
     * Busca gastos mensuales por cuenta de socio, período y estado added.
     * @param memberAccountId ID de la cuenta de socio
     * @param period Período en formato "YYYY-MM"
     * @param added Estado del campo added
     * @return Lista de gastos mensuales que coinciden con los criterios
     */
    @Query("SELECT me FROM MonthlyExpenseEntity me WHERE me.memberAccount.id = :memberAccountId AND me.yearMonth = :period AND me.added = :added")
    List<MonthlyExpenseEntity> findByMemberAccountIdAndPeriodAndAdded(
        @Param("memberAccountId") Long memberAccountId,
        @Param("period") String period,
        @Param("added") Boolean added
    );
    
    /**
     * Busca gastos mensuales por cuenta de abonado, período y estado added.
     * @param subscriberAccountId ID de la cuenta de abonado
     * @param period Período en formato "YYYY-MM"
     * @param added Estado del campo added
     * @return Lista de gastos mensuales que coinciden con los criterios
     */
    @Query("SELECT me FROM MonthlyExpenseEntity me WHERE me.subscriberAccount.id = :subscriberAccountId AND me.yearMonth = :period AND me.added = :added")
    List<MonthlyExpenseEntity> findBySubscriberAccountIdAndPeriodAndAdded(
        @Param("subscriberAccountId") Long subscriberAccountId,
        @Param("period") String period,
        @Param("added") Boolean added
    );
    
    /**
     * Busca gastos mensuales por cuenta de vehículo, período y estado added.
     * @param vehicleAccountId ID de la cuenta de vehículo
     * @param period Período en formato "YYYY-MM"
     * @param added Estado del campo added
     * @return Lista de gastos mensuales que coinciden con los criterios
     */
    @Query("SELECT me FROM MonthlyExpenseEntity me WHERE me.vehicleAccount.id = :vehicleAccountId AND me.yearMonth = :period AND me.added = :added")
    List<MonthlyExpenseEntity> findByVehicleAccountIdAndPeriodAndAdded(
        @Param("vehicleAccountId") Long vehicleAccountId,
        @Param("period") String period,
        @Param("added") Boolean added
    );
    
    /**
     * Busca un gasto mensual por cuenta de socio, período y tipo de gasto.
     * Solo aplica cuando el tipo tiene monthlyRecurrence = true.
     * @param memberAccountId ID de la cuenta de socio
     * @param period Período en formato "YYYY-MM"
     * @param expenseTypeId ID del tipo de gasto
     * @return Gasto mensual encontrado (si existe)
     */
    @Query("SELECT me FROM MonthlyExpenseEntity me WHERE me.memberAccount.id = :memberAccountId AND me.yearMonth = :period AND me.expenseType.id = :expenseTypeId")
    Optional<MonthlyExpenseEntity> findByMemberAccountIdAndPeriodAndExpenseTypeId(
        @Param("memberAccountId") Long memberAccountId,
        @Param("period") String period,
        @Param("expenseTypeId") Long expenseTypeId
    );
    
    /**
     * Busca un gasto mensual por cuenta de abonado, período y tipo de gasto.
     * Solo aplica cuando el tipo tiene monthlyRecurrence = true.
     * @param subscriberAccountId ID de la cuenta de abonado
     * @param period Período en formato "YYYY-MM"
     * @param expenseTypeId ID del tipo de gasto
     * @return Gasto mensual encontrado (si existe)
     */
    @Query("SELECT me FROM MonthlyExpenseEntity me WHERE me.subscriberAccount.id = :subscriberAccountId AND me.yearMonth = :period AND me.expenseType.id = :expenseTypeId")
    Optional<MonthlyExpenseEntity> findBySubscriberAccountIdAndPeriodAndExpenseTypeId(
        @Param("subscriberAccountId") Long subscriberAccountId,
        @Param("period") String period,
        @Param("expenseTypeId") Long expenseTypeId
    );
    
    /**
     * Busca un gasto mensual por cuenta de vehículo, período y tipo de gasto.
     * Solo aplica cuando el tipo tiene monthlyRecurrence = true.
     * @param vehicleAccountId ID de la cuenta de vehículo
     * @param period Período en formato "YYYY-MM"
     * @param expenseTypeId ID del tipo de gasto
     * @return Gasto mensual encontrado (si existe)
     */
    @Query("SELECT me FROM MonthlyExpenseEntity me WHERE me.vehicleAccount.id = :vehicleAccountId AND me.yearMonth = :period AND me.expenseType.id = :expenseTypeId")
    Optional<MonthlyExpenseEntity> findByVehicleAccountIdAndPeriodAndExpenseTypeId(
        @Param("vehicleAccountId") Long vehicleAccountId,
        @Param("period") String period,
        @Param("expenseTypeId") Long expenseTypeId
    );
    
    /**
     * Busca gastos mensuales por período.
     * @param period Período en formato "YYYY-MM"
     * @return Lista de gastos mensuales del período
     */
    @Query("SELECT me FROM MonthlyExpenseEntity me WHERE me.yearMonth = :period")
    List<MonthlyExpenseEntity> findByPeriod(@Param("period") String period);
    
    /**
     * Busca gastos mensuales activos por período.
     * @param period Período en formato "YYYY-MM"
     * @return Lista de gastos mensuales activos del período
     */
    @Query("SELECT me FROM MonthlyExpenseEntity me WHERE me.yearMonth = :period AND me.active = true")
    List<MonthlyExpenseEntity> findByPeriodAndActiveTrue(@Param("period") String period);
    
    /**
     * Busca gastos mensuales por tipo de gasto.
     * @param expenseTypeId ID del tipo de gasto
     * @return Lista de gastos mensuales del tipo especificado
     */
    @Query("SELECT me FROM MonthlyExpenseEntity me WHERE me.expenseType.id = :expenseTypeId")
    List<MonthlyExpenseEntity> findByExpenseTypeId(@Param("expenseTypeId") Long expenseTypeId);
    
    /**
     * Busca gastos mensuales activos por tipo de gasto.
     * @param expenseTypeId ID del tipo de gasto
     * @return Lista de gastos mensuales activos del tipo especificado
     */
    @Query("SELECT me FROM MonthlyExpenseEntity me WHERE me.expenseType.id = :expenseTypeId AND me.active = true")
    List<MonthlyExpenseEntity> findByExpenseTypeIdAndActiveTrue(@Param("expenseTypeId") Long expenseTypeId);
    
    /**
     * Busca gastos mensuales que aún no han sido agregados al saldo (added = false).
     * @return Lista de gastos mensuales pendientes de agregar
     */
    @Query("SELECT me FROM MonthlyExpenseEntity me WHERE me.added = false AND me.active = true")
    List<MonthlyExpenseEntity> findByAddedFalseAndActiveTrue();
}

