package com.pepotec.cooperative_taxi_managment.repositories;

import com.pepotec.cooperative_taxi_managment.models.entities.CashMovementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository para movimientos de dinero en efectivo.
 */
@Repository
public interface CashMovementRepository extends JpaRepository<CashMovementEntity, Long> {
    
    /**
     * Busca movimientos activos.
     * @param active Estado activo
     * @return Lista de movimientos activos
     */
    List<CashMovementEntity> findByActive(Boolean active);
    
    /**
     * Busca movimientos por rango de fechas.
     * @param startDate Fecha inicial
     * @param endDate Fecha final
     * @return Lista de movimientos en el rango
     */
    List<CashMovementEntity> findByDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Busca movimientos activos por rango de fechas.
     * @param startDate Fecha inicial
     * @param endDate Fecha final
     * @param active Estado activo
     * @return Lista de movimientos activos en el rango
     */
    List<CashMovementEntity> findByDateBetweenAndActive(LocalDate startDate, LocalDate endDate, Boolean active);
    
    /**
     * Busca movimientos por cuenta de socio.
     * @param memberAccountId ID de la cuenta de socio
     * @return Lista de movimientos asociados a la cuenta
     */
    List<CashMovementEntity> findByMemberAccountId(Long memberAccountId);
    
    /**
     * Busca movimientos por cuenta de abonado.
     * @param subscriberAccountId ID de la cuenta de abonado
     * @return Lista de movimientos asociados a la cuenta
     */
    List<CashMovementEntity> findBySubscriberAccountId(Long subscriberAccountId);
    
    /**
     * Busca movimientos por cuenta de vehículo.
     * @param vehicleAccountId ID de la cuenta de vehículo
     * @return Lista de movimientos asociados a la cuenta
     */
    List<CashMovementEntity> findByVehicleAccountId(Long vehicleAccountId);
}

