package com.pepotec.cooperative_taxi_managment.controllers;

import com.pepotec.cooperative_taxi_managment.models.dto.fuelreimbursement.FuelReimbursementDTO;
import com.pepotec.cooperative_taxi_managment.models.dto.fuelreimbursement.FuelReimbursementCreateDTO;
import com.pepotec.cooperative_taxi_managment.services.FuelReimbursementService;
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
    name = "Fuel Reimbursements",
    description = "API para la gestión de reintegros de combustible"
)
@RestController
@RequestMapping("/fuel-reimbursements")
public class FuelReimbursementController {

    @Autowired
    private FuelReimbursementService fuelReimbursementService;

    @Operation(
        summary = "Crear un nuevo reintegro de combustible",
        description = "Crea un nuevo registro de reintegro de combustible para una cuenta de socio.",
        tags = {"Fuel Reimbursements"},
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Reintegro de combustible creado exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = FuelReimbursementDTO.class)
                )
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Cuenta de socio no encontrada"),
            @ApiResponse(responseCode = "409", description = "Ya existe un registro activo para esta cuenta")
        }
    )
    @PostMapping("/member-accounts/{memberAccountId}")
    public ResponseEntity<FuelReimbursementDTO> createFuelReimbursement(
        @PathVariable Long memberAccountId,
        @Valid @RequestBody FuelReimbursementCreateDTO fuelReimbursement
    ) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(fuelReimbursementService.createFuelReimbursement(memberAccountId, fuelReimbursement));
    }

    @Operation(
        summary = "Obtener un reintegro de combustible por ID",
        description = "Busca y retorna un reintegro de combustible por su ID.",
        tags = {"Fuel Reimbursements"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Reintegro de combustible encontrado",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = FuelReimbursementDTO.class)
                )
            ),
            @ApiResponse(responseCode = "404", description = "Reintegro de combustible no encontrado")
        }
    )
    @GetMapping("/get/{id}")
    public ResponseEntity<FuelReimbursementDTO> getFuelReimbursementById(@PathVariable Long id) {
        return ResponseEntity.ok(fuelReimbursementService.getFuelReimbursementById(id));
    }

    @Operation(
        summary = "Obtener reintegro de combustible por ID de cuenta de socio",
        description = "Busca y retorna el reintegro de combustible para una cuenta de socio. Con OneToOne, solo puede haber uno por cuenta.",
        tags = {"Fuel Reimbursements"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Reintegro de combustible encontrado",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = FuelReimbursementDTO.class)
                )
            ),
            @ApiResponse(responseCode = "400", description = "ID de cuenta inválido"),
            @ApiResponse(responseCode = "404", description = "Reintegro de combustible no encontrado")
        }
    )
    @GetMapping("/get/by-member-account/{memberAccountId}")
    public ResponseEntity<FuelReimbursementDTO> getFuelReimbursementByMemberAccountId(
        @PathVariable Long memberAccountId
    ) {
        return ResponseEntity.ok(fuelReimbursementService.getFuelReimbursementByMemberAccountId(memberAccountId));
    }

    @Operation(
        summary = "Listar todos los reintegros de combustible",
        description = "Retorna una lista de todos los reintegros de combustible.",
        tags = {"Fuel Reimbursements"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de reintegros retornada exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = FuelReimbursementDTO.class)
                )
            )
        }
    )
    @GetMapping("/list")
    public ResponseEntity<List<FuelReimbursementDTO>> getAllFuelReimbursements() {
        return ResponseEntity.ok(fuelReimbursementService.getAllFuelReimbursements());
    }

    @Operation(
        summary = "Obtener reintegro de combustible por ID de cuenta de socio (alias)",
        description = "Alias de get/by-member-account. Con OneToOne, solo puede haber uno por cuenta.",
        tags = {"Fuel Reimbursements"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Reintegro de combustible encontrado",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = FuelReimbursementDTO.class)
                )
            ),
            @ApiResponse(responseCode = "400", description = "ID de cuenta inválido"),
            @ApiResponse(responseCode = "404", description = "Reintegro de combustible no encontrado")
        }
    )
    @GetMapping("/list/by-member-account/{memberAccountId}")
    public ResponseEntity<FuelReimbursementDTO> getFuelReimbursementsByMemberAccountId(
        @PathVariable Long memberAccountId
    ) {
        return ResponseEntity.ok(fuelReimbursementService.getFuelReimbursementsByMemberAccountId(memberAccountId));
    }

    @Operation(
        summary = "Acumular crédito de combustible",
        description = "Acumula un monto al reintegro de combustible de un chofer. Si no existe un registro activo, lo crea automáticamente.",
        tags = {"Fuel Reimbursements"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Crédito acumulado exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = FuelReimbursementDTO.class)
                )
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Cuenta de socio no encontrada")
        }
    )
    @PostMapping("/member-accounts/{memberAccountId}/accumulate")
    public ResponseEntity<FuelReimbursementDTO> accumulateFuelCredit(
        @PathVariable Long memberAccountId,
        @RequestParam Double amount
    ) {
        return ResponseEntity.ok(fuelReimbursementService.accumulateFuelCredit(memberAccountId, amount));
    }

    @Operation(
        summary = "Reintegrar crédito de combustible",
        description = "Reintegra el monto acumulado al balance de la cuenta de socio. Suma el accumulatedAmount al balance y resetea el accumulatedAmount a 0.",
        tags = {"Fuel Reimbursements"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Crédito reintegrado exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = FuelReimbursementDTO.class)
                )
            ),
            @ApiResponse(responseCode = "400", description = "No hay monto acumulado para reintegrar"),
            @ApiResponse(responseCode = "404", description = "Reintegro de combustible no encontrado")
        }
    )
    @PostMapping("/member-accounts/{memberAccountId}/reimburse")
    public ResponseEntity<FuelReimbursementDTO> reimburseFuelCredit(
        @PathVariable Long memberAccountId
    ) {
        return ResponseEntity.ok(fuelReimbursementService.reimburseFuelCredit(memberAccountId));
    }

    @Operation(
        summary = "Actualizar un reintegro de combustible",
        description = "Actualiza los datos de un reintegro de combustible por su ID.",
        tags = {"Fuel Reimbursements"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Reintegro de combustible actualizado exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = FuelReimbursementDTO.class)
                )
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Reintegro de combustible no encontrado")
        }
    )
    @PutMapping("/update/{id}")
    public ResponseEntity<FuelReimbursementDTO> updateFuelReimbursement(
        @PathVariable Long id,
        @Valid @RequestBody FuelReimbursementDTO fuelReimbursement
    ) {
        fuelReimbursement.setId(id);
        return ResponseEntity.ok(fuelReimbursementService.updateFuelReimbursement(fuelReimbursement));
    }

    @Operation(
        summary = "Eliminar un reintegro de combustible",
        description = "Elimina (soft delete) un reintegro de combustible por su ID.",
        tags = {"Fuel Reimbursements"},
        responses = {
            @ApiResponse(responseCode = "204", description = "Reintegro de combustible eliminado exitosamente"),
            @ApiResponse(responseCode = "400", description = "ID inválido"),
            @ApiResponse(responseCode = "404", description = "Reintegro de combustible no encontrado")
        }
    )
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteFuelReimbursement(@PathVariable Long id) {
        fuelReimbursementService.deleteFuelReimbursement(id);
        return ResponseEntity.noContent().build();
    }
}

