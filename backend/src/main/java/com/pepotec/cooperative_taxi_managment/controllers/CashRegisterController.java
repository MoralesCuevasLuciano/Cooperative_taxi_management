package com.pepotec.cooperative_taxi_managment.controllers;

import com.pepotec.cooperative_taxi_managment.models.dto.cashregister.CashRegisterDTO;
import com.pepotec.cooperative_taxi_managment.services.CashRegisterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@ApiResponses(
    value = {
        @ApiResponse(responseCode = "200", description = "Operación exitosa"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    }
)
@Tag(
    name = "Cash Register",
    description = "API para la gestión de la caja física"
)
@RestController
@RequestMapping("/cash-register")
public class CashRegisterController {

    @Autowired
    private CashRegisterService cashRegisterService;

    @Operation(
        summary = "Obtener caja",
        description = "Devuelve la caja (singleton) con su monto actual.",
        tags = {"Cash Register"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Caja obtenida correctamente",
                content = @Content(schema = @Schema(implementation = CashRegisterDTO.class))
            )
        }
    )
    @GetMapping
    public ResponseEntity<CashRegisterDTO> getCashRegister() {
        return ResponseEntity.ok(cashRegisterService.getCashRegister());
    }

    @Operation(
        summary = "Actualizar monto de caja",
        description = "Actualiza el monto actual de la caja.",
        tags = {"Cash Register"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Caja actualizada correctamente",
                content = @Content(schema = @Schema(implementation = CashRegisterDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
        }
    )
    @PutMapping("/update")
    public ResponseEntity<CashRegisterDTO> updateAmount(@RequestParam Double amount) {
        return ResponseEntity.ok(cashRegisterService.updateAmount(amount));
    }
}


