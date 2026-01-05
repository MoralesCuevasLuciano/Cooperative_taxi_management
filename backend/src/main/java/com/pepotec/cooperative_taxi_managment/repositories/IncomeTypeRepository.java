package com.pepotec.cooperative_taxi_managment.repositories;

import com.pepotec.cooperative_taxi_managment.models.entities.IncomeTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository para tipos de ingreso.
 */
@Repository
public interface IncomeTypeRepository extends JpaRepository<IncomeTypeEntity, Long> {
    
    /**
     * Busca un tipo de ingreso por nombre.
     * @param name Nombre del tipo de ingreso
     * @return Tipo de ingreso encontrado (si existe)
     */
    Optional<IncomeTypeEntity> findByName(String name);
    
    /**
     * Verifica si existe un tipo de ingreso con el nombre especificado.
     * @param name Nombre del tipo de ingreso
     * @return true si existe, false en caso contrario
     */
    boolean existsByName(String name);
    
    /**
     * Busca todos los tipos de ingreso activos.
     * @return Lista de tipos de ingreso activos
     */
    List<IncomeTypeEntity> findByActiveTrue();
    
    /**
     * Busca todos los tipos de ingreso con recurrencia mensual activos.
     * @return Lista de tipos de ingreso recurrentes mensualmente y activos
     */
    List<IncomeTypeEntity> findByMonthlyRecurrenceTrueAndActiveTrue();
}

