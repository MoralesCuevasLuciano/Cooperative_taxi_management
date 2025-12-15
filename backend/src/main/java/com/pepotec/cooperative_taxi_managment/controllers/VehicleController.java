package com.pepotec.cooperative_taxi_managment.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import com.pepotec.cooperative_taxi_managment.services.VehicleService;
import com.pepotec.cooperative_taxi_managment.models.dto.vehicle.VehicleDTO;
import com.pepotec.cooperative_taxi_managment.models.dto.vehicle.VehicleCreateDTO;
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
    name = "Vehicles",
    description = "API para la gestión de vehículos de la cooperativa"
)
@RestController
@RequestMapping("/vehicles")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @Operation(
        summary = "Crear un nuevo vehículo",
        description = "Crea un nuevo vehículo con la información proporcionada.",
        tags = {"Vehicles"},
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Vehículo creado exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = VehicleDTO.class)
                )
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "409", description = "Conflicto - Patente, número de licencia, motor o chasis ya existen")
        }
    )
    @PostMapping("/create")
    public ResponseEntity<VehicleDTO> createVehicle(@Valid @RequestBody VehicleCreateDTO vehicle) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(vehicleService.createVehicle(vehicle));
    }

    @Operation(
        summary = "Actualizar un vehículo existente",
        description = "Actualiza los datos de un vehículo existente por su ID.",
        tags = {"Vehicles"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Vehículo actualizado exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = VehicleDTO.class)
                )
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Vehículo no encontrado"),
            @ApiResponse(responseCode = "409", description = "Conflicto - Patente, número de licencia, motor o chasis ya existen")
        }
    )
    @PutMapping("/update/{id}")
    public ResponseEntity<VehicleDTO> updateVehicle(
        @PathVariable Long id,
        @Valid @RequestBody VehicleDTO vehicle
    ) {
        vehicle.setId(id);
        return ResponseEntity.ok(vehicleService.updateVehicle(vehicle));
    }

    @Operation(
        summary = "Eliminar un vehículo",
        description = "Da de baja un vehículo por su ID (soft delete).",
        tags = {"Vehicles"},
        responses = {
            @ApiResponse(responseCode = "204", description = "Vehículo eliminado exitosamente"),
            @ApiResponse(responseCode = "400", description = "ID inválido"),
            @ApiResponse(responseCode = "404", description = "Vehículo no encontrado")
        }
    )
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Eliminar un vehículo con fecha de baja específica",
        description = "Da de baja un vehículo por su ID con una fecha de baja específica.",
        tags = {"Vehicles"},
        responses = {
            @ApiResponse(responseCode = "204", description = "Vehículo eliminado exitosamente"),
            @ApiResponse(responseCode = "400", description = "ID o fecha inválidos"),
            @ApiResponse(responseCode = "404", description = "Vehículo no encontrado")
        }
    )
    @DeleteMapping("/delete/{id}/leave-date/{leaveDate}")
    public ResponseEntity<Void> deleteVehicle(
        @PathVariable Long id,
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate leaveDate
    ) {
        vehicleService.deleteVehicle(id, leaveDate);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Obtener un vehículo por ID",
        description = "Busca y retorna un vehículo por su ID.",
        tags = {"Vehicles"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Vehículo encontrado",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = VehicleDTO.class)
                )
            ),
            @ApiResponse(responseCode = "404", description = "Vehículo no encontrado")
        }
    )
    @GetMapping("/get/{id}")
    public ResponseEntity<VehicleDTO> getVehicleById(@PathVariable Long id) {
        return ResponseEntity.ok(vehicleService.getVehicleById(id));
    }

    @Operation(
        summary = "Obtener un vehículo por patente",
        description = "Busca y retorna un vehículo por su patente.",
        tags = {"Vehicles"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Vehículo encontrado",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = VehicleDTO.class)
                )
            ),
            @ApiResponse(responseCode = "400", description = "Patente inválida"),
            @ApiResponse(responseCode = "404", description = "Vehículo no encontrado")
        }
    )
    @GetMapping("/get/license-plate/{licensePlate}")
    public ResponseEntity<VehicleDTO> getVehicleByLicensePlate(@PathVariable String licensePlate) {
        return ResponseEntity.ok(vehicleService.getVehicleByLicensePlate(licensePlate));
    }

    @Operation(
        summary = "Obtener todos los vehículos",
        description = "Retorna una lista de todos los vehículos (activos e inactivos).",
        tags = {"Vehicles"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de vehículos retornada exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = VehicleDTO.class)
                )
            )
        }
    )
    @GetMapping("/list")
    public ResponseEntity<List<VehicleDTO>> getAllVehicles() {
        return ResponseEntity.ok(vehicleService.getAllVehicles());
    }

    @Operation(
        summary = "Obtener todos los vehículos activos",
        description = "Retorna una lista de todos los vehículos activos.",
        tags = {"Vehicles"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de vehículos activos retornada exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = VehicleDTO.class)
                )
            )
        }
    )
    @GetMapping("/get/active")
    public ResponseEntity<List<VehicleDTO>> getActiveVehicles() {
        return ResponseEntity.ok(vehicleService.getActiveVehicles());
    }

    @Operation(
        summary = "Obtener vehículos por modelo",
        description = "Retorna una lista de vehículos filtrados por modelo.",
        tags = {"Vehicles"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de vehículos retornada exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = VehicleDTO.class)
                )
            )
        }
    )
    @GetMapping("/get/by-model/{modelId}")
    public ResponseEntity<List<VehicleDTO>> getVehiclesByModel(@PathVariable Long modelId) {
        return ResponseEntity.ok(vehicleService.getVehiclesByModel(modelId));
    }
}

