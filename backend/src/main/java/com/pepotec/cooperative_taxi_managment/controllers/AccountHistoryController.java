package com.pepotec.cooperative_taxi_managment.controllers;

import com.pepotec.cooperative_taxi_managment.models.dto.accounthistory.AccountHistoryCreateDTO;
import com.pepotec.cooperative_taxi_managment.models.dto.accounthistory.AccountHistoryDTO;
import com.pepotec.cooperative_taxi_managment.services.AccountHistoryService;
import com.pepotec.cooperative_taxi_managment.services.AccountHistorySchedulerService;
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
    name = "Account History",
    description = "API para la gestión del historial mensual de saldos de cuentas"
)
@RestController
@RequestMapping("/account-histories")
public class AccountHistoryController {

    @Autowired
    private AccountHistoryService accountHistoryService;

    @Autowired
    private AccountHistorySchedulerService accountHistorySchedulerService;

    @Operation(
        summary = "Crear un nuevo historial de cuenta",
        description = "Crea un nuevo registro de historial de cuenta para registrar el saldo al cierre de un mes.",
        tags = {"Account History"},
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Historial de cuenta creado exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AccountHistoryDTO.class)
                )
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Cuenta no encontrada"),
            @ApiResponse(responseCode = "409", description = "Ya existe un historial para esta cuenta y período")
        }
    )
    @PostMapping("/create")
    public ResponseEntity<AccountHistoryDTO> create(@Valid @RequestBody AccountHistoryCreateDTO dto) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(accountHistoryService.create(dto));
    }

    @Operation(
        summary = "Obtener un historial de cuenta por ID",
        description = "Obtiene los detalles de un historial de cuenta específico por su ID.",
        tags = {"Account History"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Historial de cuenta encontrado",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AccountHistoryDTO.class)
                )
            ),
            @ApiResponse(responseCode = "404", description = "Historial de cuenta no encontrado")
        }
    )
    @GetMapping("/get/{id}")
    public ResponseEntity<AccountHistoryDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(accountHistoryService.getById(id));
    }

    @Operation(
        summary = "Listar todos los historiales de cuenta",
        description = "Obtiene una lista de todos los historiales de cuenta registrados.",
        tags = {"Account History"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de historiales de cuenta",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AccountHistoryDTO.class)
                )
            )
        }
    )
    @GetMapping("/list")
    public ResponseEntity<List<AccountHistoryDTO>> listAll() {
        return ResponseEntity.ok(accountHistoryService.listAll());
    }

    @Operation(
        summary = "Listar historiales por cuenta de socio",
        description = "Obtiene una lista de todos los historiales asociados a una cuenta de socio específica.",
        tags = {"Account History"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de historiales del socio",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AccountHistoryDTO.class)
                )
            )
        }
    )
    @GetMapping("/list/by-member-account/{memberAccountId}")
    public ResponseEntity<List<AccountHistoryDTO>> listByMemberAccount(@PathVariable Long memberAccountId) {
        return ResponseEntity.ok(accountHistoryService.listByMemberAccount(memberAccountId));
    }

    @Operation(
        summary = "Listar historiales por cuenta de abonado",
        description = "Obtiene una lista de todos los historiales asociados a una cuenta de abonado específica.",
        tags = {"Account History"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de historiales del abonado",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AccountHistoryDTO.class)
                )
            )
        }
    )
    @GetMapping("/list/by-subscriber-account/{subscriberAccountId}")
    public ResponseEntity<List<AccountHistoryDTO>> listBySubscriberAccount(@PathVariable Long subscriberAccountId) {
        return ResponseEntity.ok(accountHistoryService.listBySubscriberAccount(subscriberAccountId));
    }

    @Operation(
        summary = "Listar historiales por cuenta de vehículo",
        description = "Obtiene una lista de todos los historiales asociados a una cuenta de vehículo específica.",
        tags = {"Account History"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de historiales del vehículo",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AccountHistoryDTO.class)
                )
            )
        }
    )
    @GetMapping("/list/by-vehicle-account/{vehicleAccountId}")
    public ResponseEntity<List<AccountHistoryDTO>> listByVehicleAccount(@PathVariable Long vehicleAccountId) {
        return ResponseEntity.ok(accountHistoryService.listByVehicleAccount(vehicleAccountId));
    }

    @Operation(
        summary = "Listar historiales por período",
        description = "Obtiene una lista de todos los historiales registrados en un período específico (año-mes).",
        tags = {"Account History"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de historiales del período",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AccountHistoryDTO.class)
                )
            )
        }
    )
    @GetMapping("/list/by-period")
    public ResponseEntity<List<AccountHistoryDTO>> listByPeriod(
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth yearMonth
    ) {
        return ResponseEntity.ok(accountHistoryService.listByPeriod(yearMonth));
    }

    @Operation(
        summary = "Listar historiales por rango de fechas de registro",
        description = "Obtiene una lista de todos los historiales registrados entre dos fechas específicas.",
        tags = {"Account History"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de historiales en el rango de fechas",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AccountHistoryDTO.class)
                )
            ),
            @ApiResponse(responseCode = "400", description = "Fechas inválidas o rango incorrecto")
        }
    )
    @GetMapping("/list/by-registration-date-range")
    public ResponseEntity<List<AccountHistoryDTO>> listByRegistrationDateRange(
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate
    ) {
        return ResponseEntity.ok(accountHistoryService.listByRegistrationDateRange(startDate, endDate));
    }

    @Operation(
        summary = "Actualizar un historial de cuenta existente",
        description = "Actualiza los datos de un historial de cuenta existente por su ID.",
        tags = {"Account History"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Historial de cuenta actualizado exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AccountHistoryDTO.class)
                )
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Historial de cuenta no encontrado"),
            @ApiResponse(responseCode = "409", description = "Ya existe un historial para esta cuenta y período")
        }
    )
    @PutMapping("/update/{id}")
    public ResponseEntity<AccountHistoryDTO> update(
        @PathVariable Long id,
        @Valid @RequestBody AccountHistoryCreateDTO dto
    ) {
        return ResponseEntity.ok(accountHistoryService.update(id, dto));
    }

    @Operation(
        summary = "Eliminar un historial de cuenta",
        description = "Elimina (soft delete) un historial de cuenta por su ID.",
        tags = {"Account History"},
        responses = {
            @ApiResponse(responseCode = "204", description = "Historial de cuenta eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Historial de cuenta no encontrado")
        }
    )
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        accountHistoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Generar historiales de cuenta automáticamente (prueba)",
        description = "Ejecuta manualmente la generación automática de historiales de cuenta para el mes anterior. " +
                     "Este endpoint está diseñado para pruebas y permite ejecutar el proceso que normalmente se ejecuta " +
                     "automáticamente el día 1 de cada mes a las 00:00:00. Genera historiales para todas las cuentas " +
                     "activas (socios, abonados y vehículos) con el saldo actual de cada una.",
        tags = {"Account History"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Proceso de generación de historiales iniciado exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(example = "{\"message\": \"Generación de historiales iniciada. Revisa los logs para ver el progreso.\"}")
                )
            ),
            @ApiResponse(responseCode = "500", description = "Error al iniciar el proceso de generación")
        }
    )
    @PostMapping("/generate-monthly-histories")
    public ResponseEntity<java.util.Map<String, String>> generateMonthlyHistories() {
        // Ejecutar el método del scheduler manualmente
        accountHistorySchedulerService.generateMonthlyAccountHistories();
        
        java.util.Map<String, String> response = new java.util.HashMap<>();
        response.put("message", "Generación de historiales iniciada. Revisa los logs para ver el progreso.");
        response.put("timestamp", java.time.LocalDateTime.now().toString());
        
        return ResponseEntity.ok(response);
    }
}

