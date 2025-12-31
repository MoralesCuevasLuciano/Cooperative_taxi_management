package com.pepotec.cooperative_taxi_managment.services;

import com.pepotec.cooperative_taxi_managment.models.dto.accounthistory.AccountHistoryCreateDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;

/**
 * Servicio que se encarga de generar automáticamente los historiales de cuenta
 * al inicio de cada mes.
 * 
 * Se ejecuta el día 1 de cada mes a las 00:00:00 para crear los historiales
 * del mes anterior con el saldo de cierre de cada cuenta activa.
 */
@Service
public class AccountHistorySchedulerService {

    private static final Logger logger = LoggerFactory.getLogger(AccountHistorySchedulerService.class);

    @Autowired
    private AccountHistoryService accountHistoryService;

    @Autowired
    private MemberAccountService memberAccountService;

    @Autowired
    private SubscriberAccountService subscriberAccountService;

    @Autowired
    private VehicleAccountService vehicleAccountService;

    /**
     * Genera automáticamente los historiales de cuenta para el mes anterior.
     * 
     * Se ejecuta automáticamente el día 1 de cada mes a las 00:00:00 mediante @Scheduled.
     * También puede ser llamado manualmente desde un endpoint de administración.
     * 
     * Ejemplo: Si se ejecuta el 01/01/2026 a las 00:00:00, crea historiales
     * para diciembre 2025 (period = "2025-12") con el saldo actual de cada cuenta.
     */
    @Scheduled(cron = "0 0 0 1 * ?") // Ejecuta el día 1 de cada mes a las 00:00:00
    @Transactional
    public void generateMonthlyAccountHistories() {
        logger.info("Starting automatic generation of account histories for previous month");

        try {
            // Obtener el mes anterior
            YearMonth previousMonth = YearMonth.now().minusMonths(1);
            LocalDate registrationDate = LocalDate.now(); // Fecha actual (día 1 del nuevo mes)

            logger.info("Generating account histories for period: {} (registration date: {})", 
                previousMonth, registrationDate);

            // Generar historiales para cuentas de socios activas
            generateMemberAccountHistories(previousMonth, registrationDate);

            // Generar historiales para cuentas de abonados activas
            generateSubscriberAccountHistories(previousMonth, registrationDate);

            // Generar historiales para cuentas de vehículos activas
            generateVehicleAccountHistories(previousMonth, registrationDate);

            logger.info("Successfully completed automatic generation of account histories");
        } catch (Exception e) {
            logger.error("Error generating account histories automatically", e);
            // No relanzamos la excepción para que el scheduler continúe funcionando
        }
    }

    /**
     * Genera historiales para todas las cuentas de socios activas.
     */
    private void generateMemberAccountHistories(YearMonth period, LocalDate registrationDate) {
        logger.debug("Generating member account histories");
        
        memberAccountService.getActiveMemberAccounts().forEach(account -> {
            try {
                // Verificar si ya existe un historial para este período
                String periodStr = period.toString();
                if (accountHistoryService.listByMemberAccount(account.getId()).stream()
                    .anyMatch(h -> h.getYearMonth() != null && h.getYearMonth().toString().equals(periodStr))) {
                    logger.debug("Account history already exists for member account {} and period {}", 
                        account.getId(), period);
                    return;
                }

                AccountHistoryCreateDTO dto = AccountHistoryCreateDTO.builder()
                    .memberAccountId(account.getId())
                    .yearMonth(period)
                    .registrationDate(registrationDate)
                    .monthEndBalance(account.getBalance())
                    .build();

                accountHistoryService.create(dto);
                logger.debug("Created account history for member account {} with balance {}", 
                    account.getId(), account.getBalance());
            } catch (Exception e) {
                logger.error("Error creating account history for member account {}", account.getId(), e);
                // Continuamos con la siguiente cuenta aunque falle una
            }
        });
    }

    /**
     * Genera historiales para todas las cuentas de abonados activas.
     */
    private void generateSubscriberAccountHistories(YearMonth period, LocalDate registrationDate) {
        logger.debug("Generating subscriber account histories");
        
        subscriberAccountService.getActiveSubscriberAccounts().forEach(account -> {
            try {
                // Verificar si ya existe un historial para este período
                String periodStr = period.toString();
                if (accountHistoryService.listBySubscriberAccount(account.getId()).stream()
                    .anyMatch(h -> h.getYearMonth() != null && h.getYearMonth().toString().equals(periodStr))) {
                    logger.debug("Account history already exists for subscriber account {} and period {}", 
                        account.getId(), period);
                    return;
                }

                AccountHistoryCreateDTO dto = AccountHistoryCreateDTO.builder()
                    .subscriberAccountId(account.getId())
                    .yearMonth(period)
                    .registrationDate(registrationDate)
                    .monthEndBalance(account.getBalance())
                    .build();

                accountHistoryService.create(dto);
                logger.debug("Created account history for subscriber account {} with balance {}", 
                    account.getId(), account.getBalance());
            } catch (Exception e) {
                logger.error("Error creating account history for subscriber account {}", account.getId(), e);
                // Continuamos con la siguiente cuenta aunque falle una
            }
        });
    }

    /**
     * Genera historiales para todas las cuentas de vehículos activas.
     */
    private void generateVehicleAccountHistories(YearMonth period, LocalDate registrationDate) {
        logger.debug("Generating vehicle account histories");
        
        vehicleAccountService.getActiveVehicleAccounts().forEach(account -> {
            try {
                // Verificar si ya existe un historial para este período
                String periodStr = period.toString();
                if (accountHistoryService.listByVehicleAccount(account.getId()).stream()
                    .anyMatch(h -> h.getYearMonth() != null && h.getYearMonth().toString().equals(periodStr))) {
                    logger.debug("Account history already exists for vehicle account {} and period {}", 
                        account.getId(), period);
                    return;
                }

                AccountHistoryCreateDTO dto = AccountHistoryCreateDTO.builder()
                    .vehicleAccountId(account.getId())
                    .yearMonth(period)
                    .registrationDate(registrationDate)
                    .monthEndBalance(account.getBalance())
                    .build();

                accountHistoryService.create(dto);
                logger.debug("Created account history for vehicle account {} with balance {}", 
                    account.getId(), account.getBalance());
            } catch (Exception e) {
                logger.error("Error creating account history for vehicle account {}", account.getId(), e);
                // Continuamos con la siguiente cuenta aunque falle una
            }
        });
    }
}

