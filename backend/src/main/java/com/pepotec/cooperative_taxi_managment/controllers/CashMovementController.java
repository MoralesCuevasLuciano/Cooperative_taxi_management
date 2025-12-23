package com.pepotec.cooperative_taxi_managment.controllers;

import com.pepotec.cooperative_taxi_managment.models.dto.movement.cash.CashMovementCreateDTO;
import com.pepotec.cooperative_taxi_managment.models.dto.movement.cash.CashMovementDTO;
import com.pepotec.cooperative_taxi_managment.services.CashMovementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
    name = "Cash Movements",
    description = "API para la gestión de movimientos de dinero en efectivo"
)
@RestController
@RequestMapping("/cash-movements")
public class CashMovementController {

    @Autowired
    private CashMovementService cashMovementService;

    @Operation(
        summary = "Crear movimiento en efectivo",
        description = "Crea un movimiento de caja en efectivo y actualiza saldos de cuenta y caja.",
        tags = {"Cash Movements"},
        responses = {
            @ApiResponse(responseCode = "200", description = "Movimiento creado", content = @Content(schema = @Schema(implementation = CashMovementDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Cuenta o caja no encontrada")
        }
    )
    @PostMapping("/create")
    public ResponseEntity<CashMovementDTO> create(@Valid @RequestBody CashMovementCreateDTO dto) {
        return ResponseEntity.ok(cashMovementService.create(dto));
    }

    @Operation(
        summary = "Obtener movimiento por ID",
        description = "Devuelve un movimiento de caja por su ID.",
        tags = {"Cash Movements"},
        responses = {
            @ApiResponse(responseCode = "200", description = "Movimiento encontrado", content = @Content(schema = @Schema(implementation = CashMovementDTO.class))),
            @ApiResponse(responseCode = "404", description = "Movimiento no encontrado")
        }
    )
    @GetMapping("/get/{id}")
    public ResponseEntity<CashMovementDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(cashMovementService.getById(id));
    }

    @Operation(
        summary = "Listar movimientos",
        description = "Lista todos los movimientos de caja.",
        tags = {"Cash Movements"},
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida", content = @Content(schema = @Schema(implementation = CashMovementDTO.class)))
        }
    )
    @GetMapping("/list")
    public ResponseEntity<List<CashMovementDTO>> listAll() {
        return ResponseEntity.ok(cashMovementService.listAll());
    }

    @Operation(
        summary = "Listar movimientos activos",
        description = "Lista movimientos de caja con active=true.",
        tags = {"Cash Movements"},
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida", content = @Content(schema = @Schema(implementation = CashMovementDTO.class)))
        }
    )
    @GetMapping("/active")
    public ResponseEntity<List<CashMovementDTO>> listActive() {
        return ResponseEntity.ok(cashMovementService.listActive());
    }

    @Operation(
        summary = "Listar movimientos por rango de fechas",
        description = "Devuelve movimientos entre startDate y endDate (yyyy-MM-dd).",
        tags = {"Cash Movements"},
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida", content = @Content(schema = @Schema(implementation = CashMovementDTO.class)))
        }
    )
    @GetMapping("/by-date-range")
    public ResponseEntity<List<CashMovementDTO>> listByDateRange(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        return ResponseEntity.ok(cashMovementService.listByDateRange(startDate, endDate));
    }

    @Operation(
        summary = "Listar movimientos por cuenta",
        description = "Devuelve movimientos filtrados por cuenta (member, subscriber o vehicle).",
        tags = {"Cash Movements"},
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida", content = @Content(schema = @Schema(implementation = CashMovementDTO.class)))
        }
    )
    @GetMapping("/by-account")
    public ResponseEntity<List<CashMovementDTO>> listByAccount(
            @RequestParam(required = false) Long memberAccountId,
            @RequestParam(required = false) Long subscriberAccountId,
            @RequestParam(required = false) Long vehicleAccountId) {
        return ResponseEntity.ok(cashMovementService.listByAccount(memberAccountId, subscriberAccountId, vehicleAccountId));
    }

    @Operation(
        summary = "Actualizar movimiento en efectivo",
        description = "Actualiza un movimiento de caja, revierte el saldo previo y aplica el nuevo.",
        tags = {"Cash Movements"},
        responses = {
            @ApiResponse(responseCode = "200", description = "Movimiento actualizado", content = @Content(schema = @Schema(implementation = CashMovementDTO.class))),
            @ApiResponse(responseCode = "404", description = "Movimiento no encontrado")
        }
    )
    @PutMapping("/update/{id}")
    public ResponseEntity<CashMovementDTO> update(@PathVariable Long id, @Valid @RequestBody CashMovementCreateDTO dto) {
        return ResponseEntity.ok(cashMovementService.update(id, dto));
    }

    @Operation(
        summary = "Eliminar movimiento en efectivo",
        description = "Soft delete: revierte el saldo y marca el movimiento como inactivo.",
        tags = {"Cash Movements"},
        responses = {
            @ApiResponse(responseCode = "204", description = "Movimiento eliminado"),
            @ApiResponse(responseCode = "404", description = "Movimiento no encontrado")
        }
    )
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cashMovementService.delete(id);
        return ResponseEntity.noContent().build();
    }
}


