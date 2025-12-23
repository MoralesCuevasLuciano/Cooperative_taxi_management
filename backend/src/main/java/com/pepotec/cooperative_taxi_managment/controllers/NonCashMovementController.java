package com.pepotec.cooperative_taxi_managment.controllers;

import com.pepotec.cooperative_taxi_managment.models.dto.movement.noncash.NonCashMovementCreateDTO;
import com.pepotec.cooperative_taxi_managment.models.dto.movement.noncash.NonCashMovementDTO;
import com.pepotec.cooperative_taxi_managment.services.NonCashMovementService;
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
    name = "Non Cash Movements",
    description = "API para la gestión de movimientos de dinero sin efectivo"
)
@RestController
@RequestMapping("/non-cash-movements")
public class NonCashMovementController {

    @Autowired
    private NonCashMovementService nonCashMovementService;

    @Operation(
        summary = "Crear movimiento no efectivo",
        description = "Crea un movimiento sin efectivo y actualiza el saldo de la cuenta (si aplica).",
        tags = {"Non Cash Movements"},
        responses = {
            @ApiResponse(responseCode = "200", description = "Movimiento creado", content = @Content(schema = @Schema(implementation = NonCashMovementDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Cuenta no encontrada")
        }
    )
    @PostMapping("/create")
    public ResponseEntity<NonCashMovementDTO> create(@Valid @RequestBody NonCashMovementCreateDTO dto) {
        return ResponseEntity.ok(nonCashMovementService.create(dto));
    }

    @Operation(
        summary = "Obtener movimiento no efectivo por ID",
        description = "Devuelve un movimiento sin efectivo por su ID.",
        tags = {"Non Cash Movements"},
        responses = {
            @ApiResponse(responseCode = "200", description = "Movimiento encontrado", content = @Content(schema = @Schema(implementation = NonCashMovementDTO.class))),
            @ApiResponse(responseCode = "404", description = "Movimiento no encontrado")
        }
    )
    @GetMapping("/get/{id}")
    public ResponseEntity<NonCashMovementDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(nonCashMovementService.getById(id));
    }

    @Operation(
        summary = "Listar movimientos no efectivos",
        description = "Lista todos los movimientos sin efectivo.",
        tags = {"Non Cash Movements"},
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida", content = @Content(schema = @Schema(implementation = NonCashMovementDTO.class)))
        }
    )
    @GetMapping("/list")
    public ResponseEntity<List<NonCashMovementDTO>> listAll() {
        return ResponseEntity.ok(nonCashMovementService.listAll());
    }

    @Operation(
        summary = "Listar movimientos no efectivos activos",
        description = "Lista movimientos sin efectivo con active=true.",
        tags = {"Non Cash Movements"},
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida", content = @Content(schema = @Schema(implementation = NonCashMovementDTO.class)))
        }
    )
    @GetMapping("/active")
    public ResponseEntity<List<NonCashMovementDTO>> listActive() {
        return ResponseEntity.ok(nonCashMovementService.listActive());
    }

    @Operation(
        summary = "Listar movimientos no efectivos por rango de fechas",
        description = "Devuelve movimientos entre startDate y endDate (yyyy-MM-dd).",
        tags = {"Non Cash Movements"},
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida", content = @Content(schema = @Schema(implementation = NonCashMovementDTO.class)))
        }
    )
    @GetMapping("/by-date-range")
    public ResponseEntity<List<NonCashMovementDTO>> listByDateRange(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        return ResponseEntity.ok(nonCashMovementService.listByDateRange(startDate, endDate));
    }

    @Operation(
        summary = "Listar movimientos no efectivos por cuenta",
        description = "Devuelve movimientos filtrados por cuenta (member, subscriber o vehicle).",
        tags = {"Non Cash Movements"},
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida", content = @Content(schema = @Schema(implementation = NonCashMovementDTO.class)))
        }
    )
    @GetMapping("/by-account")
    public ResponseEntity<List<NonCashMovementDTO>> listByAccount(
            @RequestParam(required = false) Long memberAccountId,
            @RequestParam(required = false) Long subscriberAccountId,
            @RequestParam(required = false) Long vehicleAccountId) {
        return ResponseEntity.ok(nonCashMovementService.listByAccount(memberAccountId, subscriberAccountId, vehicleAccountId));
    }

    @Operation(
        summary = "Actualizar movimiento no efectivo",
        description = "Actualiza un movimiento sin efectivo, revierte el saldo previo y aplica el nuevo.",
        tags = {"Non Cash Movements"},
        responses = {
            @ApiResponse(responseCode = "200", description = "Movimiento actualizado", content = @Content(schema = @Schema(implementation = NonCashMovementDTO.class))),
            @ApiResponse(responseCode = "404", description = "Movimiento no encontrado")
        }
    )
    @PutMapping("/update/{id}")
    public ResponseEntity<NonCashMovementDTO> update(@PathVariable Long id, @Valid @RequestBody NonCashMovementCreateDTO dto) {
        return ResponseEntity.ok(nonCashMovementService.update(id, dto));
    }

    @Operation(
        summary = "Eliminar movimiento no efectivo",
        description = "Soft delete: revierte el saldo y marca el movimiento como inactivo.",
        tags = {"Non Cash Movements"},
        responses = {
            @ApiResponse(responseCode = "204", description = "Movimiento eliminado"),
            @ApiResponse(responseCode = "404", description = "Movimiento no encontrado")
        }
    )
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        nonCashMovementService.delete(id);
        return ResponseEntity.noContent().build();
    }
}


