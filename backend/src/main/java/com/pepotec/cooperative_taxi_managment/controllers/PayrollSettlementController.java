package com.pepotec.cooperative_taxi_managment.controllers;

import com.pepotec.cooperative_taxi_managment.models.dto.payrollsettlement.PayrollSettlementCreateDTO;
import com.pepotec.cooperative_taxi_managment.models.dto.payrollsettlement.PayrollSettlementDTO;
import com.pepotec.cooperative_taxi_managment.services.PayrollSettlementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
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
    name = "Payroll Settlements",
    description = "API para la gestión de liquidaciones de sueldo"
)
@RestController
@RequestMapping("/payroll-settlements")
public class PayrollSettlementController {

    @Autowired
    private PayrollSettlementService payrollSettlementService;

    @Operation(
        summary = "Crear una nueva liquidación",
        description = "Crea una nueva liquidación de sueldo para un socio (no chofer). Puede asociar adelantos existentes sin liquidación.",
        tags = {"Payroll Settlements"},
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Liquidación creada exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PayrollSettlementDTO.class)
                )
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos, el socio es chofer, o ya existe una liquidación para este período"),
            @ApiResponse(responseCode = "404", description = "Cuenta de socio o adelanto no encontrado"),
            @ApiResponse(responseCode = "409", description = "Ya existe una liquidación para esta cuenta y período")
        }
    )
    @PostMapping("/create")
    public ResponseEntity<PayrollSettlementDTO> create(@Valid @RequestBody PayrollSettlementCreateDTO dto) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(payrollSettlementService.create(dto));
    }

    @Operation(
        summary = "Actualizar una liquidación existente",
        description = "Actualiza los datos de una liquidación por su ID. Puede asociar o desasociar adelantos.",
        tags = {"Payroll Settlements"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Liquidación actualizada exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PayrollSettlementDTO.class)
                )
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o conflicto de unicidad"),
            @ApiResponse(responseCode = "404", description = "Liquidación no encontrada")
        }
    )
    @PutMapping("/update/{id}")
    public ResponseEntity<PayrollSettlementDTO> update(
        @PathVariable Long id,
        @Valid @RequestBody PayrollSettlementCreateDTO dto
    ) {
        return ResponseEntity.ok(payrollSettlementService.update(id, dto));
    }

    @Operation(
        summary = "Obtener una liquidación por ID",
        description = "Obtiene los detalles de una liquidación específica por su ID, incluyendo los adelantos asociados.",
        tags = {"Payroll Settlements"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Liquidación encontrada",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PayrollSettlementDTO.class)
                )
            ),
            @ApiResponse(responseCode = "404", description = "Liquidación no encontrada")
        }
    )
    @GetMapping("/get/{id}")
    public ResponseEntity<PayrollSettlementDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(payrollSettlementService.getById(id));
    }

    @Operation(
        summary = "Listar todas las liquidaciones",
        description = "Obtiene una lista de todas las liquidaciones registradas.",
        tags = {"Payroll Settlements"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de liquidaciones",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PayrollSettlementDTO.class)
                )
            )
        }
    )
    @GetMapping("/list")
    public ResponseEntity<List<PayrollSettlementDTO>> listAll() {
        return ResponseEntity.ok(payrollSettlementService.listAll());
    }

    @Operation(
        summary = "Listar liquidaciones por cuenta de socio",
        description = "Obtiene una lista de liquidaciones asociadas a una cuenta de socio específica.",
        tags = {"Payroll Settlements"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de liquidaciones del socio",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PayrollSettlementDTO.class)
                )
            )
        }
    )
    @GetMapping("/by-account/{memberAccountId}")
    public ResponseEntity<List<PayrollSettlementDTO>> listByAccount(@PathVariable Long memberAccountId) {
        return ResponseEntity.ok(payrollSettlementService.listByAccount(memberAccountId));
    }

    @Operation(
        summary = "Listar liquidaciones por período",
        description = "Obtiene una lista de liquidaciones para un período específico (formato: YYYY-MM, ej: 2025-12).",
        tags = {"Payroll Settlements"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de liquidaciones del período",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PayrollSettlementDTO.class)
                )
            ),
            @ApiResponse(responseCode = "400", description = "Formato de período inválido")
        }
    )
    @GetMapping("/by-period/{yearMonth}")
    public ResponseEntity<List<PayrollSettlementDTO>> listByPeriod(@PathVariable String yearMonth) {
        YearMonth period = YearMonth.parse(yearMonth, DateTimeFormatter.ofPattern("yyyy-MM"));
        return ResponseEntity.ok(payrollSettlementService.listByPeriod(period));
    }

    @Operation(
        summary = "Listar liquidaciones por rango de fechas de pago",
        description = "Obtiene una lista de liquidaciones pagadas dentro de un rango de fechas específico.",
        tags = {"Payroll Settlements"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de liquidaciones pagadas en el rango de fechas",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PayrollSettlementDTO.class)
                )
            ),
            @ApiResponse(responseCode = "400", description = "Fechas inválidas")
        }
    )
    @GetMapping("/by-payment-date-range")
    public ResponseEntity<List<PayrollSettlementDTO>> listByPaymentDateRange(
        @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate startDate,
        @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate endDate
    ) {
        return ResponseEntity.ok(payrollSettlementService.listByPaymentDateRange(startDate, endDate));
    }

    @Operation(
        summary = "Eliminar una liquidación (soft delete)",
        description = "Realiza un soft delete de una liquidación, marcándola como inactiva.",
        tags = {"Payroll Settlements"},
        responses = {
            @ApiResponse(responseCode = "200", description = "Liquidación eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Liquidación no encontrada")
        }
    )
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        payrollSettlementService.delete(id);
        return ResponseEntity.ok().build();
    }
}

