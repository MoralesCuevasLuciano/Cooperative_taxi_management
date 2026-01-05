package com.pepotec.cooperative_taxi_managment.repositories;

import com.pepotec.cooperative_taxi_managment.models.entities.WorkshopRepairEntity;
import com.pepotec.cooperative_taxi_managment.models.enums.RepairType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository para arreglos de taller.
 */
@Repository
public interface WorkshopRepairRepository extends JpaRepository<WorkshopRepairEntity, Long> {
    
    /**
     * Busca arreglos de taller por cuenta de socio.
     * @param memberAccountId ID de la cuenta de socio
     * @return Lista de arreglos de taller asociados a la cuenta
     */
    @Query("SELECT wr FROM WorkshopRepairEntity wr WHERE wr.memberAccount.id = :memberAccountId")
    List<WorkshopRepairEntity> findByMemberAccountId(@Param("memberAccountId") Long memberAccountId);
    
    /**
     * Busca arreglos de taller activos por cuenta de socio.
     * @param memberAccountId ID de la cuenta de socio
     * @return Lista de arreglos de taller activos asociados a la cuenta
     */
    @Query("SELECT wr FROM WorkshopRepairEntity wr WHERE wr.memberAccount.id = :memberAccountId AND wr.active = true")
    List<WorkshopRepairEntity> findByMemberAccountIdAndActiveTrue(@Param("memberAccountId") Long memberAccountId);
    
    /**
     * Busca arreglos de taller por cuenta de abonado.
     * @param subscriberAccountId ID de la cuenta de abonado
     * @return Lista de arreglos de taller asociados a la cuenta
     */
    @Query("SELECT wr FROM WorkshopRepairEntity wr WHERE wr.subscriberAccount.id = :subscriberAccountId")
    List<WorkshopRepairEntity> findBySubscriberAccountId(@Param("subscriberAccountId") Long subscriberAccountId);
    
    /**
     * Busca arreglos de taller activos por cuenta de abonado.
     * @param subscriberAccountId ID de la cuenta de abonado
     * @return Lista de arreglos de taller activos asociados a la cuenta
     */
    @Query("SELECT wr FROM WorkshopRepairEntity wr WHERE wr.subscriberAccount.id = :subscriberAccountId AND wr.active = true")
    List<WorkshopRepairEntity> findBySubscriberAccountIdAndActiveTrue(@Param("subscriberAccountId") Long subscriberAccountId);
    
    /**
     * Busca arreglos de taller por cuenta de vehículo.
     * @param vehicleAccountId ID de la cuenta de vehículo
     * @return Lista de arreglos de taller asociados a la cuenta
     */
    @Query("SELECT wr FROM WorkshopRepairEntity wr WHERE wr.vehicleAccount.id = :vehicleAccountId")
    List<WorkshopRepairEntity> findByVehicleAccountId(@Param("vehicleAccountId") Long vehicleAccountId);
    
    /**
     * Busca arreglos de taller activos por cuenta de vehículo.
     * @param vehicleAccountId ID de la cuenta de vehículo
     * @return Lista de arreglos de taller activos asociados a la cuenta
     */
    @Query("SELECT wr FROM WorkshopRepairEntity wr WHERE wr.vehicleAccount.id = :vehicleAccountId AND wr.active = true")
    List<WorkshopRepairEntity> findByVehicleAccountIdAndActiveTrue(@Param("vehicleAccountId") Long vehicleAccountId);
    
    /**
     * Busca arreglos de taller por cuenta de socio, período y estado added.
     * @param memberAccountId ID de la cuenta de socio
     * @param period Período en formato "YYYY-MM"
     * @param added Estado del campo added
     * @return Lista de arreglos de taller que coinciden con los criterios
     */
    @Query("SELECT wr FROM WorkshopRepairEntity wr WHERE wr.memberAccount.id = :memberAccountId AND wr.yearMonth = :period AND wr.added = :added")
    List<WorkshopRepairEntity> findByMemberAccountIdAndPeriodAndAdded(
        @Param("memberAccountId") Long memberAccountId,
        @Param("period") String period,
        @Param("added") Boolean added
    );
    
    /**
     * Busca arreglos de taller por cuenta de abonado, período y estado added.
     * @param subscriberAccountId ID de la cuenta de abonado
     * @param period Período en formato "YYYY-MM"
     * @param added Estado del campo added
     * @return Lista de arreglos de taller que coinciden con los criterios
     */
    @Query("SELECT wr FROM WorkshopRepairEntity wr WHERE wr.subscriberAccount.id = :subscriberAccountId AND wr.yearMonth = :period AND wr.added = :added")
    List<WorkshopRepairEntity> findBySubscriberAccountIdAndPeriodAndAdded(
        @Param("subscriberAccountId") Long subscriberAccountId,
        @Param("period") String period,
        @Param("added") Boolean added
    );
    
    /**
     * Busca arreglos de taller por cuenta de vehículo, período y estado added.
     * @param vehicleAccountId ID de la cuenta de vehículo
     * @param period Período en formato "YYYY-MM"
     * @param added Estado del campo added
     * @return Lista de arreglos de taller que coinciden con los criterios
     */
    @Query("SELECT wr FROM WorkshopRepairEntity wr WHERE wr.vehicleAccount.id = :vehicleAccountId AND wr.yearMonth = :period AND wr.added = :added")
    List<WorkshopRepairEntity> findByVehicleAccountIdAndPeriodAndAdded(
        @Param("vehicleAccountId") Long vehicleAccountId,
        @Param("period") String period,
        @Param("added") Boolean added
    );
    
    /**
     * Busca arreglos de taller por período.
     * @param period Período en formato "YYYY-MM"
     * @return Lista de arreglos de taller del período
     */
    @Query("SELECT wr FROM WorkshopRepairEntity wr WHERE wr.yearMonth = :period")
    List<WorkshopRepairEntity> findByPeriod(@Param("period") String period);
    
    /**
     * Busca arreglos de taller activos por período.
     * @param period Período en formato "YYYY-MM"
     * @return Lista de arreglos de taller activos del período
     */
    @Query("SELECT wr FROM WorkshopRepairEntity wr WHERE wr.yearMonth = :period AND wr.active = true")
    List<WorkshopRepairEntity> findByPeriodAndActiveTrue(@Param("period") String period);
    
    /**
     * Busca arreglos de taller por tipo de reparación.
     * @param repairType Tipo de reparación (WORKSHOP_REPAIR o LUBRICATION_CENTER)
     * @return Lista de arreglos de taller del tipo especificado
     */
    @Query("SELECT wr FROM WorkshopRepairEntity wr WHERE wr.repairType = :repairType")
    List<WorkshopRepairEntity> findByRepairType(@Param("repairType") RepairType repairType);
    
    /**
     * Busca arreglos de taller activos por tipo de reparación.
     * @param repairType Tipo de reparación (WORKSHOP_REPAIR o LUBRICATION_CENTER)
     * @return Lista de arreglos de taller activos del tipo especificado
     */
    @Query("SELECT wr FROM WorkshopRepairEntity wr WHERE wr.repairType = :repairType AND wr.active = true")
    List<WorkshopRepairEntity> findByRepairTypeAndActiveTrue(@Param("repairType") RepairType repairType);
    
    /**
     * Busca arreglos de taller que aún no han sido agregados al saldo (added = false).
     * @return Lista de arreglos de taller pendientes de agregar
     */
    @Query("SELECT wr FROM WorkshopRepairEntity wr WHERE wr.added = false AND wr.active = true")
    List<WorkshopRepairEntity> findByAddedFalseAndActiveTrue();
    
    /**
     * Busca arreglos de taller con saldo pendiente (remainingBalance > 0).
     * @return Lista de arreglos de taller con saldo pendiente
     */
    @Query("SELECT wr FROM WorkshopRepairEntity wr WHERE wr.remainingBalance > 0 AND wr.active = true")
    List<WorkshopRepairEntity> findByRemainingBalanceGreaterThanZeroAndActiveTrue();
}

