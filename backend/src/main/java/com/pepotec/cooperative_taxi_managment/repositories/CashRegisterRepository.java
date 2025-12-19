package com.pepotec.cooperative_taxi_managment.repositories;

import com.pepotec.cooperative_taxi_managment.models.entities.CashRegisterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository para la entidad CashRegister.
 * Solo debe existir una instancia (singleton).
 */
@Repository
public interface CashRegisterRepository extends JpaRepository<CashRegisterEntity, Long> {
    
    /**
     * Obtiene todas las instancias de caja (debería ser solo una).
     * @return Lista con la única instancia de caja
     */
    List<CashRegisterEntity> findAll();
    
    /**
     * Busca la caja activa.
     * @param active Estado activo
     * @return La caja activa (debería ser solo una)
     */
    CashRegisterEntity findByActive(Boolean active);
}

