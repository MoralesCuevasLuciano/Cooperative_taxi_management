package com.pepotec.cooperative_taxi_managment.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import com.pepotec.cooperative_taxi_managment.services.DriverSettlementService;
import com.pepotec.cooperative_taxi_managment.models.dto.driversettlement.DriverSettlementDTO;
import com.pepotec.cooperative_taxi_managment.models.dto.driversettlement.DriverSettlementCreateDTO;
import org.springframework.web.bind.annotation.RequestMapping;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@ApiResponses(
    value = {
        @ApiResponse(responseCode = "200", description = "Operación exitosa"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    }
)
@Tag(
    name = "Driver Settlements",
    description = "API para la gestión de rendiciones de choferes de la cooperativa"
)
@RestController
@RequestMapping("/driver-settlements")
public class DriverSettlementController {

    @Autowired
    private DriverSettlementService driverSettlementService;

    @Operation(
        summary = "Crear una nueva rendición de chofer",
        description = "Crea una nueva rendición de chofer para un chofer existente.",
        tags = {"Driver Settlements"},
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Rendición de chofer creada exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DriverSettlementDTO.class)
                )
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
        }
    )
    @PostMapping("/drivers/{driverId}/settlements")
    public ResponseEntity<DriverSettlementDTO> createDriverSettlement(
        @PathVariable Long driverId,
        @Valid @RequestBody DriverSettlementCreateDTO settlement
    ) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(driverSettlementService.createDriverSettlement(driverId, settlement));
    }

    @Operation(
        summary = "Obtener todas las rendiciones de choferes",
        description = "Retorna una lista con todas las rendiciones de choferes registradas.",
        tags = {"Driver Settlements"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de rendiciones de choferes",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DriverSettlementDTO.class)
                )
            )
        }
    )
    @GetMapping("/list")
    public ResponseEntity<List<DriverSettlementDTO>> getAllDriverSettlements() {
        return ResponseEntity.ok(driverSettlementService.getAllDriverSettlements());
    }

    @Operation(
        summary = "Obtener una rendición de chofer por ID",
        description = "Busca y retorna una rendición de chofer por su ID.",
        tags = {"Driver Settlements"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Rendición de chofer encontrada",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DriverSettlementDTO.class)
                )
            ),
            @ApiResponse(responseCode = "404", description = "Rendición de chofer no encontrada")
        }
    )
    @GetMapping("/get/{id}")
    public ResponseEntity<DriverSettlementDTO> getDriverSettlementById(@PathVariable Long id) {
        return ResponseEntity.ok(driverSettlementService.getDriverSettlementById(id));
    }

    @Operation(
        summary = "Obtener rendiciones de choferes por chofer",
        description = "Retorna todas las rendiciones de choferes asociadas a un chofer específico.",
        tags = {"Driver Settlements"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de rendiciones de choferes",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DriverSettlementDTO.class)
                )
            ),
            @ApiResponse(responseCode = "400", description = "ID de chofer inválido")
        }
    )
    @GetMapping("/get/by-driver/{driverId}")
    public ResponseEntity<List<DriverSettlementDTO>> getDriverSettlementsByDriver(@PathVariable Long driverId) {
        return ResponseEntity.ok(driverSettlementService.getDriverSettlementsByDriver(driverId));
    }

    @Operation(
        summary = "Obtener rendiciones de choferes por fecha de entrega",
        description = "Retorna todas las rendiciones de choferes con una fecha de entrega específica.",
        tags = {"Driver Settlements"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de rendiciones de choferes",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DriverSettlementDTO.class)
                )
            ),
            @ApiResponse(responseCode = "400", description = "Fecha inválida")
        }
    )
    @GetMapping("/get/by-submission-date/{submissionDate}")
    public ResponseEntity<List<DriverSettlementDTO>> getDriverSettlementsBySubmissionDate(
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate submissionDate
    ) {
        return ResponseEntity.ok(driverSettlementService.getDriverSettlementsBySubmissionDate(submissionDate));
    }

    @Operation(
        summary = "Obtener rendiciones de choferes por rango de fechas de entrega",
        description = "Retorna todas las rendiciones de choferes con fecha de entrega dentro del rango especificado.",
        tags = {"Driver Settlements"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de rendiciones de choferes",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DriverSettlementDTO.class)
                )
            ),
            @ApiResponse(responseCode = "400", description = "Fechas inválidas")
        }
    )
    @GetMapping("/get/by-submission-date-range")
    public ResponseEntity<List<DriverSettlementDTO>> getDriverSettlementsBySubmissionDateRange(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return ResponseEntity.ok(driverSettlementService.getDriverSettlementsBySubmissionDateRange(startDate, endDate));
    }

    @Operation(
        summary = "Obtener rendiciones de choferes por chofer y rango de fechas de entrega",
        description = "Retorna todas las rendiciones de choferes de un chofer específico con fecha de entrega dentro del rango especificado.",
        tags = {"Driver Settlements"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de rendiciones de choferes",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DriverSettlementDTO.class)
                )
            ),
            @ApiResponse(responseCode = "400", description = "ID de chofer o fechas inválidas")
        }
    )
    @GetMapping("/get/by-driver/{driverId}/submission-date-range")
    public ResponseEntity<List<DriverSettlementDTO>> getDriverSettlementsByDriverAndSubmissionDateRange(
        @PathVariable Long driverId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return ResponseEntity.ok(driverSettlementService.getDriverSettlementsByDriverAndSubmissionDateRange(driverId, startDate, endDate));
    }

    @Operation(
        summary = "Calcular el total de tickets de una rendición",
        description = "Calcula y retorna el total de tickets asociados a una rendición específica.",
        tags = {"Driver Settlements"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Total de tickets calculado",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(type = "number", format = "double")
                )
            ),
            @ApiResponse(responseCode = "400", description = "ID de rendición inválido"),
            @ApiResponse(responseCode = "404", description = "Rendición no encontrada")
        }
    )
    @GetMapping("/calculate/total-tickets/{settlementId}")
    public ResponseEntity<Double> calculateTotalTickets(@PathVariable Long settlementId) {
        return ResponseEntity.ok(driverSettlementService.calculateTotalTickets(settlementId));
    }

    @Operation(
        summary = "Calcular el saldo final de una rendición",
        description = "Calcula y retorna el saldo final de una rendición basado en los montos de tickets, vouchers y diferencia.",
        tags = {"Driver Settlements"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Saldo final calculado",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(type = "number", format = "double")
                )
            ),
            @ApiResponse(responseCode = "400", description = "Datos de rendición inválidos")
        }
    )
    @PostMapping("/calculate/final-balance")
    public ResponseEntity<Double> calculateFinalBalance(@Valid @RequestBody DriverSettlementDTO settlement) {
        return ResponseEntity.ok(driverSettlementService.calculateFinalBalance(settlement));
    }

    @Operation(
        summary = "Actualizar una rendición de chofer existente",
        description = "Actualiza los datos de una rendición de chofer existente por su ID.",
        tags = {"Driver Settlements"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Rendición de chofer actualizada exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DriverSettlementDTO.class)
                )
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Rendición de chofer no encontrada")
        }
    )
    @PutMapping("/update/{id}")
    public ResponseEntity<DriverSettlementDTO> updateDriverSettlement(
        @PathVariable Long id,
        @Valid @RequestBody DriverSettlementDTO settlement
    ) {
        settlement.setId(id);
        return ResponseEntity.ok(driverSettlementService.updateDriverSettlement(settlement));
    }

    @Operation(
        summary = "Eliminar una rendición de chofer",
        description = "Elimina una rendición de chofer por su ID.",
        tags = {"Driver Settlements"},
        responses = {
            @ApiResponse(responseCode = "204", description = "Rendición de chofer eliminada exitosamente"),
            @ApiResponse(responseCode = "400", description = "ID inválido"),
            @ApiResponse(responseCode = "404", description = "Rendición de chofer no encontrada")
        }
    )
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteDriverSettlement(@PathVariable Long id) {
        driverSettlementService.deleteDriverSettlement(id);
        return ResponseEntity.noContent().build();
    }
}

