package com.pepotec.cooperative_taxi_managment.controllers;

import com.pepotec.cooperative_taxi_managment.models.dto.vehicle.account.VehicleAccountCreateDTO;
import com.pepotec.cooperative_taxi_managment.models.dto.vehicle.account.VehicleAccountDTO;
import com.pepotec.cooperative_taxi_managment.services.VehicleAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    name = "Vehicle Accounts",
    description = "API para la gestión de cuentas de vehículos"
)
@RestController
@RequestMapping("/vehicle-accounts")
public class VehicleAccountController {

    @Autowired
    private VehicleAccountService vehicleAccountService;

    @Operation(
        summary = "Crear una nueva cuenta de vehículo",
        description = "Crea una nueva cuenta para un vehículo existente.",
        tags = {"Vehicle Accounts"},
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Cuenta de vehículo creada exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = VehicleAccountDTO.class)
                )
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Vehículo no encontrado"),
            @ApiResponse(responseCode = "409", description = "El vehículo ya tiene una cuenta asociada")
        }
    )
    @PostMapping("/vehicles/{vehicleId}")
    public ResponseEntity<VehicleAccountDTO> createVehicleAccount(
        @PathVariable Long vehicleId,
        @Valid @RequestBody VehicleAccountCreateDTO account
    ) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(vehicleAccountService.createVehicleAccount(vehicleId, account));
    }

    @Operation(
        summary = "Actualizar una cuenta de vehículo existente",
        description = "Actualiza los datos de una cuenta de vehículo por su ID.",
        tags = {"Vehicle Accounts"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Cuenta de vehículo actualizada exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = VehicleAccountDTO.class)
                )
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Cuenta de vehículo no encontrada")
        }
    )
    @PutMapping("/update/{id}")
    public ResponseEntity<VehicleAccountDTO> updateVehicleAccount(
        @PathVariable Long id,
        @Valid @RequestBody VehicleAccountDTO account
    ) {
        account.setId(id);
        return ResponseEntity.ok(vehicleAccountService.updateVehicleAccount(account));
    }

    @Operation(
        summary = "Eliminar una cuenta de vehículo",
        description = "Elimina una cuenta de vehículo por su ID.",
        tags = {"Vehicle Accounts"},
        responses = {
            @ApiResponse(responseCode = "204", description = "Cuenta de vehículo eliminada exitosamente"),
            @ApiResponse(responseCode = "400", description = "ID inválido"),
            @ApiResponse(responseCode = "404", description = "Cuenta de vehículo no encontrada")
        }
    )
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteVehicleAccount(@PathVariable Long id) {
        vehicleAccountService.deleteVehicleAccount(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Obtener una cuenta de vehículo por ID",
        description = "Busca y retorna una cuenta de vehículo por su ID.",
        tags = {"Vehicle Accounts"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Cuenta de vehículo encontrada",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = VehicleAccountDTO.class)
                )
            ),
            @ApiResponse(responseCode = "404", description = "Cuenta de vehículo no encontrada")
        }
    )
    @GetMapping("/get/{id}")
    public ResponseEntity<VehicleAccountDTO> getVehicleAccountById(@PathVariable Long id) {
        return ResponseEntity.ok(vehicleAccountService.getVehicleAccountById(id));
    }

    @Operation(
        summary = "Obtener una cuenta de vehículo por ID de vehículo",
        description = "Busca y retorna la cuenta de un vehículo por el ID del vehículo.",
        tags = {"Vehicle Accounts"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Cuenta de vehículo encontrada",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = VehicleAccountDTO.class)
                )
            ),
            @ApiResponse(responseCode = "400", description = "ID de vehículo inválido"),
            @ApiResponse(responseCode = "404", description = "Cuenta de vehículo no encontrada")
        }
    )
    @GetMapping("/get/by-vehicle/{vehicleId}")
    public ResponseEntity<VehicleAccountDTO> getVehicleAccountByVehicleId(@PathVariable Long vehicleId) {
        return ResponseEntity.ok(vehicleAccountService.getVehicleAccountByVehicleId(vehicleId));
    }

    @Operation(
        summary = "Obtener todas las cuentas de vehículos",
        description = "Retorna una lista de todas las cuentas de vehículos.",
        tags = {"Vehicle Accounts"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de cuentas de vehículos retornada exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = VehicleAccountDTO.class)
                )
            )
        }
    )
    @GetMapping("/list")
    public ResponseEntity<List<VehicleAccountDTO>> getAllVehicleAccounts() {
        return ResponseEntity.ok(vehicleAccountService.getAllVehicleAccounts());
    }

    @Operation(
        summary = "Obtener todas las cuentas de vehículos activas",
        description = "Retorna una lista de todas las cuentas de vehículos activas.",
        tags = {"Vehicle Accounts"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de cuentas de vehículos activas retornada exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = VehicleAccountDTO.class)
                )
            )
        }
    )
    @GetMapping("/list/active")
    public ResponseEntity<List<VehicleAccountDTO>> getActiveVehicleAccounts() {
        return ResponseEntity.ok(vehicleAccountService.getActiveVehicleAccounts());
    }
}


