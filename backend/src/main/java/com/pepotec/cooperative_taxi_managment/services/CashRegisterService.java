package com.pepotec.cooperative_taxi_managment.services;

import com.pepotec.cooperative_taxi_managment.models.dto.cashregister.CashRegisterDTO;
import com.pepotec.cooperative_taxi_managment.models.entities.CashRegisterEntity;
import com.pepotec.cooperative_taxi_managment.repositories.CashRegisterRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CashRegisterService {

    @Autowired
    private CashRegisterRepository cashRegisterRepository;

    @PostConstruct
    public void initializeCashRegister() {
        getOrCreate();
    }

    /**
    * Obtiene la única instancia de caja; si no existe, la crea.
    */
    @Transactional
    public CashRegisterEntity getOrCreate() {
        List<CashRegisterEntity> registers = cashRegisterRepository.findAll();
        if (registers.isEmpty()) {
            CashRegisterEntity entity = new CashRegisterEntity();
            entity.setAmount(0.0);
            entity.setActive(true);
            return cashRegisterRepository.save(entity);
        }
        return registers.get(0);
    }

    public CashRegisterEntity getCashRegisterEntity() {
        return getOrCreate();
    }

    public CashRegisterDTO getCashRegister() {
        return convertToDTO(getOrCreate());
    }

    @Transactional
    public CashRegisterDTO updateAmount(Double amount) {
        CashRegisterEntity entity = getOrCreate();
        entity.setAmount(amount);
        return convertToDTO(cashRegisterRepository.save(entity));
    }

    private CashRegisterDTO convertToDTO(CashRegisterEntity entity) {
        if (entity == null) return null;
        return CashRegisterDTO.builder()
                .id(entity.getId())
                .amount(entity.getAmount())
                .active(entity.getActive())
                .build();
    }
}


