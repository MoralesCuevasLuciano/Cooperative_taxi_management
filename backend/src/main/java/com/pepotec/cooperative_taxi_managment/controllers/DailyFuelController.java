package com.pepotec.cooperative_taxi_managment.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import com.pepotec.cooperative_taxi_managment.services.DailyFuelService;
import com.pepotec.cooperative_taxi_managment.models.dto.DailyFuelDTO;
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
import com.pepotec.cooperative_taxi_managment.models.enums.FuelType;

@ApiResponses(
    value = {
        @ApiResponse(responseCode = "200", description = "Operación exitosa"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    }
)
@Tag(
    name = "Daily Fuel",
    description = "API para la gestión de combustible diario de la cooperativa"
)
@RestController
@RequestMapping("/daily-fuel")
public class DailyFuelController {

    @Autowired
    private DailyFuelService dailyFuelService;

    @Operation(
        summary = "Crear un nuevo registro de combustible diario",
        description = "Crea un nuevo registro de combustible diario con la información proporcionada.",
        tags = {"Daily Fuel"},
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Registro de combustible diario creado exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DailyFuelDTO.class)
                )
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
        }
    )
    @PostMapping("/create")
    public ResponseEntity<DailyFuelDTO> createDailyFuel(@Valid @RequestBody DailyFuelDTO dailyFuel) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(dailyFuelService.createDailyFuel(dailyFuel));
    }

    @Operation(
        summary = "Actualizar un registro de combustible diario existente",
        description = "Actualiza los datos de un registro de combustible diario existente por su ID.",
        tags = {"Daily Fuel"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Registro de combustible diario actualizado exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DailyFuelDTO.class)
                )
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Registro de combustible diario no encontrado")
        }
    )
    @PutMapping("/update/{id}")
    public ResponseEntity<DailyFuelDTO> updateDailyFuel(
        @PathVariable Long id,
        @Valid @RequestBody DailyFuelDTO dailyFuel
    ) {
        dailyFuel.setId(id);
        return ResponseEntity.ok(dailyFuelService.updateDailyFuel(dailyFuel));
    }

    @Operation(
        summary = "Eliminar un registro de combustible diario",
        description = "Elimina un registro de combustible diario por su ID.",
        tags = {"Daily Fuel"},
        responses = {
            @ApiResponse(responseCode = "204", description = "Registro de combustible diario eliminado exitosamente"),
            @ApiResponse(responseCode = "400", description = "ID inválido"),
            @ApiResponse(responseCode = "404", description = "Registro de combustible diario no encontrado")
        }
    )
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteDailyFuel(@PathVariable Long id) {
        dailyFuelService.deleteDailyFuel(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Obtener un registro de combustible diario por ID",
        description = "Busca y retorna un registro de combustible diario por su ID.",
        tags = {"Daily Fuel"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Registro de combustible diario encontrado",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DailyFuelDTO.class)
                )
            ),
            @ApiResponse(responseCode = "404", description = "Registro de combustible diario no encontrado")
        }
    )
    @GetMapping("/get/{id}")
    public ResponseEntity<DailyFuelDTO> getDailyFuelById(@PathVariable Long id) {
        return ResponseEntity.ok(dailyFuelService.getDailyFuelById(id));
    }

    @Operation(
        summary = "Obtener todos los registros de combustible diario",
        description = "Retorna una lista de todos los registros de combustible diario.",
        tags = {"Daily Fuel"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de registros de combustible diario retornada exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DailyFuelDTO.class)
                )
            )
        }
    )
    @GetMapping("/list")
    public ResponseEntity<List<DailyFuelDTO>> getAllDailyFuels() {
        return ResponseEntity.ok(dailyFuelService.getAllDailyFuels());
    }

    @Operation(
        summary = "Obtener registros de combustible diario por vehículo",
        description = "Retorna una lista de registros de combustible diario filtrados por vehículo.",
        tags = {"Daily Fuel"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de registros de combustible diario retornada exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DailyFuelDTO.class)
                )
            )
        }
    )
    @GetMapping("/get/by-vehicle/{vehicleId}")
    public ResponseEntity<List<DailyFuelDTO>> getDailyFuelsByVehicle(@PathVariable Long vehicleId) {
        return ResponseEntity.ok(dailyFuelService.getDailyFuelsByVehicle(vehicleId));
    }

    @Operation(
        summary = "Obtener registros de combustible diario por chofer",
        description = "Retorna una lista de registros de combustible diario filtrados por chofer.",
        tags = {"Daily Fuel"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de registros de combustible diario retornada exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DailyFuelDTO.class)
                )
            )
        }
    )
    @GetMapping("/get/by-driver/{driverId}")
    public ResponseEntity<List<DailyFuelDTO>> getDailyFuelsByDriver(@PathVariable Long driverId) {
        return ResponseEntity.ok(dailyFuelService.getDailyFuelsByDriver(driverId));
    }

    @Operation(
        summary = "Obtener registros de combustible diario por rango de fechas de emisión",
        description = "Retorna una lista de registros de combustible diario filtrados por rango de fechas de emisión del ticket.",
        tags = {"Daily Fuel"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de registros de combustible diario retornada exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DailyFuelDTO.class)
                )
            )
        }
    )
    @GetMapping("/get/by-ticket-issue-date-range")
    public ResponseEntity<List<DailyFuelDTO>> getDailyFuelsByTicketIssueDateRange(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return ResponseEntity.ok(dailyFuelService.getDailyFuelsByTicketIssueDateRange(startDate, endDate));
    }

    @Operation(
        summary = "Obtener registros de combustible diario por rango de fechas de entrega",
        description = "Retorna una lista de registros de combustible diario filtrados por rango de fechas de entrega.",
        tags = {"Daily Fuel"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de registros de combustible diario retornada exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DailyFuelDTO.class)
                )
            )
        }
    )
    @GetMapping("/get/by-submission-date-range")
    public ResponseEntity<List<DailyFuelDTO>> getDailyFuelsBySubmissionDateRange(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return ResponseEntity.ok(dailyFuelService.getDailyFuelsBySubmissionDateRange(startDate, endDate));
    }

    @Operation(
        summary = "Obtener registros de combustible diario por tipo de combustible",
        description = "Retorna una lista de registros de combustible diario filtrados por tipo de combustible (GNC o NAFTA).",
        tags = {"Daily Fuel"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de registros de combustible diario retornada exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DailyFuelDTO.class)
                )
            )
        }
    )
    @GetMapping("/get/by-fuel-type/{fuelType}")
    public ResponseEntity<List<DailyFuelDTO>> getDailyFuelsByFuelType(@PathVariable FuelType fuelType) {
        return ResponseEntity.ok(dailyFuelService.getDailyFuelsByFuelType(fuelType));
    }

    @Operation(
        summary = "Obtener registros de combustible diario por vehículo y rango de fechas de emisión",
        description = "Retorna una lista de registros de combustible diario filtrados por vehículo y rango de fechas de emisión del ticket.",
        tags = {"Daily Fuel"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de registros de combustible diario retornada exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DailyFuelDTO.class)
                )
            )
        }
    )
    @GetMapping("/get/by-vehicle/{vehicleId}/ticket-issue-date-range")
    public ResponseEntity<List<DailyFuelDTO>> getDailyFuelsByVehicleAndTicketIssueDateRange(
        @PathVariable Long vehicleId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return ResponseEntity.ok(dailyFuelService.getDailyFuelsByVehicleAndTicketIssueDateRange(vehicleId, startDate, endDate));
    }

    @Operation(
        summary = "Obtener registros de combustible diario por chofer y rango de fechas de emisión",
        description = "Retorna una lista de registros de combustible diario filtrados por chofer y rango de fechas de emisión del ticket.",
        tags = {"Daily Fuel"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de registros de combustible diario retornada exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DailyFuelDTO.class)
                )
            )
        }
    )
    @GetMapping("/get/by-driver/{driverId}/ticket-issue-date-range")
    public ResponseEntity<List<DailyFuelDTO>> getDailyFuelsByDriverAndTicketIssueDateRange(
        @PathVariable Long driverId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return ResponseEntity.ok(dailyFuelService.getDailyFuelsByDriverAndTicketIssueDateRange(driverId, startDate, endDate));
    }

    @Operation(
        summary = "Obtener registros de combustible diario por vehículo y tipo de combustible",
        description = "Retorna una lista de registros de combustible diario filtrados por vehículo y tipo de combustible.",
        tags = {"Daily Fuel"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de registros de combustible diario retornada exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DailyFuelDTO.class)
                )
            )
        }
    )
    @GetMapping("/get/by-vehicle/{vehicleId}/fuel-type/{fuelType}")
    public ResponseEntity<List<DailyFuelDTO>> getDailyFuelsByVehicleAndFuelType(
        @PathVariable Long vehicleId,
        @PathVariable FuelType fuelType
    ) {
        return ResponseEntity.ok(dailyFuelService.getDailyFuelsByVehicleAndFuelType(vehicleId, fuelType));
    }

    @Operation(
        summary = "Obtener registros de combustible diario por chofer y tipo de combustible",
        description = "Retorna una lista de registros de combustible diario filtrados por chofer y tipo de combustible.",
        tags = {"Daily Fuel"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de registros de combustible diario retornada exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DailyFuelDTO.class)
                )
            )
        }
    )
    @GetMapping("/get/by-driver/{driverId}/fuel-type/{fuelType}")
    public ResponseEntity<List<DailyFuelDTO>> getDailyFuelsByDriverAndFuelType(
        @PathVariable Long driverId,
        @PathVariable FuelType fuelType
    ) {
        return ResponseEntity.ok(dailyFuelService.getDailyFuelsByDriverAndFuelType(driverId, fuelType));
    }
}

