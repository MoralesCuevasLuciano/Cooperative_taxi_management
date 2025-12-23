package com.pepotec.cooperative_taxi_managment.services;

import com.pepotec.cooperative_taxi_managment.exceptions.ResourceNotFoundException;
import com.pepotec.cooperative_taxi_managment.models.dto.cashregister.CashRegisterHistoryDTO;
import com.pepotec.cooperative_taxi_managment.models.entities.CashRegisterEntity;
import com.pepotec.cooperative_taxi_managment.models.entities.CashRegisterHistoryEntity;
import com.pepotec.cooperative_taxi_managment.repositories.CashRegisterHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CashRegisterHistoryService {

    @Autowired
    private CashRegisterHistoryRepository cashRegisterHistoryRepository;

    @Autowired
    private CashRegisterService cashRegisterService;

    /**
     * Crea el historial para hoy si no existe.
     */
    @Transactional
    public CashRegisterHistoryDTO ensureTodayHistoryExists() {
        LocalDate today = LocalDate.now();
        return cashRegisterHistoryRepository.findByDate(today)
                .map(this::convertToDTO)
                .orElseGet(() -> createHistoryForDate(today));
    }

    /**
     * Cierra el historial del día actual estableciendo el finalAmount con el monto de caja.
     */
    @Transactional
    public CashRegisterHistoryDTO closeTodayHistory() {
        LocalDate today = LocalDate.now();
        CashRegisterHistoryEntity history = cashRegisterHistoryRepository.findByDate(today)
                .orElseThrow(() -> new ResourceNotFoundException(null, "Historial de Caja del día " + today));

        CashRegisterEntity cashRegister = cashRegisterService.getOrCreate();
        history.setFinalAmount(cashRegister.getAmount());
        return convertToDTO(cashRegisterHistoryRepository.save(history));
    }

    public CashRegisterHistoryDTO getById(Long id) {
        CashRegisterHistoryEntity history = cashRegisterHistoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id, "Historial de Caja"));
        return convertToDTO(history);
    }

    public CashRegisterHistoryDTO getByDate(LocalDate date) {
        CashRegisterHistoryEntity history = cashRegisterHistoryRepository.findByDate(date)
                .orElseThrow(() -> new ResourceNotFoundException(null, "Historial de Caja para fecha " + date));
        return convertToDTO(history);
    }

    public List<CashRegisterHistoryDTO> listAll() {
        return cashRegisterHistoryRepository.findAllByOrderByDateDesc()
                .stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<CashRegisterHistoryDTO> listByDateRange(LocalDate startDate, LocalDate endDate) {
        return cashRegisterHistoryRepository.findByDateBetween(startDate, endDate)
                .stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private CashRegisterHistoryDTO createHistoryForDate(LocalDate date) {
        CashRegisterEntity cashRegister = cashRegisterService.getOrCreate();
        CashRegisterHistoryEntity history = CashRegisterHistoryEntity.builder()
                .cashRegister(cashRegister)
                .initialAmount(cashRegister.getAmount())
                .finalAmount(null)
                .date(date)
                .build();
        return convertToDTO(cashRegisterHistoryRepository.save(history));
    }

    private CashRegisterHistoryDTO convertToDTO(CashRegisterHistoryEntity entity) {
        if (entity == null) return null;
        return CashRegisterHistoryDTO.builder()
                .id(entity.getId())
                .cashRegister(cashRegisterService.getCashRegister())
                .initialAmount(entity.getInitialAmount())
                .finalAmount(entity.getFinalAmount())
                .date(entity.getDate())
                .build();
    }
}


