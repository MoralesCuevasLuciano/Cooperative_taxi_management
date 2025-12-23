package com.pepotec.cooperative_taxi_managment.controllers;

import com.pepotec.cooperative_taxi_managment.models.dto.cashregister.CashRegisterHistoryDTO;
import com.pepotec.cooperative_taxi_managment.services.CashRegisterHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@ApiResponses(
    value = {
        @ApiResponse(responseCode = "200", description = "Operación exitosa"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    }
)
@Tag(
    name = "Cash Register History",
    description = "API para la gestión del historial diario de caja"
)
@RestController
@RequestMapping("/cash-register-history")
public class CashRegisterHistoryController {

    @Autowired
    private CashRegisterHistoryService cashRegisterHistoryService;

    @Operation(
        summary = "Abrir día de caja",
        description = "Crea el historial para el día actual si no existe, con el monto inicial de la caja.",
        tags = {"Cash Register History"},
        responses = {
            @ApiResponse(responseCode = "200", description = "Historial creado/obtenido", content = @Content(schema = @Schema(implementation = CashRegisterHistoryDTO.class)))
        }
    )
    @PostMapping("/open-day")
    public ResponseEntity<CashRegisterHistoryDTO> openDay() {
        return ResponseEntity.ok(cashRegisterHistoryService.ensureTodayHistoryExists());
    }

    @Operation(
        summary = "Cerrar día de caja",
        description = "Cierra el historial del día actual estableciendo el monto final con el valor actual de la caja.",
        tags = {"Cash Register History"},
        responses = {
            @ApiResponse(responseCode = "200", description = "Historial cerrado", content = @Content(schema = @Schema(implementation = CashRegisterHistoryDTO.class)))
        }
    )
    @PostMapping("/close-day")
    public ResponseEntity<CashRegisterHistoryDTO> closeDay() {
        return ResponseEntity.ok(cashRegisterHistoryService.closeTodayHistory());
    }

    @Operation(
        summary = "Obtener historial por ID",
        description = "Devuelve el historial de caja por su ID.",
        tags = {"Cash Register History"},
        responses = {
            @ApiResponse(responseCode = "200", description = "Historial encontrado", content = @Content(schema = @Schema(implementation = CashRegisterHistoryDTO.class))),
            @ApiResponse(responseCode = "404", description = "Historial no encontrado")
        }
    )
    @GetMapping("/get/{id}")
    public ResponseEntity<CashRegisterHistoryDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(cashRegisterHistoryService.getById(id));
    }

    @Operation(
        summary = "Obtener historial por fecha",
        description = "Devuelve el historial de caja para la fecha indicada (formato yyyy-MM-dd).",
        tags = {"Cash Register History"},
        responses = {
            @ApiResponse(responseCode = "200", description = "Historial encontrado", content = @Content(schema = @Schema(implementation = CashRegisterHistoryDTO.class))),
            @ApiResponse(responseCode = "404", description = "Historial no encontrado")
        }
    )
    @GetMapping("/get/by-date/{date}")
    public ResponseEntity<CashRegisterHistoryDTO> getByDate(
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        return ResponseEntity.ok(cashRegisterHistoryService.getByDate(date));
    }

    @Operation(
        summary = "Listar historiales",
        description = "Lista todos los historiales de caja ordenados por fecha descendente.",
        tags = {"Cash Register History"},
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida", content = @Content(schema = @Schema(implementation = CashRegisterHistoryDTO.class)))
        }
    )
    @GetMapping("/list")
    public ResponseEntity<List<CashRegisterHistoryDTO>> list() {
        return ResponseEntity.ok(cashRegisterHistoryService.listAll());
    }

    @Operation(
        summary = "Obtener historiales por rango de fechas",
        description = "Devuelve los historiales entre startDate y endDate (yyyy-MM-dd).",
        tags = {"Cash Register History"},
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida", content = @Content(schema = @Schema(implementation = CashRegisterHistoryDTO.class)))
        }
    )
    @GetMapping("/get/by-date-range")
    public ResponseEntity<List<CashRegisterHistoryDTO>> getByDateRange(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        return ResponseEntity.ok(cashRegisterHistoryService.listByDateRange(startDate, endDate));
    }
}


