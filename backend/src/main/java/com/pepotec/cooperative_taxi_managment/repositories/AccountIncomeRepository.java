package com.pepotec.cooperative_taxi_managment.repositories;

import com.pepotec.cooperative_taxi_managment.models.entities.AccountIncomeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository para ingresos de cuenta.
 */
@Repository
public interface AccountIncomeRepository extends JpaRepository<AccountIncomeEntity, Long> {
    
    /**
     * Busca ingresos por cuenta de socio.
     * @param memberAccountId ID de la cuenta de socio
     * @return Lista de ingresos asociados a la cuenta
     */
    @Query("SELECT ai FROM AccountIncomeEntity ai WHERE ai.memberAccount.id = :memberAccountId")
    List<AccountIncomeEntity> findByMemberAccountId(@Param("memberAccountId") Long memberAccountId);
    
    /**
     * Busca ingresos activos por cuenta de socio.
     * @param memberAccountId ID de la cuenta de socio
     * @return Lista de ingresos activos asociados a la cuenta
     */
    @Query("SELECT ai FROM AccountIncomeEntity ai WHERE ai.memberAccount.id = :memberAccountId AND ai.active = true")
    List<AccountIncomeEntity> findByMemberAccountIdAndActiveTrue(@Param("memberAccountId") Long memberAccountId);
    
    /**
     * Busca ingresos por cuenta de abonado.
     * @param subscriberAccountId ID de la cuenta de abonado
     * @return Lista de ingresos asociados a la cuenta
     */
    @Query("SELECT ai FROM AccountIncomeEntity ai WHERE ai.subscriberAccount.id = :subscriberAccountId")
    List<AccountIncomeEntity> findBySubscriberAccountId(@Param("subscriberAccountId") Long subscriberAccountId);
    
    /**
     * Busca ingresos activos por cuenta de abonado.
     * @param subscriberAccountId ID de la cuenta de abonado
     * @return Lista de ingresos activos asociados a la cuenta
     */
    @Query("SELECT ai FROM AccountIncomeEntity ai WHERE ai.subscriberAccount.id = :subscriberAccountId AND ai.active = true")
    List<AccountIncomeEntity> findBySubscriberAccountIdAndActiveTrue(@Param("subscriberAccountId") Long subscriberAccountId);
    
    /**
     * Busca ingresos por cuenta de vehículo.
     * @param vehicleAccountId ID de la cuenta de vehículo
     * @return Lista de ingresos asociados a la cuenta
     */
    @Query("SELECT ai FROM AccountIncomeEntity ai WHERE ai.vehicleAccount.id = :vehicleAccountId")
    List<AccountIncomeEntity> findByVehicleAccountId(@Param("vehicleAccountId") Long vehicleAccountId);
    
    /**
     * Busca ingresos activos por cuenta de vehículo.
     * @param vehicleAccountId ID de la cuenta de vehículo
     * @return Lista de ingresos activos asociados a la cuenta
     */
    @Query("SELECT ai FROM AccountIncomeEntity ai WHERE ai.vehicleAccount.id = :vehicleAccountId AND ai.active = true")
    List<AccountIncomeEntity> findByVehicleAccountIdAndActiveTrue(@Param("vehicleAccountId") Long vehicleAccountId);
    
    /**
     * Busca ingresos por cuenta de socio, período y estado added.
     * @param memberAccountId ID de la cuenta de socio
     * @param period Período en formato "YYYY-MM"
     * @param added Estado del campo added
     * @return Lista de ingresos que coinciden con los criterios
     */
    @Query("SELECT ai FROM AccountIncomeEntity ai WHERE ai.memberAccount.id = :memberAccountId AND ai.yearMonth = :period AND ai.added = :added")
    List<AccountIncomeEntity> findByMemberAccountIdAndPeriodAndAdded(
        @Param("memberAccountId") Long memberAccountId,
        @Param("period") String period,
        @Param("added") Boolean added
    );
    
    /**
     * Busca ingresos por cuenta de abonado, período y estado added.
     * @param subscriberAccountId ID de la cuenta de abonado
     * @param period Período en formato "YYYY-MM"
     * @param added Estado del campo added
     * @return Lista de ingresos que coinciden con los criterios
     */
    @Query("SELECT ai FROM AccountIncomeEntity ai WHERE ai.subscriberAccount.id = :subscriberAccountId AND ai.yearMonth = :period AND ai.added = :added")
    List<AccountIncomeEntity> findBySubscriberAccountIdAndPeriodAndAdded(
        @Param("subscriberAccountId") Long subscriberAccountId,
        @Param("period") String period,
        @Param("added") Boolean added
    );
    
    /**
     * Busca ingresos por cuenta de vehículo, período y estado added.
     * @param vehicleAccountId ID de la cuenta de vehículo
     * @param period Período en formato "YYYY-MM"
     * @param added Estado del campo added
     * @return Lista de ingresos que coinciden con los criterios
     */
    @Query("SELECT ai FROM AccountIncomeEntity ai WHERE ai.vehicleAccount.id = :vehicleAccountId AND ai.yearMonth = :period AND ai.added = :added")
    List<AccountIncomeEntity> findByVehicleAccountIdAndPeriodAndAdded(
        @Param("vehicleAccountId") Long vehicleAccountId,
        @Param("period") String period,
        @Param("added") Boolean added
    );
    
    /**
     * Busca un ingreso por cuenta de socio, período y tipo de ingreso.
     * Solo aplica cuando el tipo tiene monthlyRecurrence = true.
     * @param memberAccountId ID de la cuenta de socio
     * @param period Período en formato "YYYY-MM"
     * @param incomeTypeId ID del tipo de ingreso
     * @return Ingreso encontrado (si existe)
     */
    @Query("SELECT ai FROM AccountIncomeEntity ai WHERE ai.memberAccount.id = :memberAccountId AND ai.yearMonth = :period AND ai.incomeType.id = :incomeTypeId")
    Optional<AccountIncomeEntity> findByMemberAccountIdAndPeriodAndIncomeTypeId(
        @Param("memberAccountId") Long memberAccountId,
        @Param("period") String period,
        @Param("incomeTypeId") Long incomeTypeId
    );
    
    /**
     * Busca un ingreso por cuenta de abonado, período y tipo de ingreso.
     * Solo aplica cuando el tipo tiene monthlyRecurrence = true.
     * @param subscriberAccountId ID de la cuenta de abonado
     * @param period Período en formato "YYYY-MM"
     * @param incomeTypeId ID del tipo de ingreso
     * @return Ingreso encontrado (si existe)
     */
    @Query("SELECT ai FROM AccountIncomeEntity ai WHERE ai.subscriberAccount.id = :subscriberAccountId AND ai.yearMonth = :period AND ai.incomeType.id = :incomeTypeId")
    Optional<AccountIncomeEntity> findBySubscriberAccountIdAndPeriodAndIncomeTypeId(
        @Param("subscriberAccountId") Long subscriberAccountId,
        @Param("period") String period,
        @Param("incomeTypeId") Long incomeTypeId
    );
    
    /**
     * Busca un ingreso por cuenta de vehículo, período y tipo de ingreso.
     * Solo aplica cuando el tipo tiene monthlyRecurrence = true.
     * @param vehicleAccountId ID de la cuenta de vehículo
     * @param period Período en formato "YYYY-MM"
     * @param incomeTypeId ID del tipo de ingreso
     * @return Ingreso encontrado (si existe)
     */
    @Query("SELECT ai FROM AccountIncomeEntity ai WHERE ai.vehicleAccount.id = :vehicleAccountId AND ai.yearMonth = :period AND ai.incomeType.id = :incomeTypeId")
    Optional<AccountIncomeEntity> findByVehicleAccountIdAndPeriodAndIncomeTypeId(
        @Param("vehicleAccountId") Long vehicleAccountId,
        @Param("period") String period,
        @Param("incomeTypeId") Long incomeTypeId
    );
    
    /**
     * Busca ingresos por período.
     * @param period Período en formato "YYYY-MM"
     * @return Lista de ingresos del período
     */
    @Query("SELECT ai FROM AccountIncomeEntity ai WHERE ai.yearMonth = :period")
    List<AccountIncomeEntity> findByPeriod(@Param("period") String period);
    
    /**
     * Busca ingresos activos por período.
     * @param period Período en formato "YYYY-MM"
     * @return Lista de ingresos activos del período
     */
    @Query("SELECT ai FROM AccountIncomeEntity ai WHERE ai.yearMonth = :period AND ai.active = true")
    List<AccountIncomeEntity> findByPeriodAndActiveTrue(@Param("period") String period);
    
    /**
     * Busca ingresos por tipo de ingreso.
     * @param incomeTypeId ID del tipo de ingreso
     * @return Lista de ingresos del tipo especificado
     */
    @Query("SELECT ai FROM AccountIncomeEntity ai WHERE ai.incomeType.id = :incomeTypeId")
    List<AccountIncomeEntity> findByIncomeTypeId(@Param("incomeTypeId") Long incomeTypeId);
    
    /**
     * Busca ingresos activos por tipo de ingreso.
     * @param incomeTypeId ID del tipo de ingreso
     * @return Lista de ingresos activos del tipo especificado
     */
    @Query("SELECT ai FROM AccountIncomeEntity ai WHERE ai.incomeType.id = :incomeTypeId AND ai.active = true")
    List<AccountIncomeEntity> findByIncomeTypeIdAndActiveTrue(@Param("incomeTypeId") Long incomeTypeId);
    
    /**
     * Busca ingresos que aún no han sido agregados al saldo (added = false).
     * @return Lista de ingresos pendientes de agregar
     */
    @Query("SELECT ai FROM AccountIncomeEntity ai WHERE ai.added = false AND ai.active = true")
    List<AccountIncomeEntity> findByAddedFalseAndActiveTrue();
}
