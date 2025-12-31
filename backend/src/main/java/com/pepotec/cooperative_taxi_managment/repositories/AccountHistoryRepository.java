package com.pepotec.cooperative_taxi_managment.repositories;

import com.pepotec.cooperative_taxi_managment.models.entities.AccountHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository para historiales mensuales de cuentas.
 */
@Repository
public interface AccountHistoryRepository extends JpaRepository<AccountHistoryEntity, Long> {
    
    /**
     * Busca historiales por cuenta de socio.
     * @param memberAccountId ID de la cuenta de socio
     * @return Lista de historiales asociados a la cuenta
     */
    List<AccountHistoryEntity> findByMemberAccountId(Long memberAccountId);
    
    /**
     * Busca historiales activos por cuenta de socio.
     * @param memberAccountId ID de la cuenta de socio
     * @return Lista de historiales activos asociados a la cuenta
     */
    List<AccountHistoryEntity> findByMemberAccountIdAndActiveTrue(Long memberAccountId);
    
    /**
     * Busca historiales por cuenta de abonado.
     * @param subscriberAccountId ID de la cuenta de abonado
     * @return Lista de historiales asociados a la cuenta
     */
    List<AccountHistoryEntity> findBySubscriberAccountId(Long subscriberAccountId);
    
    /**
     * Busca historiales activos por cuenta de abonado.
     * @param subscriberAccountId ID de la cuenta de abonado
     * @return Lista de historiales activos asociados a la cuenta
     */
    List<AccountHistoryEntity> findBySubscriberAccountIdAndActiveTrue(Long subscriberAccountId);
    
    /**
     * Busca historiales por cuenta de vehículo.
     * @param vehicleAccountId ID de la cuenta de vehículo
     * @return Lista de historiales asociados a la cuenta
     */
    List<AccountHistoryEntity> findByVehicleAccountId(Long vehicleAccountId);
    
    /**
     * Busca historiales activos por cuenta de vehículo.
     * @param vehicleAccountId ID de la cuenta de vehículo
     * @return Lista de historiales activos asociados a la cuenta
     */
    List<AccountHistoryEntity> findByVehicleAccountIdAndActiveTrue(Long vehicleAccountId);
    
    /**
     * Busca un historial por cuenta de socio y período.
     * @param memberAccountId ID de la cuenta de socio
     * @param period Período en formato "YYYY-MM"
     * @return Historial encontrado (si existe)
     */
    @Query("SELECT a FROM AccountHistoryEntity a WHERE a.memberAccount.id = :memberAccountId AND a.yearMonth = :period")
    Optional<AccountHistoryEntity> findByMemberAccountIdAndPeriod(
        @Param("memberAccountId") Long memberAccountId,
        @Param("period") String period
    );
    
    /**
     * Busca un historial por cuenta de abonado y período.
     * @param subscriberAccountId ID de la cuenta de abonado
     * @param period Período en formato "YYYY-MM"
     * @return Historial encontrado (si existe)
     */
    @Query("SELECT a FROM AccountHistoryEntity a WHERE a.subscriberAccount.id = :subscriberAccountId AND a.yearMonth = :period")
    Optional<AccountHistoryEntity> findBySubscriberAccountIdAndPeriod(
        @Param("subscriberAccountId") Long subscriberAccountId,
        @Param("period") String period
    );
    
    /**
     * Busca un historial por cuenta de vehículo y período.
     * @param vehicleAccountId ID de la cuenta de vehículo
     * @param period Período en formato "YYYY-MM"
     * @return Historial encontrado (si existe)
     */
    @Query("SELECT a FROM AccountHistoryEntity a WHERE a.vehicleAccount.id = :vehicleAccountId AND a.yearMonth = :period")
    Optional<AccountHistoryEntity> findByVehicleAccountIdAndPeriod(
        @Param("vehicleAccountId") Long vehicleAccountId,
        @Param("period") String period
    );
    
    /**
     * Verifica si existe un historial por cuenta de socio y período.
     * @param memberAccountId ID de la cuenta de socio
     * @param period Período en formato "YYYY-MM"
     * @return true si existe, false en caso contrario
     */
    @Query("SELECT COUNT(a) > 0 FROM AccountHistoryEntity a WHERE a.memberAccount.id = :memberAccountId AND a.yearMonth = :period")
    boolean existsByMemberAccountIdAndPeriod(
        @Param("memberAccountId") Long memberAccountId,
        @Param("period") String period
    );
    
    /**
     * Verifica si existe un historial por cuenta de abonado y período.
     * @param subscriberAccountId ID de la cuenta de abonado
     * @param period Período en formato "YYYY-MM"
     * @return true si existe, false en caso contrario
     */
    @Query("SELECT COUNT(a) > 0 FROM AccountHistoryEntity a WHERE a.subscriberAccount.id = :subscriberAccountId AND a.yearMonth = :period")
    boolean existsBySubscriberAccountIdAndPeriod(
        @Param("subscriberAccountId") Long subscriberAccountId,
        @Param("period") String period
    );
    
    /**
     * Verifica si existe un historial por cuenta de vehículo y período.
     * @param vehicleAccountId ID de la cuenta de vehículo
     * @param period Período en formato "YYYY-MM"
     * @return true si existe, false en caso contrario
     */
    @Query("SELECT COUNT(a) > 0 FROM AccountHistoryEntity a WHERE a.vehicleAccount.id = :vehicleAccountId AND a.yearMonth = :period")
    boolean existsByVehicleAccountIdAndPeriod(
        @org.springframework.data.repository.query.Param("vehicleAccountId") Long vehicleAccountId,
        @org.springframework.data.repository.query.Param("period") String period
    );
    
    /**
     * Busca historiales por período.
     * @param period Período en formato "YYYY-MM"
     * @return Lista de historiales del período
     */
    @Query("SELECT a FROM AccountHistoryEntity a WHERE a.yearMonth = :period")
    List<AccountHistoryEntity> findByPeriod(@Param("period") String period);
    
    /**
     * Busca historiales por rango de fechas de registro.
     * @param startDate Fecha inicial
     * @param endDate Fecha final
     * @return Lista de historiales registrados en el rango
     */
    List<AccountHistoryEntity> findByRegistrationDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Busca historiales activos por rango de fechas de registro.
     * @param startDate Fecha inicial
     * @param endDate Fecha final
     * @return Lista de historiales activos registrados en el rango
     */
    List<AccountHistoryEntity> findByRegistrationDateBetweenAndActiveTrue(LocalDate startDate, LocalDate endDate);
}

