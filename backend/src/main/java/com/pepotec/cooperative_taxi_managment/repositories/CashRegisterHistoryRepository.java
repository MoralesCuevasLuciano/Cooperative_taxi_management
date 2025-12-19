package com.pepotec.cooperative_taxi_managment.repositories;

import com.pepotec.cooperative_taxi_managment.models.entities.CashRegisterHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository para el historial de la caja.
 */
@Repository
public interface CashRegisterHistoryRepository extends JpaRepository<CashRegisterHistoryEntity, Long> {
    
    /**
     * Busca el historial por fecha.
     * @param date Fecha del historial
     * @return Historial del día especificado
     */
    Optional<CashRegisterHistoryEntity> findByDate(LocalDate date);
    
    /**
     * Busca historiales por rango de fechas.
     * @param startDate Fecha inicial
     * @param endDate Fecha final
     * @return Lista de historiales en el rango
     */
    List<CashRegisterHistoryEntity> findByDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Busca historiales ordenados por fecha descendente.
     * @return Lista de historiales ordenados por fecha (más reciente primero)
     */
    List<CashRegisterHistoryEntity> findAllByOrderByDateDesc();
}

