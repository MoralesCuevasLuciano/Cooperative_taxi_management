package com.pepotec.cooperative_taxi_managment.repositories;

import com.pepotec.cooperative_taxi_managment.models.entities.ExpenseTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository para tipos de gasto.
 */
@Repository
public interface ExpenseTypeRepository extends JpaRepository<ExpenseTypeEntity, Long> {
    
    /**
     * Busca un tipo de gasto por nombre.
     * @param name Nombre del tipo de gasto
     * @return Tipo de gasto encontrado (si existe)
     */
    Optional<ExpenseTypeEntity> findByName(String name);
    
    /**
     * Verifica si existe un tipo de gasto con el nombre especificado.
     * @param name Nombre del tipo de gasto
     * @return true si existe, false en caso contrario
     */
    boolean existsByName(String name);
    
    /**
     * Busca todos los tipos de gasto activos.
     * @return Lista de tipos de gasto activos
     */
    List<ExpenseTypeEntity> findByActiveTrue();
    
    /**
     * Busca todos los tipos de gasto con recurrencia mensual activos.
     * @return Lista de tipos de gasto recurrentes mensualmente y activos
     */
    List<ExpenseTypeEntity> findByMonthlyRecurrenceTrueAndActiveTrue();
}

